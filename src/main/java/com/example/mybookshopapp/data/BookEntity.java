package com.example.mybookshopapp.data;

import com.example.mybookshopapp.services.BooksRatingAndPopularityService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
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

    private String description;

    @ManyToMany
    @JoinTable(name = "book2author",
        joinColumns = @JoinColumn(name = "book_id"),
        inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    @JsonIgnore
    private List<AuthorEntity> authorList;

    @Transient
    private Integer discountPrice;

    public Integer getDiscountPrice(){
        return Math.toIntExact(Math.round(price * (1 - discount / 100.0)));
    }

    @Transient
    private List<String> authors;

    public List<String> getAuthors(){
        return authorList.stream().map(AuthorEntity::toString).toList();
    }

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Book2UserEntity> book2userList;

    @ManyToMany
    @JoinTable(name = "book2user",
    joinColumns = @JoinColumn(name = "book_id"),
    inverseJoinColumns = @JoinColumn(name = "user_id"))
    @JsonIgnore
    private List<UserEntity> userList;

    @ManyToMany
    @JoinTable(name = "book2tag",
    joinColumns = @JoinColumn(name = "book_id"),
    inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<TagEntity> tagList;

    @ManyToMany
    @JoinTable(name = "book2genre",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    @JsonIgnore
    private List<GenreEntity> genreList;

    @OneToMany(mappedBy = "book")
    private List<BookFileEntity> bookFileList = new ArrayList<>();

    @Transient
    @JsonIgnore
    private Double popularity;

    @PostLoad
    private void calculatePopularity(){
        BooksRatingAndPopularityService calculator = new BooksRatingAndPopularityService();
        popularity = calculator.getPopularity(this);
    }

    @OneToOne(mappedBy = "book", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private BookRateEntity rate;
}
