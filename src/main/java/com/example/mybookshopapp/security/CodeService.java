package com.example.mybookshopapp.security;

import com.example.mybookshopapp.data.SmsCodeEntity;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class CodeService {

    @Value("${twilio.ACCOUNT_SID}")
    private String ACCOUNT_SID;

    @Value("${twilio.AUTH_TOKEN}")
    private String AUTH_TOKEN;

    @Value("${twilio.PHONE_NUMBER}")
    private String PHONE_NUMBER;

    private final CodeRepository codeRepository;
    private final JavaMailSender javaMailSender;

    @Autowired
    public CodeService(CodeRepository codeRepository, JavaMailSender javaMailSender) {
        this.codeRepository = codeRepository;
        this.javaMailSender = javaMailSender;
    }

    public String sendCodeToPhone(String phone){
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        String formattedContact = phone.replaceAll("[( )-]", "");
        String generatedCode = generateCode();
        Message.creator(
                new PhoneNumber(formattedContact),
                new PhoneNumber(PHONE_NUMBER),
                "Your secret code is: " + generatedCode
        ).create();
        return generatedCode;
    }

    public String sendCodeToEmail(String email){
        String generatedCode = generateCode();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("bookstore.test@mail.ru");
        message.setTo(email);
        message.setSubject("Bookstore email verification");
        message.setText("Verification code is: " + generatedCode);
        javaMailSender.send(message);
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
