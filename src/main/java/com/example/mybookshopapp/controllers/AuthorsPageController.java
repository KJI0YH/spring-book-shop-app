package com.example.mybookshopapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/bookshop/authors")
public class AuthorsPageController {

    @GetMapping
    public String authorsPage(){
        return "authors/index";
    }
}
