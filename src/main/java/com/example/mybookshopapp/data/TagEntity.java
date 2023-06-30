package com.example.mybookshopapp.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "tag")
@Data
public class TagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String slug;
    private String name;

    @OneToMany(mappedBy = "tag", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Book2TagEntity> book2tagList;
}
