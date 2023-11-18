package com.example.mybookshopapp.repositories;

import com.example.mybookshopapp.data.BookFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookFileRepository extends JpaRepository<BookFileEntity, Integer> {
    BookFileEntity findBookFileEntitiesByHash(String hash);
    List<BookFileEntity> findBookFileEntitiesByBookId(Integer bookId);
}
