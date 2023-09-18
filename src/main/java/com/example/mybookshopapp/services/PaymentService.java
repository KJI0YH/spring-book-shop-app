package com.example.mybookshopapp.services;

import com.example.mybookshopapp.data.BalanceTransactionEntity;
import com.example.mybookshopapp.data.PaymentStatusEntity;
import com.example.mybookshopapp.data.UserEntity;
import com.example.mybookshopapp.errors.PaymentDoesNotExistsException;
import com.example.mybookshopapp.errors.PaymentStatusException;
import com.example.mybookshopapp.repositories.BalanceTransactionRepository;
import com.example.mybookshopapp.repositories.PaymentStatusRepository;
import com.example.mybookshopapp.security.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class PaymentService {

    @Value("${yookassa.SECRET_KEY}")
    private String secretKey;

    @Value("${yookassa.shopId}")
    private String shopId;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final String apiUrl = "https://api.yookassa.ru/v3/payments";
    private final PaymentStatusRepository paymentStatusRepository;
    private final BalanceTransactionRepository balanceTransactionRepository;
    private final UserRepository userRepository;

    @Autowired
    public PaymentService(PaymentStatusRepository paymentStatusRepository, BalanceTransactionRepository balanceTransactionRepository, UserRepository userRepository) {
        this.paymentStatusRepository = paymentStatusRepository;
        this.balanceTransactionRepository = balanceTransactionRepository;
        this.userRepository = userRepository;
    }

    public String createPayment(Integer amount, UserEntity user) throws URISyntaxException, IOException, InterruptedException {
        // TODO create idempotence key
        String idempotenceKey = String.valueOf(UUID.randomUUID());

        String jsonPayload = new JSONObject()
                .put("amount", new JSONObject().put("value", amount / 100 + "." + amount % 100).put("currency", "RUB"))
                .put("capture", true)
                .put("confirmation", new JSONObject().put("type", "redirect").put("return_url", "http://localhost:8085/payment/confirm?key=" + idempotenceKey))
                .put("description", "Account replenishment").toString();

        HttpClient httpClient = HttpClient.newBuilder().build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(apiUrl))
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .header("Authorization", "Basic " + getAuthString())
                .header("Idempotence-Key", idempotenceKey)
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200){
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonResponse = objectMapper.readTree(response.body());

            PaymentStatusEntity paymentStatus = new PaymentStatusEntity();
            paymentStatus.setId(idempotenceKey);
            paymentStatus.setUser(user);
            paymentStatus.setPaymentId(jsonResponse.at("/id").asText());
            paymentStatus.setStatus(jsonResponse.at("/status").asText());
            paymentStatusRepository.save(paymentStatus);

            return jsonResponse.at("/confirmation/confirmation_url").asText();
        } else {
            return "";
        }
    }

    private JsonNode getOnlinePayment(String key) throws PaymentDoesNotExistsException, PaymentStatusException {
        Optional<PaymentStatusEntity> paymentStatusOptional = paymentStatusRepository.findById(key);

        if (paymentStatusOptional.isEmpty()){
            throw new PaymentDoesNotExistsException("Payment with key " + key + " does not exists");
        }

        PaymentStatusEntity paymentStatus = paymentStatusOptional.get();

        HttpResponse<String> response;
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(apiUrl + "/" + paymentStatus.getPaymentId()))
                .header("Authorization", "Basic " + getAuthString())
                .build();
            HttpClient httpClient = HttpClient.newBuilder().build();
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return objectMapper.readTree(response.body());
            }
        }
        catch (IOException | InterruptedException | URISyntaxException exception){
            throw new PaymentStatusException("Can not to get payment status");
        }
        throw new PaymentStatusException("Fail to get payment status");
    }

    public void updatePaymentStatus(String key) throws PaymentDoesNotExistsException, PaymentStatusException {

        Optional<PaymentStatusEntity> paymentStatusOptional = paymentStatusRepository.findById(key);

        if (paymentStatusOptional.isEmpty()){
            throw new PaymentDoesNotExistsException("Payment with key " + key + " does not exists");
        }

        PaymentStatusEntity paymentStatus = paymentStatusOptional.get();

        if (paymentStatus.getIsProcessed()){
            return;
        }

        JsonNode onlinePayment = getOnlinePayment(key);
        String onlineStatus = onlinePayment.at("/status").asText();

        // The status of payment is changed
        if (!Objects.equals(paymentStatus.getStatus(), onlineStatus)){

            // Update payment
            paymentStatus.setStatus(onlineStatus);
            paymentStatusRepository.save(paymentStatus);
        }

        // Payment success but not processed
        if (Objects.equals(paymentStatus.getStatus(), "succeeded") && !paymentStatus.getIsProcessed()){
            chargePayment(key, onlinePayment);
        }
    }

    // Accrual of credits to the internal account according to payment
    private void chargePayment(String key, JsonNode onlinePayment) throws PaymentDoesNotExistsException {
        Optional<PaymentStatusEntity> paymentStatusOptional = paymentStatusRepository.findById(key);

        if (paymentStatusOptional.isEmpty()){
            throw new PaymentDoesNotExistsException("Payment with key " + key + " does not exists");
        }

        PaymentStatusEntity paymentStatus = paymentStatusOptional.get();
        UserEntity user = paymentStatus.getUser();

        if (paymentStatus.getIsProcessed()){
            return;
        }

        int sum = 0;
        try {
            sum = SumStringToLong(onlinePayment.at("/amount/value").asText());
        } catch (NumberFormatException exception){
            return;
        }

        BalanceTransactionEntity balanceTransaction = new BalanceTransactionEntity();
        balanceTransaction.setBook(null);
        balanceTransaction.setTime(LocalDateTime.now());
        balanceTransaction.setDescription(onlinePayment.at("/description").asText());
        balanceTransaction.setValue(sum);
        balanceTransaction.setUser(user);
        balanceTransactionRepository.save(balanceTransaction);

        paymentStatus.setIsProcessed(true);
        paymentStatusRepository.save(paymentStatus);

        // TODO add triggers
        user.setBalance(user.getBalance() + sum);
        userRepository.save(user);
    }

    private String getAuthString(){
        String credentials = shopId + ":" + secretKey;
        return Base64.getEncoder().encodeToString(credentials.getBytes());
    }

    public int SumStringToLong(String amount){
        String sumString = amount.replace(',','.');
        return  (int)(Double.parseDouble(sumString) * 100);
    }

    // Executes every 60,000 milliseconds (1 minute)
    @Scheduled(fixedRate = 60000)
    @Async
    protected void checkPaymentStatuses() {
        List<PaymentStatusEntity> unprocessedPayments = getUnprocessedPayments();

        for (PaymentStatusEntity payment : unprocessedPayments){
            processPayment(payment);
        }
    }

    @Async
    protected void processPayment(PaymentStatusEntity payment) {
        try {

            JsonNode onlinePayment = getOnlinePayment(payment.getId());
            String onlineStatus = onlinePayment.at("/status").asText();

            // The status of payment is changed
            if (!Objects.equals(payment.getStatus(), onlineStatus)){

                // Update payment
                payment.setStatus(onlineStatus);
                paymentStatusRepository.save(payment);
            }

            // Payment success but not processed
            if (Objects.equals(payment.getStatus(), "succeeded") && !payment.getIsProcessed()){
                chargePayment(payment.getId(), onlinePayment);
            }

        } catch (PaymentDoesNotExistsException | PaymentStatusException exception){
        }
    }

    public List<PaymentStatusEntity> getUnprocessedPayments(){
        return paymentStatusRepository.findAllByIsProcessedIsFalse();
    }
}
