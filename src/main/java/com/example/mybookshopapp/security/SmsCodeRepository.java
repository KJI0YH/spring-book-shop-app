package com.example.mybookshopapp.security;

import com.example.mybookshopapp.data.SmsCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SmsCodeRepository extends JpaRepository<SmsCodeEntity, Long> {
    public SmsCodeEntity findByCode(String code);
}
