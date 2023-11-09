package com.example.mybookshopapp.dto;

import lombok.Data;

@Data
public class Book2UserDto {
    private Integer userId;
    private Integer[] bookIds;
    private String status;
}
