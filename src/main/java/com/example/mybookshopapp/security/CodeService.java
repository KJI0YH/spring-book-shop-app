package com.example.mybookshopapp.security;

import com.example.mybookshopapp.data.SmsCodeEntity;
import com.example.mybookshopapp.services.EmailService;
import com.example.mybookshopapp.services.PhoneService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CodeService {
    private final CodeRepository codeRepository;
    private final EmailService emailService;
    private final PhoneService phoneService;

    public String sendCodeToPhone(String phone){
        String generatedCode = generateCode();
        phoneService.sendPhoneMessage(phone, "Your secret code is: " + generatedCode);
        return generatedCode;
    }

    public String sendCodeToEmail(String email){
        String generatedCode = generateCode();
        emailService.sendEmailMessage(email, "Bookstore email verification", "Verification code is: " + generatedCode);
        return generatedCode;
    }

    private String generateCode(){
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        while (builder.length() < 6){
            builder.append(random.nextInt(9));
        }
        return builder.toString();
    }

    public void saveCode(SmsCodeEntity smsCode){
        if (codeRepository.findByCode(smsCode.getCode()) == null){
            codeRepository.save(smsCode);
        }
    }

    public Boolean verifyCode(String code){
        SmsCodeEntity smsCode = codeRepository.findByCode(simplifyCode(code));
        return (smsCode != null && !smsCode.isExpired());
    }

    private String simplifyCode(String code){
        return code.replaceAll("\\s|-", "");
    }
}
