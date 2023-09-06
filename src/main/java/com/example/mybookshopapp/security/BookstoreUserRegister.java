package com.example.mybookshopapp.security;

import com.example.mybookshopapp.data.UserEntity;
import com.example.mybookshopapp.errors.UserAlreadyExistException;
import com.example.mybookshopapp.security.jwt.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BookstoreUserRegister {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final BookstoreUserDetailsService bookstoreUserDetailsService;
    private final JWTUtil jwtUtil;

    @Autowired
    public BookstoreUserRegister(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, BookstoreUserDetailsService bookstoreUserDetailsService, JWTUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.bookstoreUserDetailsService = bookstoreUserDetailsService;
        this.jwtUtil = jwtUtil;
    }

    public UserEntity registerNewUser(RegistrationForm registrationForm) {

        UserEntity userByEmail = userRepository.findUserEntityByEmail(registrationForm.getEmail());
        UserEntity userByPhone = userRepository.findUserEntityByPhone(registrationForm.getPhone());

        if (userByEmail == null && userByPhone == null) {
            UserEntity user = new UserEntity();
            user.setName(registrationForm.getName());
            user.setEmail(registrationForm.getEmail());
            user.setPhone(registrationForm.getPhone());
            user.setPassword(passwordEncoder.encode(registrationForm.getPass()));
            user.setRegTime(LocalDateTime.now());

            // TODO generate hash for user
            user.setHash("hash");
            user.setBalance(0);
            userRepository.save(user);
            return user;
        } else {
            return userByPhone;
//            throw new UserAlreadyExistException("User with email " + registrationForm.getEmail() + " already exists");
        }
    }

    public ContactConfirmationResponse login(ContactConfirmationPayload payload) {
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(payload.getContact(),
                        payload.getCode()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult("true");
        return response;
    }

    public ContactConfirmationResponse jwtLogin(ContactConfirmationPayload payload) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(payload.getContact(),
                payload.getCode()));
        BookstoreUserDetails userDetails = (BookstoreUserDetails) bookstoreUserDetailsService.loadUserByUsername(payload.getContact());
        String jwtToken = jwtUtil.generateToken(userDetails);
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult(jwtToken);
        return response;
    }

    public ContactConfirmationResponse jwtLoginByPhoneNumber(ContactConfirmationPayload payload) {
        RegistrationForm registrationForm = new RegistrationForm();
        registrationForm.setPhone(payload.getContact());
        registrationForm.setPass(payload.getCode());
        registerNewUser(registrationForm);
        UserDetails userDetails = bookstoreUserDetailsService.loadUserByUsername(payload.getContact());
        String jwtToken = jwtUtil.generateToken(userDetails);
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult(jwtToken);
        return response;
    }

    public Object getCurrentUser() {
        try {
            BookstoreUserDetails userDetails = (BookstoreUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return userDetails.getUserEntity();
        } catch (ClassCastException e) {
            return null;
        }
    }


}
