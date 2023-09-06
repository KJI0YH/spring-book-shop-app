package com.example.mybookshopapp.data;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "sms_code")
@Data
@NoArgsConstructor
public class SmsCodeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private LocalDateTime expiredTime;

    public SmsCodeEntity(String code, Integer expiredIn){
        this.code = code;
        this.expiredTime = LocalDateTime.now().plusSeconds(expiredIn);
    }

    public Boolean isExpired(){
        return LocalDateTime.now().isAfter(expiredTime);
    }
}
