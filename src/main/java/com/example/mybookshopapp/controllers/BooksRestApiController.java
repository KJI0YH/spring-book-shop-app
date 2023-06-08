package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.data.BookEntity;
import com.example.mybookshopapp.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BooksRestApiController {

    private final BookService bookService;

    @Autowired
    public BooksRestApiController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/by-title")
    public ResponseEntity<List<BookEntity>> booksByTitle(@RequestParam("title") String title){
        return ResponseEntity.ok(bookService.getBooksByTitle(title));
    }

    @GetMapping("/popular")
    public ResponseEntity<List<BookEntity>> booksPopular(){
        return ResponseEntity.ok(bookService.getBestsellers());
    }

    @GetMapping("/recent")
    public ResponseEntity<List<BookEntity>> booksRecent(@RequestParam("from")LocalDate from, @RequestParam("to") LocalDate to){
        return ResponseEntity.ok(bookService.getRecents(from, to));
    }

    @GetMapping("/price")
    public ResponseEntity<List<BookEntity>> booksByPrice(@RequestParam("price")Integer price){
        return ResponseEntity.ok(bookService.getBooksByPrice(price));
    }

    @GetMapping("/price-between")
    public ResponseEntity<List<BookEntity>> booksByPriceBetween(@RequestParam("min")Integer min, @RequestParam("max")Integer max){
        return ResponseEntity.ok(bookService.getBooksByPriceBetween(min, max));
    }
}
