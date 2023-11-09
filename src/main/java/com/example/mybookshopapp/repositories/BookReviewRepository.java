package com.example.mybookshopapp.repositories;

import com.example.mybookshopapp.data.BookReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookReviewRepository extends JpaRepository<BookReviewEntity, Integer> {
    BookReviewEntity findBookReviewEntitiesById(Integer id);

    List<BookReviewEntity> findBookReviewEntitiesByBookId(Integer bookId);
}
