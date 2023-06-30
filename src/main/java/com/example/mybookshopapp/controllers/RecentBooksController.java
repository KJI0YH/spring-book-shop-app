package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.data.BookEntity;
import com.example.mybookshopapp.dto.SearchWordDto;
import com.example.mybookshopapp.security.BookstoreUserRegister;
import com.example.mybookshopapp.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/books")
public class RecentBooksController {

    private final BookService bookService;
    private final BookstoreUserRegister userRegister;

    @Autowired
    public RecentBooksController(BookService bookService, BookstoreUserRegister userRegister) {
        this.bookService = bookService;
        this.userRegister = userRegister;
    }

    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }

    @ModelAttribute("curUsr")
    public Object curUsr(){
        return userRegister.getCurrentUser();
    }

    @ModelAttribute("booksList")
    public List<BookEntity> booksList() {
        return bookService.getPageOfRecentBooks(LocalDate.now().minusMonths(1), LocalDate.now(), 0, 20).getContent();
    }

    @GetMapping("/recent")
    public String recentBooksPage() {
        return "books/recent";
    }
}