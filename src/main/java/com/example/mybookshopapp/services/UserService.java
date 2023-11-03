package com.example.mybookshopapp.services;

import com.example.mybookshopapp.data.UserEntity;
import com.example.mybookshopapp.dto.ContactConfirmationPayload;
import com.example.mybookshopapp.dto.ContactConfirmationResponse;
import com.example.mybookshopapp.dto.RegistrationForm;
import com.example.mybookshopapp.errors.RoleDoesNotExistsException;
import com.example.mybookshopapp.errors.UserAlreadyExistException;
import com.example.mybookshopapp.repositories.UserRepository;
import com.example.mybookshopapp.security.CustomUserDetailsService;
import com.example.mybookshopapp.security.EmailUserDetails;
import com.example.mybookshopapp.security.jwt.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;
    private final JWTUtil jwtUtil;
    private final RoleService roleService;

    public void registerNewUser(RegistrationForm registrationForm) throws UserAlreadyExistException, RoleDoesNotExistsException {
        registerNewUser(registrationForm, "USER");
    }

    public void registerNewUser(RegistrationForm registrationForm, String roleName) throws UserAlreadyExistException, RoleDoesNotExistsException {

        UserEntity userByEmail = userRepository.findUserEntityByEmail(registrationForm.getEmail());
        if (userByEmail != null)
            throw new UserAlreadyExistException("User with email " + userByEmail.getEmail() + " already exists");

        UserEntity userByPhone = userRepository.findUserEntityByPhone(registrationForm.getPhone());
        if (userByPhone != null)
            throw new UserAlreadyExistException("User with phone " + userByPhone.getPhone() + " already exists");

        UserEntity user = new UserEntity();
        user.setName(registrationForm.getName());
        user.setEmail(registrationForm.getEmail());
        user.setPhone(registrationForm.getPhone());
        user.setPassword(passwordEncoder.encode(registrationForm.getPass()));
        user.setRegTime(LocalDateTime.now());
        user.setHash(generateHash(user));
        user.setBalance(0);
        UserEntity newUser = userRepository.save(user);
        roleService.addRoleToUser(roleName, newUser);
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
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(payload.getContact(), payload.getCode()));
        EmailUserDetails userDetails = (EmailUserDetails) customUserDetailsService.loadUserByUsername(payload.getContact());
        String jwtToken = jwtUtil.generateToken(userDetails);
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult(jwtToken);
        return response;
    }

    public UserEntity getCurrentUser() {
        try {
            EmailUserDetails userDetails = (EmailUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return userDetails.getUserEntity();
        } catch (ClassCastException e) {
            return null;
        }
    }

    public UserEntity changePassword(UserEntity user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return user;
    }

    public UserEntity changeName(UserEntity user, String newName) {
        user.setName(newName);
        userRepository.save(user);
        return user;
    }

    public UserEntity changeEmail(UserEntity user, String newEmail) throws UserAlreadyExistException {
        if (userRepository.findUserEntityByEmail(newEmail) != null) {
            throw new UserAlreadyExistException("User with email " + newEmail + " already exists");
        }
        user.setEmail(newEmail);
        userRepository.save(user);
        return user;
    }

    public UserEntity changePhone(UserEntity user, String newPhone) throws UserAlreadyExistException {
        if (userRepository.findUserEntityByPhone(newPhone) != null){
            throw new UserAlreadyExistException("User with phone " + newPhone + " already exists");
        }
        user.setPhone(newPhone);
        userRepository.save(user);
        return user;
    }

    private String generateHash(UserEntity user) {
        String input = user.getName() + ":" + user.getEmail() + ":" + user.getPhone() + ":" + user.getRegTime();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] inputBytes = input.getBytes(StandardCharsets.UTF_8);
            messageDigest.update(inputBytes);
            byte[] digest = messageDigest.digest();
            StringBuilder builder = new StringBuilder();
            for (byte b : digest) {
                builder.append(String.format("%02x", b));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            return user.getRegTime().toString();
        }
    }
}
