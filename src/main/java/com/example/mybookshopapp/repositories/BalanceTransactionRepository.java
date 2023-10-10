package com.example.mybookshopapp.repositories;

import com.example.mybookshopapp.data.BalanceTransactionEntity;
import com.example.mybookshopapp.data.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BalanceTransactionRepository extends JpaRepository<BalanceTransactionEntity, Integer> {
    Page<BalanceTransactionEntity> findAllByUserOrderByTimeAsc(UserEntity user, Pageable pageable);
    Page<BalanceTransactionEntity> findAllByUserOrderByTimeDesc(UserEntity user, Pageable pageable);

    BalanceTransactionEntity findByBookIdAndUserId(Integer book_id, Integer user_id);
}
