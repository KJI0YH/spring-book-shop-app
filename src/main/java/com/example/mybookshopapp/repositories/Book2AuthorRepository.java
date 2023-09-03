package com.example.mybookshopapp.repositories;

import com.example.mybookshopapp.data.Book2AuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Book2AuthorRepository extends JpaRepository<Book2AuthorEntity, Integer> {
}
