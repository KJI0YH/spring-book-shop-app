package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.dto.Book;
import com.example.mybookshopapp.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/books")
public class PopularBooksController {

    private final BookService bookService;

    @Autowired
    public PopularBooksController(BookService bookService) {
        this.bookService = bookService;
    }

    @ModelAttribute("booksList")
    public List<Book> booksList() {
        // TODO: bookservice.getPopularBooksData();
        return bookService.getBooksData();
    }

    @GetMapping("/popular")
    public String popularBooksPage(){
        return "/books/popular";
    }
}
