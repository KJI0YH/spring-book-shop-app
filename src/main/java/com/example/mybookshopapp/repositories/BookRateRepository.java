package com.example.mybookshopapp.repositories;

import com.example.mybookshopapp.data.BookRateEntity;
import com.example.mybookshopapp.data.BookRateIdEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRateRepository extends JpaRepository<BookRateEntity, BookRateIdEntity> {
}
