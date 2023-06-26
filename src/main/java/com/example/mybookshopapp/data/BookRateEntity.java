package com.example.mybookshopapp.data;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "book_rate")
public class BookRateEntity {

    @EmbeddedId
    private BookRateIdEntity id;
    private Integer rate;
}
