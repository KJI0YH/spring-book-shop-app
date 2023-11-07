package com.example.mybookshopapp.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "author")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
    @JsonIgnore
    private List<Book2AuthorEntity> book2AuthorList;

    @ManyToMany(mappedBy = "authorList")
    @ToString.Exclude
    @JsonIgnore
    private List<BookEntity> bookList;

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}
