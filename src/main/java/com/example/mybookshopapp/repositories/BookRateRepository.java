package com.example.mybookshopapp.repositories;

import com.example.mybookshopapp.data.BookRateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRateRepository extends JpaRepository<BookRateEntity, Integer> {
    BookRateEntity findBookRateEntityByBookId(Integer bookId);
}
