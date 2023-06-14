package com.example.mybookshopapp.data;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Entity
@Table(name = "book_review")
@Data
public class BookReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private LocalDateTime time;

    public String getTimeString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        return time.format(formatter);
    }

    private String text;

    @ManyToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private BookEntity book;

    @OneToMany(mappedBy = "review")
    private List<BookReviewLikeEntity> reviewLikeList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;

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
