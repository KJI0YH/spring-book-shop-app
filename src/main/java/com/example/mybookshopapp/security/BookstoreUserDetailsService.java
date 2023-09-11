package com.example.mybookshopapp.security;

import com.example.mybookshopapp.data.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class BookstoreUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public BookstoreUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findUserEntityByEmail(username);
        if (userEntity != null) {
            return new BookstoreUserDetails(userEntity);
        }
        userEntity = userRepository.findUserEntityByPhone(username);
        if (userEntity != null) {
            return new PhoneNumberUserDetails(userEntity);
        }
        throw new UsernameNotFoundException("User not found");
    }
}
