package com.example.mybookshopapp.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "genre")
@Data
public class GenreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "parent_id", insertable = false, updatable = false)
    private Integer parentId;

    private String slug;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    private GenreEntity parent;

    @OneToMany(mappedBy = "parent")
    private List<GenreEntity> children;

    @OneToMany(mappedBy = "genre", fetch = FetchType.LAZY)
    private List<Book2GenreEntity> book2genreList;

    @ManyToMany(mappedBy = "genreList")
    private List<BookEntity> bookList;
}
