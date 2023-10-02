package com.example.mybookshopapp.services;

import com.example.mybookshopapp.data.BookRateEntity;
import com.example.mybookshopapp.data.BookRateIdEntity;
import com.example.mybookshopapp.repositories.BookRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BookRateService {

    private final BookRateRepository bookRateRepository;

    public void rateBook(Integer bookId, Integer userId, Integer rate) {
        BookRateIdEntity bookRateId = new BookRateIdEntity();
        bookRateId.setBookId(bookId);
        bookRateId.setUserId(userId);
        BookRateEntity bookRate = new BookRateEntity();
        bookRate.setId(bookRateId);
        bookRate.setRate(rate);
        bookRateRepository.save(bookRate);
    }
}
