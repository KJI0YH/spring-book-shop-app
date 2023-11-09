package com.example.mybookshopapp.dto;

import lombok.Data;

@Data
public class Book2AuthorDto {
    private Integer[] bookIds;
    private AuthorSortIndexDto[] authors;
}
