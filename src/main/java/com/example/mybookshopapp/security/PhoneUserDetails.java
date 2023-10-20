package com.example.mybookshopapp.security;

import com.example.mybookshopapp.data.UserEntity;

public class PhoneUserDetails extends EmailUserDetails {

    public PhoneUserDetails(UserEntity userEntity) {
        super(userEntity);
    }

    @Override
    public String getUsername(){
        return getUserEntity().getPhone();
    }
}
