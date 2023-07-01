package com.example.mybookshopapp.repositories;

import com.example.mybookshopapp.data.GenreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GenreRepository extends JpaRepository<GenreEntity, Integer> {
    List<GenreEntity> findGenreEntityByParentIdIsNull();

    GenreEntity findGenreEntityBySlug(String slug);
}
