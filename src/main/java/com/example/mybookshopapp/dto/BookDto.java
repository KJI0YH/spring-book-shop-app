package com.example.mybookshopapp.dto;

import lombok.Data;

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
    private Integer[] tagIds;
    private Integer[] genreIds;
    private AuthorSortIndexDto[] authorIds;
}
