package com.example.mybookshopapp.data;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "author")
public class AuthorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "VARCHAR(255)")
    private String photo;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String slug;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String firstName;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String lastName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
   private List<Book2AuthorEntity> book2AuthorList;

    public AuthorEntity() {

    }

    public List<Book2AuthorEntity> getBook2AuthorList() {
        return book2AuthorList;
    }

    public void setBook2AuthorList(List<Book2AuthorEntity> book2AuthorList) {
        this.book2AuthorList = book2AuthorList;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}
