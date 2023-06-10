package com.example.mybookshopapp.services;

import com.example.mybookshopapp.repositories.BookRepository;
import com.example.mybookshopapp.data.BookEntity;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.net.ContentHandler;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

    public Page<BookEntity> getPageOfRecommendedBooks(Integer offset, Integer limit){
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findAll(nextPage);
    }

    public Page<BookEntity> getPageOfSearchResultBooks(String searchWord, Integer offset, Integer limit){
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findBookEntitiesByTitleContainingIgnoreCase(searchWord, nextPage);
    }

    public Page<BookEntity> getPageOfRecentBooks(LocalDate from, LocalDate to, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findBooksByPubDateBetween(from, to, nextPage);
    }

    public Page<BookEntity> getPageOfPopularBooks(Integer offset, Integer limit){
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findPopularBooks(nextPage);
    }
}
