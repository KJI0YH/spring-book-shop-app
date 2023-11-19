package com.example.mybookshopapp.repositories;

import com.example.mybookshopapp.data.PaymentStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentStatusRepository extends JpaRepository<PaymentStatusEntity, String> {
    List<PaymentStatusEntity> findAllByIsProcessedIsFalseAndStatus(String status);

    PaymentStatusEntity findPaymentStatusEntityById(String id);
}
