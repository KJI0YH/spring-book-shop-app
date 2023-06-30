package com.example.mybookshopapp.errors;

public class ApiWrongParameterException extends Exception{
    public ApiWrongParameterException(String message) {
        super(message);
    }
}
