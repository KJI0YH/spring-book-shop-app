package com.example.mybookshopapp.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Data
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String hash;

    private LocalDateTime regTime;

    private int balance;

    private String name;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Book2UserEntity> book2userList;

    @OneToMany(mappedBy = "user")
    private List<BookReviewEntity> reviewList;

    @OneToMany(mappedBy = "user")
    private List<BookReviewLikeEntity> reviewLikeList;
}
