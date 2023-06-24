package com.example.mybookshopapp.security.jwt;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JwtRepository extends JpaRepository<JWTEntity, Integer> {
    JWTEntity findJWTEntityByToken(String token);
}
