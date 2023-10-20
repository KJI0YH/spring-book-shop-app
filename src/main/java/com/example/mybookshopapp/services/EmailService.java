package com.example.mybookshopapp.services;

import lombok.RequiredArgsConstructor;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final EmailValidator emailValidator = EmailValidator.getInstance();
    @Value("${appEmail.email}")
    private String SENDER_EMAIL;

    public void sendEmailMessage(String receiveEmail, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(SENDER_EMAIL);
        message.setTo(receiveEmail);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }

    public String getEmailConfirmationLink(String key) {
        return "http://localhost:8085/profile/change/email?key=" + key;
    }

    public String generateEmailConfirmationKey(String email) {
        String input = email + ":" + LocalDateTime.now().toString();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] inputBytes = input.getBytes(StandardCharsets.UTF_8);
            messageDigest.update(inputBytes);
            byte[] digest = messageDigest.digest();
            return getDigestString(digest);
        } catch (NoSuchAlgorithmException e) {
            return input;
        }
    }

    private String getDigestString(byte[] digest) {
        StringBuilder builder = new StringBuilder();
        for (byte b : digest) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }

    public boolean isValidEmail(String email) {
        return emailValidator.isValid(email);
    }
}
