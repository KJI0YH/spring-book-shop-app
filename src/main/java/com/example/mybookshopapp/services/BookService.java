package com.example.mybookshopapp.services;

import com.example.mybookshopapp.repositories.BookRepository;
import com.example.mybookshopapp.data.BookEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<BookEntity> getBooksData() {
        return bookRepository.findAll();
    }
}
