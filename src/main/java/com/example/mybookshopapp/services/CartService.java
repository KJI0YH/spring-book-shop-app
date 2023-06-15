package com.example.mybookshopapp.services;

import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class CartService {

    public Integer[] getCookiesIds(String cookieIds){
        cookieIds = cookieIds.startsWith("/") ? cookieIds.substring(1) : cookieIds;
        cookieIds = cookieIds.endsWith("/") ? cookieIds.substring(0, cookieIds.length() - 1) : cookieIds;
        String[] cartItems = cookieIds.split("/");
        return Arrays.stream(cartItems)
                .map(Integer::valueOf)
                .toArray(Integer[]::new);
    }
}
