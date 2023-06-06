package com.example.mybookshopapp.data;

import com.example.mybookshopapp.dto.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Integer> {
}
