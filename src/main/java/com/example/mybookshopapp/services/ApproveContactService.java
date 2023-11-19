package com.example.mybookshopapp.services;

import com.example.mybookshopapp.data.ApproveContactEntity;
import com.example.mybookshopapp.errors.ApproveContactException;
import com.example.mybookshopapp.repositories.ApproveContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApproveContactService {

    private final ApproveContactRepository approveContactRepository;
    private final Random random = new Random();
    @Value("#{new Integer('${contact.confirmation.max-attempts}')}")
    private Integer maxAttempts;
    @Value("#{new Integer('${contact.confirmation.expiration-seconds}')}")
    private Integer expirationTime;
    @Value("#{new Integer('${contact.confirmation.resend-seconds}')}")
    private Integer resendTime;

    public ApproveContactEntity getApproveContact(String contact) {
        return approveContactRepository.findApproveContactEntityByContactAndApprovedIsFalse(contact);
    }

    public void createApproveContact(String contact, String code) {
        ApproveContactEntity approveContact = approveContactRepository.findApproveContactEntityByContact(contact);
        if (approveContact == null) {
            approveContact = new ApproveContactEntity();
            approveContact.setContact(contact);
        }
        approveContact.setApproved(false);
        approveContact.setCode(code);
        approveContact.setExpiredTime(LocalDateTime.now().plusSeconds(expirationTime));
        approveContact.setResendTime(LocalDateTime.now().plusSeconds(resendTime));
        approveContact.setAttempts(0);
        approveContactRepository.save(approveContact);
    }

    public void approveContact(String contact, String code) throws ApproveContactException {
        ApproveContactEntity approveContact = approveContactRepository.findApproveContactEntityByContactAndApprovedIsFalse(contact);
        if (approveContact == null) {
            throw new ApproveContactException("Contact not found. Request contact confirmation");
        }

        if (approveContact.isCodeExpired()) {
            throw new ApproveContactException("Confirmation code has expired. Request a new confirmation code");
        }

        if (approveContact.isAttemptsExpired(maxAttempts)) {
            throw new ApproveContactException("The number of confirmation code input attempts has been exhausted. Request a new confirmation code");
        }

        if (!approveContact.getCode().equals(simplifyCode(code))) {
            approveContact.setAttempts(approveContact.getAttempts() + 1);
            approveContactRepository.save(approveContact);
            int attemptsLeft = maxAttempts - approveContact.getAttempts();
            throw new ApproveContactException("Incorrect code. " + attemptsLeft + " attempts left");
        } else {
            approveContact.setApproved(true);
            approveContactRepository.save(approveContact);
        }
    }

    public String getCode(String contact) throws ApproveContactException {
        ApproveContactEntity approveContact = getApproveContact(contact);

        // First request
        if (approveContact == null) {
            String code = generateCode();
            createApproveContact(contact, code);
            return code;
        }

        // Repeated request
        if (isSendOldCode(approveContact)) {
            approveContact.setResendTime(LocalDateTime.now().plusSeconds(resendTime));
            approveContactRepository.save(approveContact);
            return approveContact.getContact();
        } else if (isSendNewCode(approveContact)) {
            String code = generateCode();
            approveContact.setCode(code);
            approveContact.setAttempts(0);
            approveContact.setResendTime(LocalDateTime.now().plusSeconds(resendTime));
            approveContactRepository.save(approveContact);
            return code;
        }

        // Error code requesting
        StringBuilder builder = new StringBuilder();
        long resendTimeout = Duration.between(LocalDateTime.now(), approveContact.getResendTime()).getSeconds();
        builder
                .append("You can request the code again after ")
                .append(resendTimeout / 60)
                .append(" min ")
                .append(resendTimeout % 60)
                .append(" sec");
        throw new ApproveContactException(builder.toString());
    }

    private boolean isSendOldCode(ApproveContactEntity approveContact) {
        return !approveContact.isCodeExpired() &&
                !approveContact.isAttemptsExpired(maxAttempts) &&
                approveContact.isCanResend();
    }

    private boolean isSendNewCode(ApproveContactEntity approveContact) {
        return (approveContact.isAttemptsExpired(maxAttempts) ||
                approveContact.isCodeExpired())
                && approveContact.isCanResend();
    }

    private String generateCode() {
        StringBuilder builder = new StringBuilder();
        while (builder.length() < 6) {
            builder.append(random.nextInt(9));
        }
        return builder.toString();
    }

    private String simplifyCode(String code) {
        return code.replaceAll("[\\s-]", "");
    }
}
