package com.example.mybookshopapp.data;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "book2user")
@Data
public class Book2UserEntity {

    @EmbeddedId
    private Book2UserIdEntity id;
    private LocalDateTime time;

    @ManyToOne
    @JoinColumn(name = "book_id", updatable = false, insertable = false)
    private BookEntity book;

    @ManyToOne
    @JoinColumn(name = "user_id", updatable = false, insertable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private Book2UserTypeEntity type;
}
