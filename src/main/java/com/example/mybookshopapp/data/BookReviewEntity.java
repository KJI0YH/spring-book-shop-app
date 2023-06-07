package com.example.mybookshopapp.data;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "book_review")
@Data
public class BookReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int bookId;

    private int userId;

    private LocalDateTime time;

    private String text;
}
