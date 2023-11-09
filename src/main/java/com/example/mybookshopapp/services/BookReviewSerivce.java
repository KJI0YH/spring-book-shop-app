package com.example.mybookshopapp.services;

import com.example.mybookshopapp.data.BookReviewEntity;
import com.example.mybookshopapp.dto.BookReviewDto;
import com.example.mybookshopapp.errors.ApiWrongParameterException;
import com.example.mybookshopapp.repositories.BookReviewRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BookReviewSerivce {
    private final BookReviewRepository bookReviewRepository;

    public List<BookReviewEntity> getAllReviews() {
        return bookReviewRepository.findAll();
    }

    public BookReviewEntity getReviewById(Integer reviewId) throws ApiWrongParameterException {
        BookReviewEntity review = bookReviewRepository.findBookReviewEntitiesById(reviewId);
        if (review == null)
            throw new ApiWrongParameterException("Review with id " + reviewId + " does not exists");
        return review;
    }

    public List<BookReviewEntity> getReviewsByBookId(Integer bookId) {
        return bookReviewRepository.findBookReviewEntitiesByBookId(bookId);
    }

    public BookReviewEntity updateBookReview(Integer reviewId, BookReviewDto bookReviewDto) throws ApiWrongParameterException {
        BookReviewEntity review = getReviewById(reviewId);
        if (StringUtils.isNotBlank(bookReviewDto.getText()))
            review.setText(bookReviewDto.getText());
        try {
            return bookReviewRepository.save(review);
        } catch (Exception e) {
            throw new ApiWrongParameterException("Can not update review: " + e.getMessage());
        }
    }

    public void deleteBookReview(Integer reviewId) throws ApiWrongParameterException {
        BookReviewEntity review = getReviewById(reviewId);
        bookReviewRepository.delete(review);
    }
}
