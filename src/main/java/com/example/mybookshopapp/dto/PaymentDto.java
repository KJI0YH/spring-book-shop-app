package com.example.mybookshopapp.dto;

import lombok.Data;

@Data
public class PaymentDto {
    private Long time;
    private String sum;
    private String hash;
}
