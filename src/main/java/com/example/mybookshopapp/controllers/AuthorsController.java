package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.data.AuthorEntity;
import com.example.mybookshopapp.services.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Controller
@RequestMapping("/authors")
public class AuthorsController {

    private final AuthorService authorService;

    @Autowired
    public AuthorsController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @ModelAttribute("authorsMap")
    public Map<String, List<AuthorEntity>> authorsMap(){
        return authorService.getAuthorsMap();
    }

    @GetMapping
    public String authorsPage(){
        return "/authors/index";
    }
}
