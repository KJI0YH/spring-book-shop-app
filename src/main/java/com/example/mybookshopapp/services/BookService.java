package com.example.mybookshopapp.services;

import com.example.mybookshopapp.data.Book2UserTypeEntity;
import com.example.mybookshopapp.data.BookEntity;
import com.example.mybookshopapp.repositories.Book2UserTypeRepository;
import com.example.mybookshopapp.repositories.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BookService {

    private final BookRepository bookRepository;
    private final Book2UserTypeRepository book2UserTypeRepository;

    public Page<BookEntity> getPageOfRecommendedBooks(Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findAll(nextPage);
    }

    public Page<BookEntity> getPageOfBooksByTitle(String searchWord, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findBookEntitiesByTitleContainingIgnoreCase(searchWord, nextPage);
    }

    public Page<BookEntity> getPageOfRecentBooks(LocalDate from, LocalDate to, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findBooksByPubDateBetween(from, to, nextPage);
    }

    public Page<BookEntity> getPageOfPopularBooks(Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findAllByOrderByPopularityDesc(nextPage);
    }

    public Page<BookEntity> getPageOfBooksByTagSlug(String tagSlug, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findBooksByTagSlug(tagSlug, nextPage);
    }

    public Page<BookEntity> getPageOfBooksByGenreSlug(String genreSlug, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findBooksByGenreSlug(genreSlug, nextPage);
    }

    public Page<BookEntity> getPageOfBooksByAuthorSlug(String authorSlug, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findBooksByAuthorSlug(authorSlug, nextPage);
    }

    public BookEntity getBookBySlug(String slug) {
        return bookRepository.findBookEntityBySlug(slug);
    }

    public List<BookEntity> getBooksByUserStatus(Integer userId, String status) {
        Book2UserTypeEntity book2UserType = book2UserTypeRepository.findBook2UserTypeEntityByCodeEqualsIgnoreCase(status);
        if (book2UserType != null) {
            return bookRepository.findBooksByUserType(userId, book2UserType.getId());
        }
        return new ArrayList<>();
    }

    public Page<BookEntity> getPageOfBooksByUserStatus(Integer userId, String status, Integer offset, Integer limit){
        Book2UserTypeEntity book2UserType = book2UserTypeRepository.findBook2UserTypeEntityByCodeEqualsIgnoreCase(status);
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findBooksByUserType(userId, book2UserType.getId(), nextPage);
    }

    public Long getCountOfBooksByUserStatus(Integer userId, String status){
        Book2UserTypeEntity book2UserType = book2UserTypeRepository.findBook2UserTypeEntityByCodeEqualsIgnoreCase(status);
        if (book2UserType != null) {
            return bookRepository.getCountOfBooksByUserType(userId, book2UserType.getId());
        }
        return 0L;
    }

    public List<BookEntity> getBooksByIds(Integer[] bookIds) {
        return bookRepository.findBookEntitiesByIdIn(bookIds);
    }
}
