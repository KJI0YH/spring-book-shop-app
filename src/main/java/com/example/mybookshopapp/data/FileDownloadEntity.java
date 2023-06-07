package com.example.mybookshopapp.data;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "file_download")
@Data
public class FileDownloadEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int userId;

    private int bookId;

    private int count;
}
