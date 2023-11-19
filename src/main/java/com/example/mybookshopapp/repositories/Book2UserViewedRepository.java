package com.example.mybookshopapp.repositories;

import com.example.mybookshopapp.data.Book2UserIdEntity;
import com.example.mybookshopapp.data.Book2UserViewedEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Book2UserViewedRepository extends JpaRepository<Book2UserViewedEntity, Book2UserIdEntity> {
    Page<Book2UserViewedEntity> findAllByUserIdOrderByTimeDesc(Integer userId, Pageable pageable);

    Book2UserViewedEntity findBook2UserViewedEntityById(Book2UserIdEntity id);
}
