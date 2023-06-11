package com.example.mybookshopapp.services;

import com.example.mybookshopapp.data.TagEntity;
import com.example.mybookshopapp.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TagService {

    private final TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public List<TagEntity> getAllTags(){
        return tagRepository.findAll();
    }

    public Optional<TagEntity> getTagById(Integer id){
        return tagRepository.findById(id);
    }

    public Integer getMaxPopularityTagCount(){
        return tagRepository.findMaxTagCount();
    }

    public Integer getMinPopularityTagCount(){
        return tagRepository.findMinTagCount();
    }

}
