package com.example.mybookshopapp.errors;

import lombok.Data;

@Data
public class BalanceNotEnoughException extends Exception {
    public BalanceNotEnoughException(String message) {
        super(message);
    }
}
