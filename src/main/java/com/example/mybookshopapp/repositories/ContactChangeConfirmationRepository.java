package com.example.mybookshopapp.repositories;

import com.example.mybookshopapp.data.ContactChangeConfirmationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactChangeConfirmationRepository extends JpaRepository<ContactChangeConfirmationEntity, String> {
    ContactChangeConfirmationEntity findByIdAndIsConfirmedFalse(String id);
}
