package com.example.mybookshopapp.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "book")
@Data
public class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDate pubDate;

    private int isBestseller;

    private String slug;

    private String title;

    private String image;

    private Integer price;

    private Integer discount;

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Book2AuthorEntity> book2authorList;

    @ManyToMany
    @JoinTable(name = "book2author",
        joinColumns = @JoinColumn(name = "book_id"),
        inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    @JsonIgnore
    private List<AuthorEntity> authorList;

    public Integer getPriceWithDiscount(){
        return Math.toIntExact(Math.round(price * (1 - discount / 100.0)));
    }
}
