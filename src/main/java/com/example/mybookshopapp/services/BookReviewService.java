package com.example.mybookshopapp.services;

import com.example.mybookshopapp.data.BookReviewEntity;
import com.example.mybookshopapp.repositories.BookReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BookReviewService {

    private final BookReviewRepository bookReviewRepository;

    @Autowired
    public BookReviewService(BookReviewRepository bookReviewRepository) {
        this.bookReviewRepository = bookReviewRepository;
    }

    public void reviewBook(Integer bookId, Integer userId, String text){
        BookReviewEntity bookReview = new BookReviewEntity();
        bookReview.setBookId(bookId);
        bookReview.setUserId(userId);
        bookReview.setText(text);
        bookReview.setTime(LocalDateTime.now());
        bookReviewRepository.save(bookReview);
    }
}
