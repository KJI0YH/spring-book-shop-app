package com.example.mybookshopapp.data;

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
    private GenreEntity parent;

    @OneToMany(mappedBy = "parent")
    @ToString.Exclude
    private List<GenreEntity> children;

    @OneToMany(mappedBy = "genre", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Book2GenreEntity> book2genreList;

    @ManyToMany(mappedBy = "genreList")
    @ToString.Exclude
    private List<BookEntity> bookList;
}
