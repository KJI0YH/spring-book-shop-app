package com.example.mybookshopapp.errors;

public class PaymentRequiredException extends Exception {
    public PaymentRequiredException(String message) {
        super(message);
    }
}
