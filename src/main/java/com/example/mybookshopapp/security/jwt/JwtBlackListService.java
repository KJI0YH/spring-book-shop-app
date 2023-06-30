package com.example.mybookshopapp.security.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JwtBlackListService {

    private final JwtRepository jwtRepository;

    @Autowired
    public JwtBlackListService(JwtRepository jwtRepository) {
        this.jwtRepository = jwtRepository;
    }

    public void AddJwtToBlackList(JWTEntity jwtEntity){
        jwtRepository.save(jwtEntity);
    }

    public boolean isInBlackList(String token){
        JWTEntity jwtEntity = jwtRepository.findJWTEntityByToken(token);
        return jwtEntity != null;
    }
}
