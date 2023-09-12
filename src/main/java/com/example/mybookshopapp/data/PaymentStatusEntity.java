package com.example.mybookshopapp.data;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "payment_status")
@Data
public class PaymentStatusEntity {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
    private String paymentId;
    private String status;
    private Boolean isProcessed = false;
}
