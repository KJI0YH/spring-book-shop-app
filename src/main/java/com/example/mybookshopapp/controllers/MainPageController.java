package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.data.BookEntity;
import com.example.mybookshopapp.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
public class MainPageController {

    private final BookService bookService;

    @Autowired
    public MainPageController(BookService bookService) {
        this.bookService = bookService;
    }

    @ModelAttribute("recommendedBooks")
    public List<BookEntity> recommendedBooks(){
        return bookService.getBooksData();
    }

    @GetMapping("/")
    public String mainPage(Model model){
        return "index";
    }
}
