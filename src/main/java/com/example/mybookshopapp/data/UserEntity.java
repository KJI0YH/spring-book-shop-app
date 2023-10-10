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

    public Integer getRating(){
        long likesCount = reviewList.stream().mapToLong(BookReviewEntity::getLikesCount).sum();
        long dislikesCount = reviewList.stream().mapToLong(BookReviewEntity::getDislikesCount).sum();
        if (likesCount == 0 && dislikesCount == 0) return 0;
        return Math.toIntExact(Math.round((double) likesCount / (likesCount + dislikesCount) * 5.0));
    }

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    @JsonIgnore
    private List<UserContactEntity> contactList;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    @ToString.Exclude
    private List<Book2UserEntity> book2userList;

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    @JsonIgnore
    private List<BookReviewEntity> reviewList;
}
