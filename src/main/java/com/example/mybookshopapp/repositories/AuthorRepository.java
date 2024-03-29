package com.example.mybookshopapp.repositories;

import com.example.mybookshopapp.data.AuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<AuthorEntity, Integer> {
    AuthorEntity findAuthorEntityBySlug(String slug);

    @Query(value = "select id from author where lower(first_name) like lower(concat('%', ?1, '%')) or lower(last_name) like lower(concat('%', ?1, '%'))", nativeQuery = true)
    List<Integer> findAuthorEntitiesIdByFirstLastNameContainingIgnoreCase(String authorName);

    AuthorEntity findAuthorEntityById(Integer id);

    List<AuthorEntity> findAuthorEntitiesByIdIn(Collection<Integer> id);

}
