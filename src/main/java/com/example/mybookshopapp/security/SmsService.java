package com.example.mybookshopapp.security;

import com.example.mybookshopapp.data.SmsCodeEntity;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class SmsService {

    @Value("${twilio.ACCOUNT_SID}")
    private String ACCOUNT_SID;

    @Value("${twilio.AUTH_TOKEN}")
    private String AUTH_TOKEN;

    @Value("${twilio.PHONE_NUMBER}")
    private String PHONE_NUMBER;

    private final SmsCodeRepository smsCodeRepository;

    @Autowired
    public SmsService(SmsCodeRepository smsCodeRepository) {
        this.smsCodeRepository = smsCodeRepository;
    }

    public String sendSmsCode(String contact){
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        String formattedContact = contact.replaceAll("[( )-]", "");
        String generatedCode = generateCode();
        Message.creator(
                new PhoneNumber(formattedContact),
                new PhoneNumber(PHONE_NUMBER),
                "Your secret code is: " + generatedCode
        ).create();
        return generatedCode;
    }

    private String generateCode(){
        // nnn nnn - pattern
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        while (builder.length() < 6){
            builder.append(random.nextInt(9));
        }
        builder.insert(3, " ");
        return builder.toString();
    }

    public void saveNewSmsCode(SmsCodeEntity smsCode){
        if (smsCodeRepository.findByCode(smsCode.getCode()) == null){
            smsCodeRepository.save(smsCode);
        }
    }

    public Boolean verifySmsCode(String code){
        SmsCodeEntity smsCode = smsCodeRepository.findByCode(code);
        return (smsCode != null && !smsCode.isExpired());
    }
}
