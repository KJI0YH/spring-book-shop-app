package com.example.mybookshopapp.dto;

import lombok.Data;

@Data
public class GenreDto {
    private Integer parentId;
    private String name;
    private String slug;
}
