package com.example.mybookshopapp.repositories;

import com.example.mybookshopapp.data.AuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<AuthorEntity, Integer> {
    AuthorEntity findAuthorEntityBySlug(String slug);
}
