package com.example.mybookshopapp.errors;

public class UserUnauthorizedException extends Exception{
    public UserUnauthorizedException(String message){
        super(message);
    }
}
