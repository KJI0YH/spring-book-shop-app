package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.data.AuthorEntity;
import com.example.mybookshopapp.dto.SearchWordDto;
import com.example.mybookshopapp.security.BookstoreUserRegister;
import com.example.mybookshopapp.services.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.*;

@Controller
public class AuthorsController {

    private final AuthorService authorService;
    private final BookstoreUserRegister userRegister;

    @Autowired
    public AuthorsController(AuthorService authorService, BookstoreUserRegister userRegister) {
        this.authorService = authorService;
        this.userRegister = userRegister;
    }

    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto(){
        return new SearchWordDto();
    }

    @ModelAttribute("authorsMap")
    public Map<String, List<AuthorEntity>> authorsMap(){
        return authorService.getAuthorsMap();
    }

    @ModelAttribute("curUsr")
    public Object curUsr(){
        return userRegister.getCurrentUser();
    }

    @GetMapping("/authors")
    public String authorsPage(){
        return "authors/index";
    }
}
