package com.example.mybookshopapp.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookDto {
    private String pubDate;
    private Boolean isBestseller;
    private String slug;
    private String title;
    private String image;
    private String description;
    private Integer price;
    private Integer discount;    
}
