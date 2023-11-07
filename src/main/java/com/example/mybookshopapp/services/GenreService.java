package com.example.mybookshopapp.services;

import com.example.mybookshopapp.data.GenreEntity;
import com.example.mybookshopapp.dto.GenreDto;
import com.example.mybookshopapp.errors.ApiWrongParameterException;
import com.example.mybookshopapp.repositories.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
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

    private List<GenreEntity> sortGenreLevel(List<GenreEntity> genreChildren) {

        for (GenreEntity genre : genreChildren) {
            if (genre.getChildren().size() > 1) {
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

    public List<GenreEntity> getAllGenres() {
        return genreRepository.findAll();
    }

    public GenreEntity getGenreById(Integer genreId) throws ApiWrongParameterException {
        GenreEntity genre = genreRepository.findGenreEntityById(genreId);
        if (genre == null)
            throw new ApiWrongParameterException("Genre with id " + genreId + " does not exists");
        return genre;
    }

    public GenreEntity createGenre(GenreDto genreDto) throws ApiWrongParameterException {
        if (!StringUtils.isNotBlank(genreDto.getName()) || !StringUtils.isNotBlank(genreDto.getSlug()))
            throw new ApiWrongParameterException("Invalid genre parameters");
        GenreEntity parent = null;
        if (genreDto.getParentId() != null) {
            parent = getGenreById(genreDto.getParentId());
            if (parent == null)
                throw new ApiWrongParameterException("Parent genre with id " + genreDto.getParentId() + " does not exists");
        }

        GenreEntity genre = new GenreEntity();
        genre.setName(genreDto.getName());
        genre.setSlug(genreDto.getSlug());
        genre.setParent(parent);
        genre.setParentId(parent == null ? null : parent.getId());
        GenreEntity newGenre;
        try {
            newGenre = genreRepository.save(genre);
        } catch (Exception e) {
            throw new ApiWrongParameterException("Can not save genre: " + e.getMessage());
        }

        return newGenre;
    }

    public GenreEntity updateGenre(Integer genreId, GenreDto genreDto) throws ApiWrongParameterException {
        GenreEntity genre = getGenreById(genreId);

        if (StringUtils.isNotBlank(genreDto.getName()))
            genre.setName(genreDto.getName());
        if (StringUtils.isNotBlank(genreDto.getSlug()))
            genre.setSlug(genreDto.getSlug());
        GenreEntity parent = null;
        if (genreDto.getParentId() != null) {
            parent = getGenreById(genreDto.getParentId());
        }
        genre.setParent(parent);
        genre.setParentId(parent == null ? null : parent.getId());
        try {
            genreRepository.save(genre);
        } catch (Exception e) {
            throw new ApiWrongParameterException("Can not update genre: " + e.getMessage());
        }
        return genre;
    }

    public void deleteGenreById(Integer genreId) throws ApiWrongParameterException {
        GenreEntity genre = getGenreById(genreId);
        genreRepository.delete(genre);
    }
}
