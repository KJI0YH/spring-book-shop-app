package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.dto.BooksPageDto;
import com.example.mybookshopapp.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/books")
public class BooksRestApiController {

    private final BookService bookService;

    @Autowired
    public BooksRestApiController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/recent")
    public ResponseEntity<BooksPageDto> getRecentBooksPage(@RequestParam("offset") Integer offset,
                                                           @RequestParam("limit") Integer limit,
                                                           @RequestParam(value = "from", required = false) LocalDate from,
                                                           @RequestParam(value = "to", required = false) LocalDate to) {
        return ResponseEntity.ok(new BooksPageDto(bookService.getPageOfRecentBooks(from, to, offset, limit).getContent()));
    }

    @GetMapping("/recommended")
    public ResponseEntity<BooksPageDto> getRecommendedBooksPage(@RequestParam("offset") Integer offset,
                                                                @RequestParam("limit") Integer limit) {
        return ResponseEntity.ok(new BooksPageDto(bookService.getPageOfRecommendedBooks(offset, limit).getContent()));
    }

    @GetMapping("/popular")
    public ResponseEntity<BooksPageDto> getPopularBooksPage(@RequestParam("offset") Integer offset,
                                                            @RequestParam("limit") Integer limit) {
        return ResponseEntity.ok(new BooksPageDto(bookService.getPageOfPopularBooks(offset, limit).getContent()));
    }
}
