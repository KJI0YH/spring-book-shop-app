package com.example.mybookshopapp.data;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "book_rate")
@Data
public class BookRateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Integer bookId;

    @Column(name = "value_1")
    private Integer value1;

    @Column(name = "value_2")
    private Integer value2;

    @Column(name = "value_3")
    private Integer value3;

    @Column(name = "value_4")
    private Integer value4;

    @Column(name = "value_5")
    private Integer value5;

    @OneToOne
    @MapsId
    @JoinColumn(name = "book_id")
    private BookEntity book;

    public Integer getValuesCount(){
        return value1 + value2 + value3 + value4 + value5;
    }

    public Integer getAverageValue(){
        return Math.toIntExact(Math.round((double)(value1 + 2 * value2 + 3 * value3 + 4 * value4 + 5 * value5) / getValuesCount()));
    }
}
