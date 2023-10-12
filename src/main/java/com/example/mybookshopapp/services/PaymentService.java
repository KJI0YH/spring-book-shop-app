package com.example.mybookshopapp.services;

import com.example.mybookshopapp.data.PaymentStatusEntity;
import com.example.mybookshopapp.data.UserEntity;
import com.example.mybookshopapp.errors.ApiWrongParameterException;
import com.example.mybookshopapp.errors.PaymentDoesNotExistsException;
import com.example.mybookshopapp.errors.PaymentInitiateException;
import com.example.mybookshopapp.errors.PaymentStatusException;
import com.example.mybookshopapp.repositories.PaymentStatusRepository;
import com.example.mybookshopapp.security.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PaymentService {
    private final PaymentStatusRepository paymentStatusRepository;
    private final TransactionService transactionService;
    private final GatewayPaymentService gatewayPaymentService;
    private final UserRepository userRepository;

    public PaymentStatusEntity getPaymentStatusById(String id) {
        return paymentStatusRepository.findPaymentStatusEntityById(id);
    }

    public String initiatePayment(String sum, String userHash) throws ApiWrongParameterException, PaymentInitiateException {
        int sumAmount;
        try {
            sumAmount = getSum(sum);
        } catch (NullPointerException | NumberFormatException e) {
            throw new ApiWrongParameterException("Invalid sum parameter");
        }

        UserEntity user = userRepository.findByHash(userHash);
        if (user == null)
            throw new ApiWrongParameterException("User with this hash does not exists");

        // TODO create idempotence key
        String idempotenceKey = String.valueOf(UUID.randomUUID());
        String returnUrl = "http://localhost:8085/payment/confirm?key=" + idempotenceKey;
        String description = "Account replenishment";

        JsonNode payment = gatewayPaymentService.createPayment(idempotenceKey, sumAmount, "RUB", returnUrl, description);
        String paymentId = payment.at("/id").asText();
        String paymentStatus = payment.at("/status").asText();
        String confirmationUrl = payment.at("/confirmation/confirmation_url").asText();
        savePayment(idempotenceKey, user, paymentId, paymentStatus);
        return confirmationUrl;
    }

    public void savePayment(String key, UserEntity user, String paymentId, String status) {
        PaymentStatusEntity paymentStatus = new PaymentStatusEntity();
        paymentStatus.setId(key);
        paymentStatus.setUser(user);
        paymentStatus.setPaymentId(paymentId);
        paymentStatus.setStatus(status);
        paymentStatusRepository.save(paymentStatus);
    }

    public void updatePaymentStatus(PaymentStatusEntity payment, String newStatus) {
        payment.setStatus(newStatus);
        paymentStatusRepository.save(payment);
    }

    private void chargePayment(PaymentStatusEntity payment, Integer sum, String description) {
        UserEntity user = payment.getUser();
        if (!payment.getIsProcessed()) {
            transactionService.saveReplenishmentTransaction(user, sum, description);
            payment.setIsProcessed(true);
            paymentStatusRepository.save(payment);
        }
    }

    private int getSum(String sumString) {
        return (int) (Double.parseDouble(sumString.replace(',', '.')) * 100);
    }

    // Executes every 60,000 milliseconds (1 minute)
    @Scheduled(fixedRate = 60000)
    @Async
    protected void processUnchargedPayments() {
        List<PaymentStatusEntity> unchargedPayments = getUnchargedPayments();

        for (PaymentStatusEntity payment : unchargedPayments) {
            processPayment(payment);
        }
    }

    public void processPayment(PaymentStatusEntity payment) {
        try {
            JsonNode onlinePayment = gatewayPaymentService.getPayment(payment.getPaymentId());
            String onlineStatus = onlinePayment.at("/status").asText();

            if (isStatusChanged(payment, onlineStatus)) {
                updatePaymentStatus(payment, onlineStatus);
            }

            if (isNeedCharge(payment)) {
                Integer sum = getSum(onlinePayment.at("/amount/value").asText());
                String description = onlinePayment.at("/description").asText();
                chargePayment(payment, sum, description);
            }
        } catch (PaymentDoesNotExistsException e) {
            paymentStatusRepository.delete(payment);
        } catch (PaymentStatusException ignored) {

        }
    }

    public List<PaymentStatusEntity> getUnchargedPayments() {
        return paymentStatusRepository.findAllByIsProcessedIsFalseAndStatus("succeeded");
    }

    private boolean isStatusChanged(PaymentStatusEntity payment, String status) {
        return !payment.getStatus().equals(status);
    }

    private boolean isNeedCharge(PaymentStatusEntity payment) {
        return payment.getStatus().equals("succeeded") && !payment.getIsProcessed();
    }
}
