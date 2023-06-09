package com.example.mybookshopapp.services;

import com.example.mybookshopapp.repositories.BookRepository;
import com.example.mybookshopapp.data.BookEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    public List<BookEntity> getBooksByTitle(String title){
        return bookRepository.findBookEntitiesByTitleContainingIgnoreCase(title);
    }

    public List<BookEntity> getBooksByPriceBetween(Integer min, Integer max){
        return bookRepository.findBookEntitiesByPriceBetween(min, max);
    }

    public List<BookEntity> getBooksByPrice(Integer price){
        return bookRepository.findBookEntitiesByPriceIs(price);
    }

    public List<BookEntity> getBestsellers(){
        return bookRepository.getBestsellers();
    }

    public List<BookEntity> getRecents(LocalDate from, LocalDate to){
        return bookRepository.getRecents(from, to);
    }

    public Page<BookEntity> getPageOfRecommendedBooks(Integer offset, Integer limit){
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findAll(nextPage);
    }
}
