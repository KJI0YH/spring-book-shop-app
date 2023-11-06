package com.example.mybookshopapp.dto;

import com.example.mybookshopapp.data.TagEntity;
import lombok.Data;

@Data
public class TagPopularityDto {
    private TagEntity tag;
    private String popularityClass;

    public TagPopularityDto(TagEntity tag, String popularityClass) {
        this.tag = tag;
        this.popularityClass = popularityClass;
    }
}
