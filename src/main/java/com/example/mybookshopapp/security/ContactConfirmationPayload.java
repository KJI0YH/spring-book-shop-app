package com.example.mybookshopapp.security;

import lombok.Data;

@Data
public class ContactConfirmationPayload {

    private String contact;
    private String code;
}
