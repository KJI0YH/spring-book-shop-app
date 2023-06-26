package com.example.mybookshopapp.services;

import com.example.mybookshopapp.data.BookReviewEntity;
import com.example.mybookshopapp.repositories.BookRepository;
import com.example.mybookshopapp.repositories.BookReviewRepository;
import com.example.mybookshopapp.security.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BookReviewService {

    private final BookReviewRepository bookReviewRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    @Autowired
    public BookReviewService(BookReviewRepository bookReviewRepository, UserRepository userRepository, BookRepository bookRepository) {
        this.bookReviewRepository = bookReviewRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    public void reviewBook(Integer bookId, Integer userId, String text){
        BookReviewEntity bookReview = new BookReviewEntity();
        bookReview.setBook(bookRepository.findBookEntityById(bookId));
        bookReview.setUser(userRepository.findUserEntityById(userId));
        bookReview.setText(text);
        bookReview.setTime(LocalDateTime.now());
        bookReviewRepository.save(bookReview);
    }
}
