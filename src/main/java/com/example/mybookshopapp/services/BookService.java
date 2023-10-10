package com.example.mybookshopapp.services;

import com.example.mybookshopapp.data.*;
import com.example.mybookshopapp.repositories.Book2UserRepository;
import com.example.mybookshopapp.repositories.Book2UserTypeRepository;
import com.example.mybookshopapp.repositories.BookRepository;
import com.example.mybookshopapp.security.BookstoreUserRegister;
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
    private final Book2UserRepository book2UserRepository;
    private final BookstoreUserRegister userRegister;

    public List<BookEntity> getPageOfRecommendedBooks(Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return setBookStatus(bookRepository.findAll(nextPage).getContent());
    }

    public List<BookEntity> getPageOfBooksByTitle(String searchWord, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return setBookStatus(bookRepository.findBookEntitiesByTitleContainingIgnoreCase(searchWord, nextPage).getContent());
    }

    public List<BookEntity> getPageOfRecentBooks(LocalDate from, LocalDate to, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return setBookStatus(bookRepository.findBooksByPubDateBetween(from, to, nextPage).getContent());
    }

    public List<BookEntity> getPageOfPopularBooks(Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return setBookStatus(bookRepository.findAllByOrderByPopularityDesc(nextPage).getContent());
    }

    public List<BookEntity> getPageOfBooksByTagSlug(String tagSlug, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return setBookStatus(bookRepository.findBooksByTagSlug(tagSlug, nextPage).getContent());
    }

    public List<BookEntity> getPageOfBooksByGenreSlug(String genreSlug, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return setBookStatus(bookRepository.findBooksByGenreSlug(genreSlug, nextPage).getContent());
    }

    public List<BookEntity> getPageOfBooksByGenreId(Integer genreId, Integer offset, Integer limit){
        Pageable nextPage = PageRequest.of(offset, limit);
        return setBookStatus(bookRepository.findBooksByGenreId(genreId, nextPage).getContent());
    }

    public List<BookEntity> getPageOfBooksByAuthorSlug(String authorSlug, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return setBookStatus(bookRepository.findBooksByAuthorSlug(authorSlug, nextPage).getContent());
    }

    public List<BookEntity> getPageOfBooksByAuthorId(Integer authorId, Integer offset, Integer limit){
        Pageable nextPage = PageRequest.of(offset, limit);
        return setBookStatus(bookRepository.findBooksByAuthorId(authorId, nextPage).getContent());
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

    public List<BookEntity> getPageOfBooksByUserStatus(Integer userId, String status, Integer offset, Integer limit) {
        Book2UserTypeEntity book2UserType = book2UserTypeRepository.findBook2UserTypeEntityByCodeEqualsIgnoreCase(status);
        Pageable nextPage = PageRequest.of(offset, limit);
        return setBookStatus(bookRepository.findBooksByUserType(userId, book2UserType.getId(), nextPage).getContent());
    }

    public Long getCountOfBooksByUserStatus(Integer userId, String status) {
        Book2UserTypeEntity book2UserType = book2UserTypeRepository.findBook2UserTypeEntityByCodeEqualsIgnoreCase(status);
        if (book2UserType != null) {
            return bookRepository.getCountOfBooksByUserType(userId, book2UserType.getId());
        }
        return 0L;
    }

    public List<BookEntity> getBooksByIds(Integer[] bookIds) {
        return setBookStatus(bookRepository.findBookEntitiesByIdIn(bookIds));
    }

    public List<BookEntity> setBookStatus(List<BookEntity> books) {
        UserEntity user = (UserEntity) userRegister.getCurrentUser();
        if (user != null) {
            Book2UserIdEntity id = new Book2UserIdEntity();
            id.setUserId(user.getId());
            for (BookEntity book : books) {
                id.setBookId(book.getId());
                Book2UserEntity book2user = book2UserRepository.findBook2UserEntityById(id);
                if (book2user != null) {
                    book.setStatus(book2user.getType().getCode());
                }
            }
        }
        return books;
    }
}
