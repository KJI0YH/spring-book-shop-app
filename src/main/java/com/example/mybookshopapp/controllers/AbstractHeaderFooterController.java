package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.data.UserEntity;
import com.example.mybookshopapp.dto.SearchWordDto;
import com.example.mybookshopapp.services.UserService;
import com.example.mybookshopapp.services.BookService;
import com.example.mybookshopapp.services.CookieService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;

public abstract class AbstractHeaderFooterController {

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @Autowired
    private CookieService cookieService;

    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }

    @ModelAttribute("curUsr")
    public Object curUsr() {
        return userService.getCurrentUser();
    }

    @ModelAttribute("cartAmount")
    public Long cartAmount(@CookieValue(value = "cartContents", required = false) String cartContents) {
        UserEntity user = (UserEntity) userService.getCurrentUser();

        // Authorized user
        if (user != null) {
            return bookService.getCountOfBooksByUserStatus(user.getId(), "CART");

            // Unauthorized user
        } else {
            return (long) cookieService.getStringIds(cartContents).length;
        }
    }

    @ModelAttribute("postponedAmount")
    public Long postponedAmount(@CookieValue(value = "postponedContents", required = false) String postponedContents) {
        UserEntity user = (UserEntity) userService.getCurrentUser();

        // Authorized user
        if (user != null) {
            return bookService.getCountOfBooksByUserStatus(user.getId(), "KEPT");
        }

        // Unauthorized user
        else {
            return (long) cookieService.getStringIds(postponedContents).length;
        }
    }

    @ModelAttribute("myAmount")
    public Long myAmount() {
        UserEntity user = (UserEntity) userService.getCurrentUser();
        if (user != null) {
            return bookService.getCountOfBooksByUserStatus(user.getId(), "PAID") +
                    bookService.getCountOfBooksByUserStatus(user.getId(), "ARCHIVED");
        }
        return 0L;
    }

    @ModelAttribute("requestURI")
    public String request(HttpServletRequest request) {
        return request.getRequestURI();
    }
}
