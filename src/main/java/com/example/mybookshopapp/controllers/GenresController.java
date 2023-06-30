package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.data.GenreEntity;
import com.example.mybookshopapp.dto.SearchWordDto;
import com.example.mybookshopapp.security.BookstoreUserRegister;
import com.example.mybookshopapp.services.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/genres")
public class GenresController {

    private final GenreService genreService;
    private final BookstoreUserRegister userRegister;

    @Autowired
    public GenresController(GenreService genreService, BookstoreUserRegister userRegister) {
        this.genreService = genreService;
        this.userRegister = userRegister;
    }

    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto(){
        return new SearchWordDto();
    }

    @ModelAttribute("curUsr")
    public Object curUsr(){
        return userRegister.getCurrentUser();
    }

    @ModelAttribute("genres")
    public List<GenreEntity> genres(){
        return genreService.getAllRootGenres();
    }

    @GetMapping
    public String genresPage(){
        return "genres/index";
    }
}
