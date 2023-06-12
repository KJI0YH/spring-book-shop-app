package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.data.BookEntity;
import com.example.mybookshopapp.dto.SearchWordDto;
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

    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto(){
        return new SearchWordDto();
    }

    @ModelAttribute("booksList")
    public List<BookEntity> booksList() {
        return bookService.getPageOfPopularBooks(0, 20).getContent();
    }

    @GetMapping("/popular")
    public String popularBooksPage(){
        return "/books/popular";
    }
}
