package com.example.mybookshopapp.errors;

import lombok.Data;

@Data
public class BalanceNotEnoughException extends Exception {
    private final Integer fundsLackInCents;

    public BalanceNotEnoughException(String message, Integer fundsLackInCents) {
        super(message);
        this.fundsLackInCents = fundsLackInCents;
    }
}
