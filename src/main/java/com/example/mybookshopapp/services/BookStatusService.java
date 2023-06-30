package com.example.mybookshopapp.services;

import com.example.mybookshopapp.data.Book2UserEntity;
import com.example.mybookshopapp.data.Book2UserIdEntity;
import com.example.mybookshopapp.data.Book2UserTypeEntity;
import com.example.mybookshopapp.dto.BookCookieStoreDto;
import com.example.mybookshopapp.repositories.Book2UserRepository;
import com.example.mybookshopapp.repositories.Book2UserTypeRepository;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class BookStatusService {

    private final CartService cartService;
    private final Book2UserRepository book2UserRepository;
    private final Book2UserTypeRepository book2UserTypeRepository;

    @Autowired
    public BookStatusService(CartService cartService, Book2UserRepository book2UserRepository, Book2UserTypeRepository book2UserTypeRepository) {
        this.cartService = cartService;
        this.book2UserRepository = book2UserRepository;
        this.book2UserTypeRepository = book2UserTypeRepository;
    }

    public BookCookieStoreDto changeStatus(String status, List<String> cartBooks, List<String> postponedBooks, List<String> bookIds) {
        switch (status) {
            case "KEPT" -> {
                return setKeptStatus(cartBooks, postponedBooks, bookIds);
            }
            case "CART" -> {
                return setCartStatus(cartBooks, postponedBooks, bookIds);
            }
            case "UNLINK" -> {
                return setUnlinkStatus(cartBooks, postponedBooks, bookIds);
            }
            default -> {
                return new BookCookieStoreDto(cartBooks, postponedBooks);
            }
        }
    }

    public BookCookieStoreDto setKeptStatus(List<String> cartBooks, List<String> postponedBooks, List<String> bookIds) {
        for (String bookId : bookIds) {
            if (!postponedBooks.contains(bookId)) {
                postponedBooks.add(bookId);
            }
            if (cartBooks.contains(bookId)){
                cartBooks.remove(bookId);
            }
        }
        return new BookCookieStoreDto(cartBooks, postponedBooks);
    }

    public BookCookieStoreDto setCartStatus(List<String> cartBooks, List<String> postponedBooks, List<String> bookIds) {
        for (String bookId : bookIds) {
            if (!cartBooks.contains(bookId)) {
                cartBooks.add(bookId);
            }
            if (postponedBooks.contains(bookId)){
                postponedBooks.remove(bookId);
            }
        }
        return new BookCookieStoreDto(cartBooks, postponedBooks);
    }

    public BookCookieStoreDto setUnlinkStatus(List<String> cartBooks, List<String> postponedBooks, List<String> bookIds) {
        for (String bookId : bookIds) {
            cartBooks.remove(bookId);
            postponedBooks.remove(bookId);
        }
        return new BookCookieStoreDto(cartBooks, postponedBooks);
    }

    public BookCookieStoreDto changeBookCookiesStatus(String status,
                                                      String cartContents,
                                                      String postponedContents,
                                                      String[] bookIds) {

        ArrayList<String> cartBookIds = new ArrayList<>();
        if (cartContents != null && !cartContents.equals("")){
            cartBookIds = new ArrayList<>(Arrays.asList(cartContents.split("/")));
        }

        ArrayList<String> postponedBookIds = new ArrayList<>();
        if (postponedContents != null && !postponedContents.equals("")){
            postponedBookIds = new ArrayList<>(Arrays.asList(postponedContents.split("/")));
        }

        return changeStatus(status, cartBookIds, postponedBookIds, Arrays.stream(bookIds).toList());
    }

    public Cookie createCookieFromBookIds(List<String> bookIds, String cookieName) {
        Cookie cookie = new Cookie(cookieName, String.join("/", bookIds));
        cookie.setPath("/");
        return cookie;
    }

    public void setStatus(Integer bookId, Integer userId, String status){
        Book2UserTypeEntity book2UserType = book2UserTypeRepository.findBook2UserTypeEntityByCodeEqualsIgnoreCase(status);
        if (book2UserType != null){
            Book2UserIdEntity book2UserId = new Book2UserIdEntity();
            book2UserId.setBookId(bookId);
            book2UserId.setUserId(userId);
            Book2UserEntity book2User = new Book2UserEntity();
            book2User.setId(book2UserId);
            book2User.setTime(LocalDateTime.now());
            book2User.setType(book2UserType);
            book2UserRepository.save(book2User);
        }
    }
}
