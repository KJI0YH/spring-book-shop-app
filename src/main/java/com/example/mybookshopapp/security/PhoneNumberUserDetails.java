package com.example.mybookshopapp.security;

import com.example.mybookshopapp.data.UserEntity;
import org.springframework.security.core.userdetails.UserDetails;

public class PhoneNumberUserDetails extends BookstoreUserDetails {

    public PhoneNumberUserDetails(UserEntity userEntity) {
        super(userEntity);
    }

    @Override
    public String getUsername(){
        return getUserEntity().getPhone();
    }
}
