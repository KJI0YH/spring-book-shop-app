package com.example.mybookshopapp.data;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "book2author")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Book2AuthorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer sortIndex;

    @ManyToOne
    @JoinColumn(name = "book_id")
    @JsonIgnore
    private BookEntity book;

    @ManyToOne
    @JoinColumn(name = "author_id")
    @JsonIgnore
    private AuthorEntity author;
    
    @JsonGetter("bookId")
    public Integer bookId(){
        return book.getId() ;
    }
    
    @JsonGetter("authorId")
    public Integer authorId(){
        return author.getId()   ;
    }
}
