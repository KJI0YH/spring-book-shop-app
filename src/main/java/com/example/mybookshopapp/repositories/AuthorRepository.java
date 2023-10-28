package com.example.mybookshopapp.repositories;

import com.example.mybookshopapp.data.AuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AuthorRepository extends JpaRepository<AuthorEntity, Integer> {
    AuthorEntity findAuthorEntityBySlug(String slug);

    @Query(value = "select id from author where lower(first_name) like lower(concat('%', ?1, '%')) or lower(last_name) like lower(concat('%', ?1, '%'))", nativeQuery = true)
    List<Integer> findAuthorEntitiesIdByFirstLastNameContainingIgnoreCase(String authorName);
}
