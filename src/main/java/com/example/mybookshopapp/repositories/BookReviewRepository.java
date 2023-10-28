package com.example.mybookshopapp.repositories;

import com.example.mybookshopapp.data.BookReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookReviewRepository extends JpaRepository<BookReviewEntity, Integer> {
    Optional<BookReviewEntity> findById(Integer id);
}
