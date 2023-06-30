package com.example.mybookshopapp.data;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "book2user_type")
@Data
public class Book2UserTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String code;
    private String name;
}
