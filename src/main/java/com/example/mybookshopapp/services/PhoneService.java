package com.example.mybookshopapp.services;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

@Service
public class PhoneService {

    private final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
    @Value("${twilio.ACCOUNT_SID}")
    private String accountSid;
    @Value("${twilio.AUTH_TOKEN}")
    private String authToken;
    @Value("${twilio.PHONE_NUMBER}")
    private String phoneNumber;

    public void sendPhoneMessage(String phone, String message) {
        Twilio.init(accountSid, authToken);
        String formattedPhone = phone.replaceAll("[( )-]", "");
        Message.creator(
                new PhoneNumber(formattedPhone),
                new PhoneNumber(phoneNumber),
                message
        ).create();
    }

    public String generatePhoneConfirmationKey(String phone) {
        String input = phone + ":" + LocalDateTime.now();
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

    public String getPhoneConfirmationLink(String key) {
        return "http://localhost:8085/profile/change/phone?key=" + key;
    }

    private String getDigestString(byte[] digest) {
        StringBuilder builder = new StringBuilder();
        for (byte b : digest) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }

    public boolean isValidPhone(String phone) {
        try {
            return phoneNumberUtil.isValidNumber(phoneNumberUtil.parse(phone, "BY"));
        } catch (NumberParseException e) {
            return false;
        }
    }
}
