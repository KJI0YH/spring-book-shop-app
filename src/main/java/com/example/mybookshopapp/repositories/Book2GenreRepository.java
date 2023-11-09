package com.example.mybookshopapp.repositories;

import com.example.mybookshopapp.data.Book2GenreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Book2GenreRepository extends JpaRepository<Book2GenreEntity, Integer> {
    Book2GenreEntity findBook2GenreEntityByBookIdAndGenreId(Integer bookId, Integer genreId);
}
