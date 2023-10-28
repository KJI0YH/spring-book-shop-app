package com.example.mybookshopapp.repositories;

import com.example.mybookshopapp.data.FileDownloadEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileDownloadRepository extends JpaRepository<FileDownloadEntity, Integer> {
    FileDownloadEntity findFileDownloadEntityByBookIdAndUserId(Integer bookId, Integer userId);
}
