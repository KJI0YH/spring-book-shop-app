package com.example.mybookshopapp.data;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "book2user")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book2UserEntity {

    @EmbeddedId
    @JsonIgnore
    private Book2UserIdEntity id;
    private LocalDateTime time;

    @ManyToOne
    @JoinColumn(name = "book_id", updatable = false, insertable = false)
    @JsonIgnore
    private BookEntity book;

    @ManyToOne
    @JoinColumn(name = "user_id", updatable = false, insertable = false)
    @JsonIgnore
    private UserEntity user;
    @ManyToOne
    @JoinColumn(name = "type_id")
    @JsonIgnore
    private Book2UserTypeEntity type;

    @JsonGetter("type")
    public String getBook2UserType() {
        return type.getName();
    }

    @JsonGetter("bookId")
    public Integer getBookId() {
        return book.getId();
    }

    @JsonGetter("userId")
    public Integer getUserId() {
        return user.getId();
    }
}
