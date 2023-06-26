package com.example.mybookshopapp.data;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class BookRateIdEntity implements Serializable {
    private Integer bookId;
    private Integer userId;
}
