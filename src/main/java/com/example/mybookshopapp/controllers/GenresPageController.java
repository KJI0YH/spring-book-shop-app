package com.example.mybookshopapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.logging.Logger;

@Controller
@RequestMapping("/bookshop/genres")
public class GenresPageController {

    @GetMapping
    public String genresPage(){
        return "genres/index";
    }
}
