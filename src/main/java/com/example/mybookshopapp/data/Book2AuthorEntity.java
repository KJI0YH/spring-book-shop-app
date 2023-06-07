package com.example.mybookshopapp.data;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "book2author")
@Data
public class Book2AuthorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int sortIndex;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private BookEntity book;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private AuthorEntity author;
}
