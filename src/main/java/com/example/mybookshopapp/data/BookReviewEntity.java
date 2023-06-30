package com.example.mybookshopapp.data;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Entity
@Table(name = "book_review")
@Data
public class BookReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalDateTime time;
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private BookEntity book;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private UserEntity user;

    @OneToMany(mappedBy = "review")
    @ToString.Exclude
    private List<BookReviewLikeEntity> reviewLikeList;

    public Long getLikesCount(){
        return reviewLikeList.stream()
                .filter(l -> l.getValue() == 1)
                .count();
    }

    public Long getDislikesCount(){
        return reviewLikeList.stream()
                .filter(d -> d.getValue() == -1)
                .count();
    }

    public Long getPopularityValue(){
        return getLikesCount() - getDislikesCount();
    }
}
