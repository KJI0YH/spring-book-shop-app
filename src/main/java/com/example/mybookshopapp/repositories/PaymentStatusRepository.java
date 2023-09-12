package com.example.mybookshopapp.repositories;

import com.example.mybookshopapp.data.PaymentStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentStatusRepository extends JpaRepository<PaymentStatusEntity, String> {
    List<PaymentStatusEntity> findAllByIsProcessedIsFalse();
}
