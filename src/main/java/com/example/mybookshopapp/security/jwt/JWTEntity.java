package com.example.mybookshopapp.security.jwt;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Table(name = "jwt_black_list")
@Data
@Entity
public class JWTEntity {

    @Id
    private String token;
}
