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
public class BookReviewEntity implements Comparable<BookReviewEntity>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalDateTime time;

    public String getTime(){
        return String.valueOf(String.format("%02d", time.getDayOfMonth())) + '.' +
                String.format("%02d", time.getMonthValue()) + '.' +
                time.getYear() + ' ' +
                String.format("%02d", time.getHour()) + ':' +
                String.format("%02d", time.getMinute()) + ':' +
                String.format("%02d", time.getSecond());
    }

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

    @Override
    public int compareTo(BookReviewEntity o) {
        if (getPopularityValue() > o.getPopularityValue()){
            return -1;
        } else if (getPopularityValue() < o.getPopularityValue()){
            return 1;
        }
        return 0;
    }
}
