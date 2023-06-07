package com.example.mybookshopapp.repositories;

import com.example.mybookshopapp.data.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<BookEntity, Integer> {
}
