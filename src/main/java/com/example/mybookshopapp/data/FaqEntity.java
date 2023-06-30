package com.example.mybookshopapp.data;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "faq")
@Data
public class FaqEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer sortIndex;
    private String question;
    private String answer;
}
