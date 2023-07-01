package com.example.mybookshopapp.services;

import com.example.mybookshopapp.data.GenreEntity;
import com.example.mybookshopapp.repositories.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class GenreService {

    private final GenreRepository genreRepository;

    @Autowired
    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public List<GenreEntity> getAllRootGenres() {
        return genreRepository.findGenreEntityByParentIdIsNull();
    }

    public GenreEntity getGenreBySlug(String slug) {
        return genreRepository.findGenreEntityBySlug(slug);
    }

    public List<GenreEntity> getGenresBreadcrumbs(String slug) {
        GenreEntity genre = genreRepository.findGenreEntityBySlug(slug);
        List<GenreEntity> breadcrumbs = new ArrayList<>();

        while (genre != null && genre.getParent() != null) {
            breadcrumbs.add(genre.getParent());
            genre = genre.getParent();
        }
        Collections.reverse(breadcrumbs);
        return breadcrumbs;
    }
}
