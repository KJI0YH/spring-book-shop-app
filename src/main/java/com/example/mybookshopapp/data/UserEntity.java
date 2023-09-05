package com.example.mybookshopapp.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String hash;
    private LocalDateTime regTime;
    private Integer balance;
    private String name;

    @Column(name = "password_hash")
    private String password;
    private String email;
    private String phone;

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    @JsonIgnore
    private List<UserContactEntity> contactList;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    @ToString.Exclude
    private List<Book2UserEntity> book2userList;

    @OneToMany
    @JoinColumn(name = "review_id")
    @ToString.Exclude
    @JsonIgnore
    private List<BookReviewEntity> reviewList;
}
