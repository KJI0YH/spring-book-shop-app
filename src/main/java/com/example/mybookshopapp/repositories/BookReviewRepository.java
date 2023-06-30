package com.example.mybookshopapp.repositories;

import com.example.mybookshopapp.data.BookReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookReviewRepository extends JpaRepository<BookReviewEntity, Integer> {
}
