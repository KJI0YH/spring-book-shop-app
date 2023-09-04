package com.example.mybookshopapp.services;

import org.springframework.stereotype.Service;

@Service
public class CartService {
    public String[] getCookiesIds(String cookieIds) {
        if (cookieIds == null || cookieIds.isEmpty()){
            return new String[0];
        }
        cookieIds = cookieIds.startsWith("/") ? cookieIds.substring(1) : cookieIds;
        cookieIds = cookieIds.endsWith("/") ? cookieIds.substring(0, cookieIds.length() - 1) : cookieIds;
        return cookieIds.split("/");
    }
}
