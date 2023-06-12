package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.data.AuthorEntity;
import com.example.mybookshopapp.data.BookEntity;
import com.example.mybookshopapp.dto.SearchWordDto;
import com.example.mybookshopapp.services.AuthorService;
import com.example.mybookshopapp.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/books/author/{authorSlug}")
public class AuthorBooksController {

    private final BookService bookService;
    private final AuthorService authorService;

    @Autowired
    public AuthorBooksController(BookService bookService, AuthorService authorService) {
        this.bookService = bookService;
        this.authorService = authorService;
    }

    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }

    @ModelAttribute("booksList")
    public List<BookEntity> authorBooks(@PathVariable("authorSlug") String authorSlug) {
        return bookService.getPageOfBooksByAuthorSlug(authorSlug, 0, 20).getContent();
    }

    @ModelAttribute("author")
    public AuthorEntity author(@PathVariable("authorSlug") String authorSlug) {
        return authorService.getAuthorBySlug(authorSlug);
    }

    @GetMapping
    public String authorBooksPage() {
        return "/books/author";
    }

}
