package com.example.mybookshopapp.dto;

import lombok.Data;

import java.util.List;

@Data
public class BookStatusDto {
    private String[] booksIds;
    private String status;
}
