package com.example.mybookshopapp.repositories;

import com.example.mybookshopapp.data.BalanceTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BalanceTransactionRepository extends JpaRepository<BalanceTransactionEntity, Integer> {
}
