package com.example.mybookshopapp.dto;

import lombok.Data;

import java.util.List;

@Data
public class BookCookieStoreDto {
    private List<String> cartContents;
    private List<String> postponedContents;

    public BookCookieStoreDto(List<String> cartContents, List<String> postponedContents) {
        this.cartContents = cartContents;
        this.postponedContents = postponedContents;
    }
}
