package com.example.mybookshopapp.data;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "genre")
@Data
public class GenreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "parent_id", insertable = false, updatable = false)
    private Integer parentId;
    
    private String slug;
    private String name;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonIgnore
    private GenreEntity parent;

    @OneToMany(mappedBy = "parent")
    @ToString.Exclude
    @JsonIgnore
    private List<GenreEntity> children;

    @OneToMany(mappedBy = "genre", fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonIgnore
    private List<Book2GenreEntity> book2genreList;

    @ManyToMany(mappedBy = "genreList")
    @ToString.Exclude
    @JsonIgnore
    private List<BookEntity> bookList;
}
