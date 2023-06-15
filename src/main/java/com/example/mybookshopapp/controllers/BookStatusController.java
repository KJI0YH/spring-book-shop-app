package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.dto.BookCookieStoreDto;
import com.example.mybookshopapp.services.BookStatusService;
import com.example.mybookshopapp.services.CartService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api")
public class BookStatusController {

    private final CartService cartService;
    private final BookStatusService bookStatusService;

    @Autowired
    public BookStatusController(CartService cartService, BookStatusService bookStatusService) {
        this.cartService = cartService;
        this.bookStatusService = bookStatusService;
    }

    @PostMapping("/changeBookStatus")
    @ResponseBody
    public String handleChangeBookStatus(HttpServletRequest request,
                                         HttpServletResponse response,
                                         @CookieValue(value = "cartContents", required = false) String cartContents,
                                         @CookieValue(value = "postponedContents", required = false) String postponedContents) throws JsonProcessingException {

        //TODO: change status for registered users in db

        Map<String, String[]> params = request.getParameterMap();

        String status = request.getParameter("status");
        String[] booksIds = params.get("booksIds[]");

        BookCookieStoreDto bookCookieStore = bookStatusService.changeBookCookiesStatus(status, cartContents, postponedContents, booksIds);
        response.addCookie(bookStatusService.createCookieFromBookIds(bookCookieStore.getCartContents(), "cartContents"));
        response.addCookie(bookStatusService.createCookieFromBookIds(bookCookieStore.getPostponedContents(), "postponedContents"));

        Map<String, Object> object = new HashMap<>();
        object.put("result", true);

        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
}
