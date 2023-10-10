package com.example.mybookshopapp.dto;

import com.example.mybookshopapp.data.TagEntity;
import lombok.Data;

@Data
public class TagDto {
    private TagEntity tag;
    private String popularityClass;

    public TagDto(TagEntity tag, String popularityClass) {
        this.tag = tag;
        this.popularityClass = popularityClass;
    }
}
