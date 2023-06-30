package com.example.mybookshopapp.data;

import io.swagger.models.auth.In;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "balance_transaction")
@Data
public class BalanceTransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer userId;
    private LocalDateTime time;
    private Integer value;
    private Integer bookId;
    private String description;
}

