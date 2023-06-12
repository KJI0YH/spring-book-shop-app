package com.example.mybookshopapp.repositories;

import com.example.mybookshopapp.data.BookFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookFileRepository extends JpaRepository<BookFileEntity, Integer> {
    public BookFileEntity findBookFileEntitiesByHash(String hash);
}
