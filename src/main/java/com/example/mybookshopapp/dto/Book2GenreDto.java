package com.example.mybookshopapp.dto;

import lombok.Data;

@Data
public class Book2GenreDto {
    private Integer[] bookIds;
    private Integer[] genreIds;
}
