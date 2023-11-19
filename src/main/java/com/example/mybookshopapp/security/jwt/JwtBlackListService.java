package com.example.mybookshopapp.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JwtBlackListService {

    private final JwtRepository jwtRepository;

    public void addJwtToBlackList(JWTEntity jwtEntity) {
        jwtRepository.save(jwtEntity);
    }

    public boolean isInBlackList(String token) {
        JWTEntity jwtEntity = jwtRepository.findJWTEntityByToken(token);
        return jwtEntity != null;
    }
}
