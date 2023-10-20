package com.example.mybookshopapp.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;

@Entity
@Table(name = "approve_contact")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApproveContactEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String code;
    private LocalDateTime expiredTime;
    private LocalDateTime resendTime;
    private String contact;
    private Integer attempts;
    private Boolean approved;

    public boolean isCodeExpired() {
        return LocalDateTime.now().isAfter(expiredTime);
    }

    public boolean isAttemptsExpired(Integer maxAttempts) {
        return attempts >= maxAttempts;
    }

    public boolean isCanResend() {
        return LocalDateTime.now().isAfter(resendTime);
    }
}
