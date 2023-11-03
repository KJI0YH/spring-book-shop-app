package com.example.mybookshopapp.errors;

public class RoleDoesNotExistsException extends Exception{
    public RoleDoesNotExistsException(String message){
        super(message);
    }
}
