package com.example.mybookshopapp.services;

import com.example.mybookshopapp.data.BookRateEntity;
import com.example.mybookshopapp.repositories.BookRateRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookRateService {

    private final BookRateRepository bookRateRepository;

    @Autowired
    public BookRateService(BookRateRepository bookRateRepository) {
        this.bookRateRepository = bookRateRepository;
    }

    @Transactional
    public void rateBook(Integer bookId, Integer value){
        BookRateEntity bookRateEntity = bookRateRepository.findBookRateEntityByBookId(bookId);

        switch (value) {
            case 1 -> bookRateEntity.setValue1(bookRateEntity.getValue1() + 1);
            case 2 -> bookRateEntity.setValue2(bookRateEntity.getValue2() + 1);
            case 3 -> bookRateEntity.setValue3(bookRateEntity.getValue3() + 1);
            case 4 -> bookRateEntity.setValue4(bookRateEntity.getValue4() + 1);
            case 5 -> bookRateEntity.setValue5(bookRateEntity.getValue5() + 1);
            default -> {
            }
        }
    }
}
