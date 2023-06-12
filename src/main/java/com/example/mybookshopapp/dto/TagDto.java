package com.example.mybookshopapp.dto;

import com.example.mybookshopapp.data.TagEntity;
import lombok.Data;

@Data
public class TagDto {

    private TagEntity tag;

    private String popularity;

    public TagDto(TagEntity tag, String popularity) {
        this.tag = tag;
        this.popularity = popularity;
    }
}
