package com.example.mybookshopapp.services;

import com.example.mybookshopapp.data.UserEntity;
import com.example.mybookshopapp.errors.ApiWrongParameterException;
import com.example.mybookshopapp.security.CustomUserDetailsService;
import com.example.mybookshopapp.security.EmailUserDetails;
import com.example.mybookshopapp.security.jwt.JWTUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CookieService {
    private final CustomUserDetailsService customUserDetailsService;
    private final JWTUtil jwtUtil;

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

    public Cookie createCookieFromBookIds(String cookieName, String cookiePath, List<String> ids) {
        Cookie cookie = new Cookie(cookieName, String.join("/", ids));
        cookie.setPath(cookiePath);
        return cookie;
    }

    public Cookie addBookId(String cookieValue, String id) {
        List<String> ids = new ArrayList<>(List.of(getStringIds(cookieValue)));
        if (!ids.contains(id)) {
            ids.add(id);
        }
        return createCookieFromBookIds("viewedContents", "/", ids);
    }

    public List<Cookie> updateCookieBookStatuses(String[] bookIds, String cartCookie, String keptCookie, String status) throws ApiWrongParameterException {
        List<String> cartIds = new ArrayList<>(List.of(getStringIds(cartCookie)));
        List<String> keptIds = new ArrayList<>(List.of(getStringIds(keptCookie)));

        for (String bookId : bookIds) {
            switch (status) {
                case "CART":
                    if (!cartIds.contains(bookId)) cartIds.add(bookId);
                    keptIds.remove(bookId);
                    break;
                case "KEPT":
                    if (!keptIds.contains(bookId)) keptIds.add(bookId);
                    cartIds.remove(bookId);
                    break;
                case "UNLINK":
                    cartIds.remove(bookId);
                    keptIds.remove(bookId);
                    break;
                default:
                    throw new ApiWrongParameterException("Invalid status parameter value");
            }
        }

        List<Cookie> cookies = new ArrayList<>();
        cookies.add(createCookieFromBookIds("cartContents", "/", cartIds));
        cookies.add(createCookieFromBookIds("postponedContents", "/", keptIds));
        return cookies;
    }

    public void deleteAllCookies(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return;
        for (Cookie cookie : cookies) {
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
    }

    public Cookie getJwtTokenCookie(UserEntity user) {
        EmailUserDetails userDetails = (EmailUserDetails) customUserDetailsService.loadUserByUsername(user.getEmail());
        Cookie cookie = new Cookie("token", jwtUtil.generateToken(userDetails));
        cookie.setPath("/");
        return cookie;
    }
}
