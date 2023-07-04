package com.example.mybookshopapp.security;

import com.example.mybookshopapp.SpringBootApplicationTest;
import com.example.mybookshopapp.data.UserEntity;
import com.example.mybookshopapp.security.jwt.JWTUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

class BookstoreUserRegisterTest extends SpringBootApplicationTest {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final BookstoreUserDetailsService bookstoreUserDetailsService;
    private final JWTUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public BookstoreUserRegisterTest(UserRepository userRepository, AuthenticationManager authenticationManager, BookstoreUserDetailsService bookstoreUserDetailsService, JWTUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.bookstoreUserDetailsService = bookstoreUserDetailsService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void registerNewUserTest() {
        Assertions.assertNotNull(saveNewUser());
    }

    @Test
    void jwtLoginTest() {
        UserEntity user = saveNewUser();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                user.getEmail(),
                "password"));
        BookstoreUserDetails userDetails = (BookstoreUserDetails) bookstoreUserDetailsService.loadUserByUsername(user.getEmail());
        String jwtToken = jwtUtil.generateToken(userDetails);
        Assertions.assertTrue(jwtUtil.validateToken(jwtToken, userDetails));
        Assertions.assertFalse(jwtUtil.isTokenExpired(jwtToken));
        Assertions.assertEquals(jwtUtil.extractUsername(jwtToken), user.getEmail());
    }

    private UserEntity saveNewUser(){
        UserEntity userEntity = UserEntity.builder()
                .hash("hash")
                .password(passwordEncoder.encode("password"))
                .regTime(LocalDateTime.now())
                .balance(0)
                .email("email@email.com")
                .phone("+711111111111")
                .name("name")
                .build();
        return userRepository.save(userEntity);
    }
}