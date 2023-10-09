package com.example.mybookshopapp.services;

import com.example.mybookshopapp.data.GenreEntity;
import com.example.mybookshopapp.repositories.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GenreService {

    private final GenreRepository genreRepository;

    public List<GenreEntity> getAllRootGenres() {
        List<GenreEntity> genres = genreRepository.findGenreEntityByParentIdIsNull();
        return sortGenreLevel(genres);
    }

    private List<GenreEntity> sortGenreLevel(List<GenreEntity> genreChildren){

        for (GenreEntity genre : genreChildren){
            if (genre.getChildren().size() > 1){
                genre.setChildren(sortGenreLevel(genre.getChildren()));
            }
        }

        return genreChildren.stream().sorted(Comparator.comparing(genre -> -genre.getBookList().size())).toList();
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
