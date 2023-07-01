package com.example.mybookshopapp.repositories;

import com.example.mybookshopapp.data.BookRateEntity;
import com.example.mybookshopapp.data.BookRateIdEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRateRepository extends JpaRepository<BookRateEntity, BookRateIdEntity> {
}
