package com.example.mybookshopapp.data;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "book_review_like")
@Data
public class BookReviewLikeEntity {

    @EmbeddedId
    private BookReviewLikeIdEntity id;
    private LocalDateTime time;
    private Integer value;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "review_id", referencedColumnName = "id", updatable = false, insertable = false)
    private BookReviewEntity review;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "user_id", referencedColumnName = "id", updatable = false, insertable = false)
    private UserEntity user;
}
