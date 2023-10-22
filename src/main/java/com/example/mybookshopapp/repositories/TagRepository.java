package com.example.mybookshopapp.repositories;

import com.example.mybookshopapp.data.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagRepository extends JpaRepository<TagEntity, Integer> {

    @Query(value = "SELECT MAX(books_count) FROM (SELECT COUNT(*) AS books_count FROM book2tag b2t GROUP BY b2t.tag_id) as subquery", nativeQuery = true)
    Integer getMaxBooksCountByTag();

    @Query(value = "SELECT MIN(books_count) FROM (SELECT COUNT(*) AS books_count FROM book2tag b2t GROUP BY b2t.tag_id) as subquery", nativeQuery = true)
    Integer getMinBooksCountByTag();

    TagEntity findTagEntityBySlug(String slug);

    @Query(value = "SELECT COUNT(*) FROM book2tag b2t WHERE b2t.tag_id = ?1 GROUP BY b2t.tag_id", nativeQuery = true)
    Integer getBooksCountByTagId(Integer id);

    @Query(value = "select id from tag where lower(name) like lower(concat('%', ?1, '%'))", nativeQuery = true)
    List<Integer> findTagEntitiesIdByNameContainingIgnoreCase(String tagName);
}
