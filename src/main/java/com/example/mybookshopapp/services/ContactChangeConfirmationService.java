package com.example.mybookshopapp.services;

import com.example.mybookshopapp.data.ContactChangeConfirmationEntity;
import com.example.mybookshopapp.data.UserEntity;
import com.example.mybookshopapp.errors.ContactConfirmationException;
import com.example.mybookshopapp.repositories.ContactChangeConfirmationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ContactChangeConfirmationService {

    private final ContactChangeConfirmationRepository contactChangeConfirmationRepository;

    public ContactChangeConfirmationEntity createConfirmation(String key, UserEntity user, String contact) {
        ContactChangeConfirmationEntity confirmation = new ContactChangeConfirmationEntity();
        confirmation.setId(key);
        confirmation.setUser(user);
        confirmation.setContact(contact);
        contactChangeConfirmationRepository.save(confirmation);
        return confirmation;
    }

    public void confirmContactChange(ContactChangeConfirmationEntity contact) {
        contact.setIsConfirmed(true);
        contactChangeConfirmationRepository.save(contact);
    }

    public ContactChangeConfirmationEntity getContactConfirmationByKey(String key) throws ContactConfirmationException {
        ContactChangeConfirmationEntity contact = contactChangeConfirmationRepository.findByIdAndIsConfirmedFalse(key);

        if (contact == null) {
            throw new ContactConfirmationException("Contact already confirmed or does not exists");
        }

        return contact;
    }
}
