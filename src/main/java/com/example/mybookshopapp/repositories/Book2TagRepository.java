package com.example.mybookshopapp.repositories;

import com.example.mybookshopapp.data.Book2TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Book2TagRepository extends JpaRepository<Book2TagEntity, Integer> {
    Book2TagEntity findBook2TagEntityByBookIdAndTagId(Integer bookId, Integer tagId);
}
