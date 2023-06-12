package com.example.mybookshopapp.repositories;

import com.example.mybookshopapp.data.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TagRepository extends JpaRepository<TagEntity, Integer> {

    @Query(value = "SELECT COUNT(*) AS tag_count FROM tag t JOIN book2tag b2t ON t.id = b2t.tag_id GROUP BY t.id ORDER BY tag_count DESC LIMIT 1", nativeQuery = true)
    public Integer findMaxTagCount();

    @Query(value = "SELECT COUNT(*) AS tag_count FROM tag t JOIN book2tag b2t ON t.id = b2t.tag_id GROUP BY t.id ORDER BY tag_count ASC LIMIT 1", nativeQuery = true)
    public Integer findMinTagCount();

    TagEntity findTagEntityBySlug(String slug);
}
