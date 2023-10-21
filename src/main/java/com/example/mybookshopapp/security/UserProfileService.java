package com.example.mybookshopapp.security;

import com.example.mybookshopapp.data.ContactChangeConfirmationEntity;
import com.example.mybookshopapp.data.UserEntity;
import com.example.mybookshopapp.dto.ContactConfirmationPayload;
import com.example.mybookshopapp.errors.*;
import com.example.mybookshopapp.repositories.UserRepository;
import com.example.mybookshopapp.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserProfileService {
    private final ApproveContactService approveContactService;
    private final PhoneService phoneService;
    private final EmailService emailService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final ContactChangeConfirmationService confirmationService;

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
        if (!emailService.isValidEmail(email)) {
            throw new ApproveContactException("Invalid email address");
        }
        String code = approveContactService.getCode(email);
        emailService.sendEmailMessage(email, "Bookstore email confirmation", "Your confirmation code is: " + code);
    }

    private void initiatePhoneConfirmation(String phone) throws ApproveContactException {
        if (!phoneService.isValidPhone(phone)) {
            throw new ApproveContactException("Invalid phone number");
        }
        String code = approveContactService.getCode(phone);
        phoneService.sendPhoneMessage(phone, "Bookstore phone confirmation. Your confirmation code is: " + code);
    }

    public void initiateChangeEmailConfirmation(UserEntity user, String newEmail) throws UserUnauthorizedException, ApiWrongParameterException, UserAlreadyExistException {
        if (user == null)
            throw new UserUnauthorizedException("User unauthorized");

        if (newEmail.isEmpty() || !emailService.isValidEmail(newEmail))
            throw new ApiWrongParameterException("Invalid email address");

        if (userRepository.findUserEntityByEmail(newEmail) != null)
            throw new UserAlreadyExistException("User with this email already exists");

        if (user.getEmail().equals(newEmail))
            throw new ApiWrongParameterException("The new email address cannot be the same as the old one");

        String key = emailService.generateEmailConfirmationKey(newEmail);
        confirmationService.createConfirmation(key, user, newEmail);
        String link = emailService.getEmailConfirmationLink(key);
        emailService.sendEmailMessage(newEmail, "Bookstore email change confirmation", "Click on the link to confirm your email: " + link);
    }

    public UserEntity confirmEmailChange(String key) throws ContactConfirmationException, UserAlreadyExistException {
        ContactChangeConfirmationEntity newContact = confirmationService.getContactConfirmationByKey(key);
        UserEntity user = userService.changeEmail(newContact.getUser(), newContact.getContact());
        confirmationService.confirmContactChange(newContact);
        return user;
    }

    public void initiateChangePhoneConfirmation(UserEntity user, String newPhone) throws UserUnauthorizedException, ApiWrongParameterException, UserAlreadyExistException {
        if (user == null)
            throw new UserUnauthorizedException("User unauthorized");

        if (newPhone.isEmpty() || !phoneService.isValidPhone(newPhone))
            throw new ApiWrongParameterException("Invalid phone number");

        if (userRepository.findUserEntityByPhone(newPhone) != null)
            throw new UserAlreadyExistException("User with this phone number already exists");

        if (user.getPhone().equals(newPhone))
            throw new ApiWrongParameterException("The new phone number cannot be the same as the old one");

        String key = phoneService.generatePhoneConfirmationKey(newPhone);
        confirmationService.createConfirmation(key, user, newPhone);
        String link = phoneService.getPhoneConfirmationLink(key);
        phoneService.sendPhoneMessage(newPhone, "Follow the link to confirm your phone change: " + link);
    }

    public UserEntity confirmPhoneChange(String key) throws ContactConfirmationException, UserAlreadyExistException {
        ContactChangeConfirmationEntity newContact = confirmationService.getContactConfirmationByKey(key);
        UserEntity user = userService.changePhone(newContact.getUser(), newContact.getContact());
        confirmationService.confirmContactChange(newContact);
        return user;
    }
}
