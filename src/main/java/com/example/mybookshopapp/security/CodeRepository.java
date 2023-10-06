package com.example.mybookshopapp.security;

import com.example.mybookshopapp.data.SmsCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeRepository extends JpaRepository<SmsCodeEntity, Long> {
    SmsCodeEntity findByCode(String code);
}
