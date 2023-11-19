package com.example.mybookshopapp.dto;

import lombok.Data;

@Data
public class BookStatusDto {
    private String[] booksIds;
    private String status;
}
