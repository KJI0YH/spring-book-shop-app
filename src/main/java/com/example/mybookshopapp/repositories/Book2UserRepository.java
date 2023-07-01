package com.example.mybookshopapp.repositories;

import com.example.mybookshopapp.data.Book2UserEntity;
import com.example.mybookshopapp.data.Book2UserIdEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Book2UserRepository extends JpaRepository<Book2UserEntity, Book2UserIdEntity> {
    Book2UserEntity findBook2UserEntityById(Book2UserIdEntity id);
}
