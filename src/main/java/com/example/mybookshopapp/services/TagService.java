package com.example.mybookshopapp.services;

import com.example.mybookshopapp.data.TagEntity;
import com.example.mybookshopapp.dto.TagDto;
import com.example.mybookshopapp.dto.TagPopularityDto;
import com.example.mybookshopapp.errors.ApiWrongParameterException;
import com.example.mybookshopapp.repositories.TagRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
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

    public TagEntity getTagById(Integer id) throws ApiWrongParameterException {
        TagEntity tag = tagRepository.findTagEntityById(id);
        if (tag == null)
            throw new ApiWrongParameterException("Tag with id " + id + " does not exists");
        return tag;
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


    public TagEntity updateTag(Integer tagId, TagDto tagDto) throws ApiWrongParameterException {
        TagEntity tag = getTagById(tagId);
        if (StringUtils.isNotBlank(tagDto.getName()))
            tag.setName(tagDto.getName());
        if (StringUtils.isNotBlank(tagDto.getSlug()))
            tag.setSlug(tagDto.getSlug());
        try {
            tagRepository.save(tag);
        } catch (Exception e) {
            throw new ApiWrongParameterException("Can not update tag: " + e.getMessage());
        }
        return tag;
    }

    public void deleteTagById(Integer id) throws ApiWrongParameterException {
        TagEntity tag = getTagById(id);
        tagRepository.delete(tag);
    }

    public List<TagPopularityDto> getTagsWithPopularityClasses() {
        List<TagEntity> tags = getAllTags();
        Integer maxBooksCount = tagRepository.getMaxBooksCountByTag();
        if (maxBooksCount == null) maxBooksCount = 0;
        Integer minBooksCount = tagRepository.getMinBooksCountByTag();
        if (minBooksCount == null) minBooksCount = 0;
        List<TagPopularityDto> tagsWithPopularity = new ArrayList<>();
        for (TagEntity tag : tags) {
            Integer booksCount = tagRepository.getBooksCountByTagId(tag.getId());
            int tagRange = Math.max(0, (int) ((double) (booksCount - minBooksCount) / (maxBooksCount - minBooksCount + 1) * tagClasses.length));
            tagsWithPopularity.add(new TagPopularityDto(tag, tagClasses[tagRange]));
        }
        return tagsWithPopularity;
    }

    public List<TagEntity> getTagsByIds(Integer[] tagIds) {
        return tagRepository.findTagEntitiesByIdIn(List.of(tagIds));
    }
}
