package com.example.mybookshopapp.dto;

import lombok.Data;

@Data
public class AuthorDto {
    private String photo;
    private String slug;
    private String firstName;
    private String lastName;
    private String description;
}
