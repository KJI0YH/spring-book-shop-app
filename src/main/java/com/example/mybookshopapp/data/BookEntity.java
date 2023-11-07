package com.example.mybookshopapp.data;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
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
    @JsonIgnore
    @ToString.Exclude
    private List<AuthorEntity> authorList;
    @ManyToMany
    @JoinTable(name = "book2tag",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    @JsonIgnore
    @ToString.Exclude
    private List<TagEntity> tagList;
    @ManyToMany
    @JoinTable(name = "book2genre",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    @JsonIgnore
    @ToString.Exclude
    private List<GenreEntity> genreList;
    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<BookFileEntity> bookFileList = new ArrayList<>();
    @JsonIgnore
    private Integer popularity = 0;
    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    private List<BookReviewEntity> reviewList;
    @OneToMany(mappedBy = "book")
    @JsonIgnore
    @ToString.Exclude
    private List<BookRateEntity> rateList;

    @JsonGetter
    public String getAuthors() {
        if (authorList == null || authorList.isEmpty())
            return "";
        
        String authors = authorList.get(0).toString();
        if (authorList.size() > 1) {
            authors += " and others";
        }

        return authors;
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
