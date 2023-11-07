package com.example.mybookshopapp.repositories;

import com.example.mybookshopapp.data.GenreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GenreRepository extends JpaRepository<GenreEntity, Integer> {
    List<GenreEntity> findGenreEntityByParentIdIsNull();

    GenreEntity findGenreEntityBySlug(String slug);

    @Query(value = "select id from genre where lower(name) like lower(concat('%', ?1, '%'))", nativeQuery = true)
    List<Integer> findGenreEntitiesIdByNameContainingIgnoreCase(String genreName);
    
    GenreEntity findGenreEntityById(Integer id);
}
