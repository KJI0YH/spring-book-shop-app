package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.data.TagEntity;
import com.example.mybookshopapp.dto.ApiResponse;
import com.example.mybookshopapp.dto.TagDto;
import com.example.mybookshopapp.errors.ApiWrongParameterException;
import com.example.mybookshopapp.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/cms")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CmsController {
    private final TagService tagService;
    
    @GetMapping("/tag/all")
    public ResponseEntity<List<TagEntity>> getAllTags(){
        return ResponseEntity.ok(tagService.getAllTags());
    }
    
    @GetMapping("/tag/{tagId}")
    public ResponseEntity<TagEntity> getTag(@PathVariable("tagId") Integer tagId){
        return ResponseEntity.ok(tagService.getTagById(tagId));
    }
    
    @PostMapping("/tag")
    public ResponseEntity<TagEntity> createTag(@RequestBody TagDto tagDto) throws ApiWrongParameterException {
        return ResponseEntity.ok(tagService.createTag(tagDto));
    }

    @PutMapping("/tag/{tagId}")
    public ResponseEntity<TagEntity> updateTag(@PathVariable("tagId") Integer tagId,
                                                 @RequestBody TagDto tagDto) throws ApiWrongParameterException {
        return ResponseEntity.ok(tagService.updateTag(tagId, tagDto));
    }
    
    @DeleteMapping("/tag/{tagId}")
    public ResponseEntity<ApiResponse> deleteTag(@PathVariable("tagId") Integer tagId){
        tagService.deleteTagById(tagId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ApiResponse(true));
    }
}
