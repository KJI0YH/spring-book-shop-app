package com.example.mybookshopapp.data;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_contact")
@Data
public class UserContactEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private ContactType type;

    private short approved;

    private String code;

    private int codeTrials;

    private LocalDateTime codeTime;

    private String contact;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;
}
