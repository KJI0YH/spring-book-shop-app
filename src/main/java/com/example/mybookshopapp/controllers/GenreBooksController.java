package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.data.BookEntity;
import com.example.mybookshopapp.data.GenreEntity;
import com.example.mybookshopapp.dto.SearchWordDto;
import com.example.mybookshopapp.security.BookstoreUserRegister;
import com.example.mybookshopapp.services.BookService;
import com.example.mybookshopapp.services.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/genres/{genreSlug}")
public class GenreBooksController {

    private final BookService bookService;
    private final GenreService genreService;
    private final BookstoreUserRegister userRegister;

    @Autowired
    public GenreBooksController(BookService bookService, GenreService genreService, BookstoreUserRegister userRegister) {
        this.bookService = bookService;
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

    @ModelAttribute("booksList")
    public List<BookEntity> booksByGenreId(@PathVariable(value = "genreSlug") String genreSlug){
        return bookService.getPageOfBooksByGenreSlug(genreSlug, 0, 20).getContent();
    }

    @ModelAttribute("genre")
    public GenreEntity genre(@PathVariable("genreSlug") String genreSlug){
        return genreService.getGenreBySlug(genreSlug);
    }

    @ModelAttribute("breadcrumbs")
    public List<GenreEntity> breadcrumbs(@PathVariable("genreSlug") String genreSlug){
        GenreEntity genre = genreService.getGenreBySlug(genreSlug);
        List<GenreEntity> breadcrumbs = new ArrayList<>();

        while (genre.getParent() != null){
            breadcrumbs.add(genre.getParent());
            genre = genre.getParent();
        }
        Collections.reverse(breadcrumbs);
        return breadcrumbs;
    }

    @GetMapping
    public String genrePage(){
        return "/genres/slug";
    }
}
