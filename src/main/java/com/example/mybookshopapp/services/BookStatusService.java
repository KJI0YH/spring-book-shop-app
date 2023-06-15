package com.example.mybookshopapp.services;

import com.example.mybookshopapp.dto.BookCookieStoreDto;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class BookStatusService {

    private final CartService cartService;

    @Autowired
    public BookStatusService(CartService cartService) {
        this.cartService = cartService;
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
}
