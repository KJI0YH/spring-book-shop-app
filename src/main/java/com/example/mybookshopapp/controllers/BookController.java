package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.data.BookEntity;
import com.example.mybookshopapp.dto.SearchWordDto;
import com.example.mybookshopapp.repositories.BookRepository;
import com.example.mybookshopapp.services.BookService;
import com.example.mybookshopapp.services.ResourceStorage;
import io.swagger.v3.oas.annotations.media.Content;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/books/{bookSlug}")
public class BookController {

    private final BookService bookService;
    private final ResourceStorage storage;
    private final BookRepository bookRepository;

    @Autowired
    public BookController(BookService bookService, ResourceStorage storage, BookRepository bookRepository) {
        this.bookService = bookService;
        this.storage = storage;
        this.bookRepository = bookRepository;
    }

    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto(){
        return new SearchWordDto();
    }

    @ModelAttribute("book")
    public BookEntity book(@PathVariable("bookSlug") String slug){
        return bookService.getBookBySlug(slug);
    }

    @GetMapping
    public String getBookPage(){
        return "/books/slug";
    }

    @PostMapping("/img/save")
    public String saveNewBookImage(@PathVariable("bookSlug") String bookSlug,
                                   @RequestParam("file")MultipartFile file) throws IOException {

        String savePath = storage.saveNewBookImage(file, bookSlug);
        BookEntity bookToUpdate = bookService.getBookBySlug(bookSlug);
        bookToUpdate.setImage(savePath);
        bookRepository.save(bookToUpdate);

        return ("redirect:/books/" + bookSlug);
    }
}
