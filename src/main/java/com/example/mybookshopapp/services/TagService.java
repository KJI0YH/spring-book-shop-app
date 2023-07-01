package com.example.mybookshopapp.services;

import com.example.mybookshopapp.data.TagEntity;
import com.example.mybookshopapp.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {

    private final TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public List<TagEntity> getAllTags() {
        return tagRepository.findAll();
    }

    public TagEntity getTagBySlug(String slug) {
        return tagRepository.findTagEntityBySlug(slug);
    }

    public Integer getMaxPopularityTagCount() {
        return tagRepository.findMaxTagCount();
    }

    public Integer getMinPopularityTagCount() {
        return tagRepository.findMinTagCount();
    }

}
