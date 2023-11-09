package com.example.mybookshopapp.data;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
    @JsonIgnore
    private Integer balance;

    @JsonGetter("balance")
    public String getBalanceJson(){
        return balance / 100 + "." + String.format("%02d", balance % 100);
    }

    private String name;

    @Column(name = "password_hash")
    @JsonIgnore
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
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "role2user",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @JsonIgnore
    @ToString.Exclude
    private List<RoleEntity> roleList;
    
    public List<String> getRoles(){
        return roleList.stream()
                .map(RoleEntity::getName)
                .collect(Collectors.toList());
    }
}
