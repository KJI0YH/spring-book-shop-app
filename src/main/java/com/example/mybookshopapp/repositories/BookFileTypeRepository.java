package com.example.mybookshopapp.repositories;

import com.example.mybookshopapp.data.BookFileType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookFileTypeRepository extends JpaRepository<BookFileType, Integer> {
    BookFileType findBookFileTypeByNameIgnoreCase(String name);
}
