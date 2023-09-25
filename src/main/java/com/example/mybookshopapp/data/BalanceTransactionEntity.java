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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
    private LocalDateTime time;
    private Integer value;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private BookEntity book;
    private String description;

    public String getTimeValue(){
        return String.valueOf(String.format("%02d", time.getDayOfMonth())) + '.' +
                String.format("%02d", time.getMonthValue()) + '.' +
                time.getYear() + ' ' +
                String.format("%02d", time.getHour()) + ':' +
                String.format("%02d", time.getMinute()) + ':' +
                String.format("%02d", time.getSecond());
    }


}

