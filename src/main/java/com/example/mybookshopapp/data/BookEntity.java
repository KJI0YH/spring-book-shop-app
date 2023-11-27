package com.example.mybookshopapp.data;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Entity
@Table(name = "book")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String slug;
    private String image;

    @Transient
    @JsonIgnore
    private String authors;
    private String title;
    private Integer discount;
    private boolean isBestseller;
    @Transient
    private Integer rating;
    @Transient
    @JsonIgnore
    private String status;
    @JsonIgnore
    private Integer price;
    @JsonIgnore
    private String description;
    private LocalDate pubDate;

    @ManyToMany
    @JoinTable(name = "book2author",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    @ToString.Exclude
    @JsonIgnore
    private List<AuthorEntity> authorList;
    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonProperty("book2author")
    private List<Book2AuthorEntity> book2AuthorList;
    @ManyToMany
    @JoinTable(name = "book2tag",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    @ToString.Exclude
    @JsonIgnore
    private List<TagEntity> tagList;
    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonProperty("book2tag")
    private List<Book2TagEntity> book2TagList;
    @ManyToMany
    @JoinTable(name = "book2genre",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    @ToString.Exclude
    @JsonIgnore
    private List<GenreEntity> genreList;
    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonProperty("book2genre")
    private List<Book2GenreEntity> book2GenreList;

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    @JsonProperty("book2files")
    @ToString.Exclude
    private List<BookFileEntity> bookFileList = new ArrayList<>();
    private Integer popularity = 0;
    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    private List<BookReviewEntity> reviewList;
    @OneToMany(mappedBy = "book")
    @JsonIgnore
    @ToString.Exclude
    private List<BookRateEntity> rateList;

    public List<AuthorEntity> getAuthorList() {
        book2AuthorList.sort(Comparator.comparing(Book2AuthorEntity::getSortIndex));
        return book2AuthorList.stream().map(Book2AuthorEntity::getAuthor).toList();
    }

    public List<Book2AuthorEntity> getBook2AuthorList() {
        if (book2AuthorList != null)
            book2AuthorList.sort(Comparator.comparing(Book2AuthorEntity::getSortIndex));
        return book2AuthorList;
    }

    @JsonGetter("authors")
    public String getAuthors() {
        if (book2AuthorList == null || book2AuthorList.isEmpty())
            return "";

        book2AuthorList.sort(Comparator.comparing(Book2AuthorEntity::getSortIndex));
        String authorsLine = book2AuthorList.get(0).getAuthor().toString();
        if (book2AuthorList.size() > 1) {
            authorsLine += " and others";
        }

        return authorsLine;
    }

    @JsonGetter("status")
    private Object getStatusJson() {
        if (status == null) {
            return false;
        }
        return status;
    }

    @JsonGetter("price")
    public String getPriceJson() {
        return price / 100 + "." + String.format("%02d", price % 100);
    }

    public Integer getDiscountPrice() {
        return Math.toIntExact(Math.round(price * (1 - discount / 100.0)));
    }

    @JsonGetter("discountPrice")
    public String getDiscountPriceJson() {
        int discountPrice = getDiscountPrice();
        return discountPrice / 100 + "." + String.format("%02d", discountPrice % 100);
    }

    public List<BookReviewEntity> getReviewList() {
        Collections.sort(reviewList);
        return reviewList;
    }

    public Long getRateCount(Integer rateValue) {
        return rateList.stream().filter(rate -> rate.getRate().equals(rateValue)).count();
    }

    public Integer getRating() {
        if (rateList == null || rateList.isEmpty()) return 0;
        return Math.round(rateList.stream().mapToInt(BookRateEntity::getRate).sum() / (float) rateList.size());
    }
}
