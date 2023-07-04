package com.example.mybookshopapp.data;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book2UserIdEntity implements Serializable {

    @Column(name = "book_id")
    private Integer bookId;

    @Column(name = "user_id")
    private Integer userId;
}
