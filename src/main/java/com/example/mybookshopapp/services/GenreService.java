package com.example.mybookshopapp.services;

import com.example.mybookshopapp.data.GenreEntity;
import com.example.mybookshopapp.repositories.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GenreService {

    private final GenreRepository genreRepository;

    @Autowired
    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public List<GenreEntity> getAllRootGenres(){
        return genreRepository.findGenreEntityByParentIdIsNull();
    }

    public Optional<GenreEntity> getGenreById(Integer genreId){
        return genreRepository.findById(genreId);
    }

    public GenreEntity getGenreBySlug(String slug){
        return genreRepository.findGenreEntityBySlug(slug);
    }
}
