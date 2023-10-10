package com.example.mybookshopapp.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CookieService {

    public String[] getStringIds(String cookie) {
        if (cookie == null || cookie.isEmpty()) {
            return new String[0];
        }
        cookie = cookie.strip();
        cookie = cookie.startsWith("/") ? cookie.substring(1) : cookie;
        cookie = cookie.endsWith("/") ? cookie.substring(0, cookie.length() - 1) : cookie;
        return cookie.split("/");
    }

    public Integer[] getIntegerIds(String cookie) {
        String[] stringIds = getStringIds(cookie);
        return Arrays.stream(stringIds).map(Integer::valueOf).toArray(Integer[]::new);
    }
}
