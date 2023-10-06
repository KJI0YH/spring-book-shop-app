package com.example.mybookshopapp.services;

import com.example.mybookshopapp.data.TagEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TagsPopularityService {

    private final TagService tagService;

    public String getPopularityTag(TagEntity tagEntity) {
        Integer max = tagService.getMaxPopularityTagCount();
        Integer min = tagService.getMinPopularityTagCount();
        int rate = Math.toIntExact(Math.round((double) tagEntity.getBook2tagList().size() / (max - min + 1) * 100.0));
        if (rate > 80) {
            return "Tag_lg";
        } else if (rate > 60) {
            return "Tag_md";
        } else if (rate > 40) {
            return "";
        } else if (rate > 20) {
            return "Tag_sm";
        }
        return "Tag_xs";
    }
}
