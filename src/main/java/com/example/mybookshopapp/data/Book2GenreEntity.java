package com.example.mybookshopapp.data;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "book2genre")
@Data
public class Book2GenreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    @JsonIgnore
    private BookEntity book;

    @ManyToOne
    @JoinColumn(name = "genre_id")
    @JsonIgnore
    private GenreEntity genre;

    @JsonGetter("bookId")
    public Integer bookId() {
        return book.getId();
    }

    @JsonGetter("tagId")
    public Integer tagId() {
        return genre.getId();
    }
}
