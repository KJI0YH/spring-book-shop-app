package com.example.mybookshopapp.data;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "author")
@Data
public class AuthorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String photo;

    private String slug;

    private String firstName;

    private String lastName;

    private String description;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
   private List<Book2AuthorEntity> book2AuthorList;

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}
