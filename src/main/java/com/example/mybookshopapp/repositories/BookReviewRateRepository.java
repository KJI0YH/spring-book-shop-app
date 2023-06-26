package com.example.mybookshopapp.repositories;

import com.example.mybookshopapp.data.BookReviewLikeEntity;
import com.example.mybookshopapp.data.BookReviewLikeIdEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookReviewRateRepository extends JpaRepository<BookReviewLikeEntity, BookReviewLikeIdEntity> {
}
