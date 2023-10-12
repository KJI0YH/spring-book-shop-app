package com.example.mybookshopapp.errors;

public class PaymentDoesNotExistsException extends Exception{
    public PaymentDoesNotExistsException(String message) {
        super(message);
    }
}
