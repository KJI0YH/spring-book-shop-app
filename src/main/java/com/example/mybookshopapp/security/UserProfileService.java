package com.example.mybookshopapp.security;

import com.example.mybookshopapp.dto.ContactConfirmationPayload;
import com.example.mybookshopapp.errors.ApproveContactException;
import com.example.mybookshopapp.services.ApproveContactService;
import com.example.mybookshopapp.services.EmailService;
import com.example.mybookshopapp.services.PhoneService;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.twilio.rest.trunking.v1.trunk.PhoneNumber;
import lombok.RequiredArgsConstructor;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserProfileService {
    private final ApproveContactService approveContactService;
    private final PhoneService phoneService;
    private final EmailService emailService;

    public void initiateContactConfirmation(ContactConfirmationPayload payload) throws ApproveContactException {
        String contact = payload.getContact();
        if (contact.contains("@")) {
            initiateEmailConfirmation(contact);
        } else {
            initiatePhoneConfirmation(contact);
        }
    }

    public void approveContact(ContactConfirmationPayload payload) throws ApproveContactException {
        approveContactService.approveContact(payload.getContact(), payload.getCode());
    }

    private void initiateEmailConfirmation(String email) throws ApproveContactException {
        if (!emailService.isValidEmail(email)){
            throw new ApproveContactException("Invalid email address");
        }
        String code = approveContactService.getCode(email);
        emailService.sendEmailMessage(email, "Bookstore email confirmation", "Your confirmation code is: " + code);
    }

    private void initiatePhoneConfirmation(String phone) throws ApproveContactException {
        if (!phoneService.isValidPhone(phone)){
            throw new ApproveContactException("Invalid phone number");
        }
        String code = approveContactService.getCode(phone);
        phoneService.sendPhoneMessage(phone, "Bookstore phone confirmation. Your confirmation code is: " + code);
    }
}
