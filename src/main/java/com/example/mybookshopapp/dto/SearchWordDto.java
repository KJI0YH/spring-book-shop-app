package com.example.mybookshopapp.dto;

import lombok.Data;

@Data
public class SearchWordDto {
    private String example;

    public SearchWordDto(String example){
        this.example = example;
    }

    public SearchWordDto(){

    }
}
