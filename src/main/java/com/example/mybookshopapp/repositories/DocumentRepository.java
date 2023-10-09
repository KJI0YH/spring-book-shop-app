package com.example.mybookshopapp.repositories;

import com.example.mybookshopapp.data.DocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<DocumentEntity, Integer> {
    List<DocumentEntity> findAllByOrderBySortIndex();
    DocumentEntity findBySlug(String slug);
}
