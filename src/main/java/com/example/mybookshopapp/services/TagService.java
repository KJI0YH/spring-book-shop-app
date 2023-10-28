package com.example.mybookshopapp.services;

import com.example.mybookshopapp.data.TagEntity;
import com.example.mybookshopapp.dto.TagDto;
import com.example.mybookshopapp.repositories.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TagService {

    private final TagRepository tagRepository;
    private final String[] tagClasses = new String[]{
            "Tag_xs",
            "Tag_sm",
            "",
            "Tag_md",
            "Tag_lg",
    };

    public List<TagEntity> getAllTags() {
        return tagRepository.findAll();
    }

    public TagEntity getTagBySlug(String slug) {
        return tagRepository.findTagEntityBySlug(slug);
    }

    public List<TagDto> getTagsWithPopularityClasses() {
        List<TagEntity> tags = getAllTags();
        Integer maxBooksCount = tagRepository.getMaxBooksCountByTag();
        Integer minBooksCount = tagRepository.getMinBooksCountByTag();
        List<TagDto> tagsWithPopularity = new ArrayList<>();
        for (TagEntity tag : tags) {
            Integer booksCount = tagRepository.getBooksCountByTagId(tag.getId());
            int tagRange = (int)((double) (booksCount - minBooksCount) / (maxBooksCount - minBooksCount + 1) * tagClasses.length);
            tagsWithPopularity.add(new TagDto(tag, tagClasses[tagRange]));
        }
        return tagsWithPopularity;
    }
}
