package com.example.mybookshopapp.security.jwt;

import jakarta.persistence.*;
import lombok.Data;

@Table(name = "jwt_black_list")
@Data
@Entity
public class JWTEntity {

    @Id
    private String token;
}
