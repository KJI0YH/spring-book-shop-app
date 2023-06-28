package com.example.mybookshopapp.services;

import com.example.mybookshopapp.data.Book2UserTypeEntity;
import com.example.mybookshopapp.data.BookEntity;
import com.example.mybookshopapp.repositories.Book2UserTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    private final BookService bookService;
    private final Book2UserTypeRepository book2UserTypeRepository;

    @Autowired
    public CartService(BookService bookService, Book2UserTypeRepository book2UserTypeRepository) {
        this.bookService = bookService;
        this.book2UserTypeRepository = book2UserTypeRepository;
    }

    public String[] getCookiesIds(String cookieIds){
        cookieIds = cookieIds.startsWith("/") ? cookieIds.substring(1) : cookieIds;
        cookieIds = cookieIds.endsWith("/") ? cookieIds.substring(0, cookieIds.length() - 1) : cookieIds;
        return cookieIds.split("/");
    }
}
