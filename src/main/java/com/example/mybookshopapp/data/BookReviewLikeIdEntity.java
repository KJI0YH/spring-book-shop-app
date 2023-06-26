package com.example.mybookshopapp.data;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Embeddable
@Data
public class BookReviewLikeIdEntity implements Serializable {

    @Column(name = "review_id")
    private Integer reviewId;

    @Column(name = "user_id")
    private Integer userId;
}
