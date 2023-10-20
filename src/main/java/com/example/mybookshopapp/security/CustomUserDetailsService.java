package com.example.mybookshopapp.security;

import com.example.mybookshopapp.data.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findUserEntityByEmail(username);
        if (userEntity != null) {
            return new EmailUserDetails(userEntity);
        }
        userEntity = userRepository.findUserEntityByPhone(username);
        if (userEntity != null) {
            return new PhoneUserDetails(userEntity);
        }
        throw new UsernameNotFoundException("User with username " + username + " not found");
    }
}
