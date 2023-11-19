package com.example.mybookshopapp.data;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookReviewLikeIdEntity implements Serializable {

    @Column(name = "review_id")
    private Integer reviewId;

    @Column(name = "user_id")
    private Integer userId;
}
