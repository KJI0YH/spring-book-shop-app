package com.example.mybookshopapp.services;

import com.example.mybookshopapp.data.BookReviewLikeEntity;
import com.example.mybookshopapp.data.BookReviewLikeIdEntity;
import com.example.mybookshopapp.repositories.BookReviewRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BookReviewRateService {
    private final BookReviewRateRepository bookReviewRateRepository;

    @Autowired
    public BookReviewRateService(BookReviewRateRepository bookReviewRateRepository) {
        this.bookReviewRateRepository = bookReviewRateRepository;
    }

    public void rateReview(Integer reviewId, Integer userId, Integer value){
        BookReviewLikeIdEntity reviewLikeId = new BookReviewLikeIdEntity();
        reviewLikeId.setReviewId(reviewId);
        reviewLikeId.setUserId(userId);
        BookReviewLikeEntity reviewLike = new BookReviewLikeEntity();
        reviewLike.setId(reviewLikeId);
        reviewLike.setTime(LocalDateTime.now());
        reviewLike.setValue(value);
        bookReviewRateRepository.save(reviewLike);
    }
}
