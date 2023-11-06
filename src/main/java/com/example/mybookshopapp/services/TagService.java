package com.example.mybookshopapp.services;

import com.example.mybookshopapp.data.TagEntity;
import com.example.mybookshopapp.dto.TagDto;
import com.example.mybookshopapp.dto.TagPopularityDto;
import com.example.mybookshopapp.errors.ApiWrongParameterException;
import com.example.mybookshopapp.repositories.TagRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.postgresql.util.PSQLException;
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

    public TagEntity getTagById(Integer id) {
        return tagRepository.findTagEntityById(id);
    }

    public TagEntity createTag(TagDto tagDto) throws ApiWrongParameterException {
        if (!StringUtils.isNotBlank(tagDto.getName()) || !StringUtils.isNotBlank(tagDto.getSlug()))
            throw new ApiWrongParameterException("Invalid tag parameters");
        TagEntity tag = new TagEntity();
        tag.setName(tagDto.getName());
        tag.setSlug(tagDto.getSlug());
        TagEntity newTag;
        try {
            newTag = tagRepository.save(tag);
        } catch (Exception e) {
            throw new ApiWrongParameterException("Can not save tag: " + e.getMessage());
        }
            
        return newTag;
    }

    public void deleteTagById(Integer id) {
        TagEntity tag = getTagById(id);
        if (tag != null) {
            tagRepository.delete(tag);
        }
    }

    public TagEntity updateTag(Integer tagId, TagDto tagDto) throws ApiWrongParameterException {
        TagEntity tag = getTagById(tagId);
        if (tag == null)
            throw new ApiWrongParameterException("Tag with this id does not exists");

        if (StringUtils.isNotBlank(tagDto.getName()))
            tag.setName(tagDto.getName());
        if (StringUtils.isNotBlank(tagDto.getSlug()))
            tag.setSlug(tagDto.getSlug());
        try {
            tagRepository.save(tag);
        } catch (Exception e){
            throw new ApiWrongParameterException("Can not update tag: " + e.getMessage());
        }
        return tag;
    }

    public List<TagPopularityDto> getTagsWithPopularityClasses() {
        List<TagEntity> tags = getAllTags();
        Integer maxBooksCount = tagRepository.getMaxBooksCountByTag();
        Integer minBooksCount = tagRepository.getMinBooksCountByTag();
        List<TagPopularityDto> tagsWithPopularity = new ArrayList<>();
        for (TagEntity tag : tags) {
            Integer booksCount = tagRepository.getBooksCountByTagId(tag.getId());
            int tagRange = Math.max(0, (int) ((double) (booksCount - minBooksCount) / (maxBooksCount - minBooksCount + 1) * tagClasses.length));
            tagsWithPopularity.add(new TagPopularityDto(tag, tagClasses[tagRange]));
        }
        return tagsWithPopularity;
    }
}
