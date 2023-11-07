package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.data.GenreEntity;
import com.example.mybookshopapp.data.TagEntity;
import com.example.mybookshopapp.dto.ApiResponse;
import com.example.mybookshopapp.dto.GenreDto;
import com.example.mybookshopapp.dto.TagDto;
import com.example.mybookshopapp.errors.ApiWrongParameterException;
import com.example.mybookshopapp.services.GenreService;
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
    private final GenreService genreService;

    @GetMapping("/tag/all")
    public ResponseEntity<List<TagEntity>> getAllTags() {
        return ResponseEntity.ok(tagService.getAllTags());
    }

    @GetMapping("/tag/{tagId}")
    public ResponseEntity<TagEntity> getTag(@PathVariable("tagId") Integer tagId) throws ApiWrongParameterException {
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
    public ResponseEntity<ApiResponse> deleteTag(@PathVariable("tagId") Integer tagId) throws ApiWrongParameterException {
        tagService.deleteTagById(tagId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ApiResponse(true));
    }

    @GetMapping("/genre/all")
    public ResponseEntity<List<GenreEntity>> getAllGenres() {
        return ResponseEntity.ok(genreService.getAllGenres());
    }

    @GetMapping("/genre/{genreId}")
    public ResponseEntity<GenreEntity> getGenre(@PathVariable("genreId") Integer genreId) throws ApiWrongParameterException {
        return ResponseEntity.ok(genreService.getGenreById(genreId));
    }

    @PostMapping("/genre")
    public ResponseEntity<GenreEntity> createGenre(@RequestBody GenreDto genreDto) throws ApiWrongParameterException {
        return ResponseEntity.ok(genreService.createGenre(genreDto));
    }

    @PutMapping("/genre/{genreId}")
    public ResponseEntity<GenreEntity> updateGenre(@PathVariable("genreId") Integer genreId,
                                                   @RequestBody GenreDto genreDto) throws ApiWrongParameterException {
        return ResponseEntity.ok(genreService.updateGenre(genreId, genreDto));
    }
    
    @DeleteMapping("/genre/{genreId}")
    public ResponseEntity<ApiResponse> deleteGenre(@PathVariable("genreId") Integer genreId) throws ApiWrongParameterException {
        genreService.deleteGenreById(genreId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ApiResponse(true));
    }
}
