package com.example.mybookshopapp.services;

import com.example.mybookshopapp.data.Book2UserIdEntity;
import com.example.mybookshopapp.data.Book2UserViewedEntity;
import com.example.mybookshopapp.data.BookEntity;
import com.example.mybookshopapp.repositories.Book2UserViewedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BookViewedService {

    private final Book2UserViewedRepository book2UserViewedRepository;

    public List<BookEntity> getPageOfViewedBooks(Integer userId, Integer offset, Integer limit){
        Pageable nextPage = PageRequest.of(offset, limit);
        Page<Book2UserViewedEntity> result =  book2UserViewedRepository.findAllByUserIdOrderByTimeDesc(userId, nextPage);
        List<BookEntity> bookList = new ArrayList<>();
        for( Book2UserViewedEntity book2UserViewed : result){
            bookList.add(book2UserViewed.getBook());
        }
        return bookList;
    }

    public Book2UserViewedEntity setViewedBook(Integer userId, Integer bookId){
        Book2UserIdEntity id = new Book2UserIdEntity(bookId, userId);
        Book2UserViewedEntity book2UserViewed = new Book2UserViewedEntity();
        book2UserViewed.setId(id);
        book2UserViewed.setTime(LocalDateTime.now());
        return book2UserViewedRepository.save(book2UserViewed);
    }
}
