package com.example.mybookshopapp.services;

import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.ArrayList;

@Service
public class CartService {
    public String[] getCookiesIds(String cookieIds) {
        cookieIds = cookieIds.startsWith("/") ? cookieIds.substring(1) : cookieIds;
        cookieIds = cookieIds.endsWith("/") ? cookieIds.substring(0, cookieIds.length() - 1) : cookieIds;
        if (cookieIds.isEmpty()){
            return new String[0];
        }
        return cookieIds.split("/");
    }
}
