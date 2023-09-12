package com.example.mybookshopapp.errors;

public class PaymentDoesNotExistsException extends Exception{
    public PaymentDoesNotExistsException(){
        super();
    }
    public PaymentDoesNotExistsException(String message) {
        super(message);
    }
}
