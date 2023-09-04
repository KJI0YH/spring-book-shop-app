package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.data.UserEntity;
import com.example.mybookshopapp.dto.SearchWordDto;
import com.example.mybookshopapp.security.BookstoreUserRegister;
import com.example.mybookshopapp.services.BookService;
import com.example.mybookshopapp.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;

public abstract class AbstractHeaderFooterController {

    @Autowired
    private BookstoreUserRegister userRegister;

    @Autowired
    private BookService bookService;

    @Autowired
    private CartService cartService;


    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto(){
        return new SearchWordDto();
    }

    @ModelAttribute("curUsr")
    public Object curUsr(){
        return userRegister.getCurrentUser();
    }

    @ModelAttribute("cartAmount")
    public Integer cartAmount(@CookieValue(value = "cartContents", required = false) String cartContents) {
        UserEntity user = (UserEntity) userRegister.getCurrentUser();
        if (user != null) {

            // Authorized user
            return bookService.getBooksByUserStatus(user.getId(), "CART").size();
        } else {

            // Unauthorized user
            return cartService.getCookiesIds(cartContents).length;
        }
    }

    @ModelAttribute("postponedAmount")
    public Integer postponedAmount(@CookieValue(value = "postponedContents", required = false) String postponedContents){
        UserEntity user = (UserEntity) userRegister.getCurrentUser();

        // Authorized user
        if (user != null){
            return bookService.getBooksByUserStatus(user.getId(), "KEPT").size();

        }

        // Unauthorized user
        else {
            return cartService.getCookiesIds(postponedContents).length;
        }
    }
}
