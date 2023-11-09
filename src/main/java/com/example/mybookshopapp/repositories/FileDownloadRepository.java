package com.example.mybookshopapp.repositories;

import com.example.mybookshopapp.data.FileDownloadEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileDownloadRepository extends JpaRepository<FileDownloadEntity, Integer> {
    FileDownloadEntity findFileDownloadEntityByBookIdAndUserId(Integer bookId, Integer userId);
}
