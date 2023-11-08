package com.example.mybookshopapp.dto;

import lombok.Data;

@Data
public class Book2TagDto {
    private Integer[] bookIds;
    private Integer[] tagIds;
}
