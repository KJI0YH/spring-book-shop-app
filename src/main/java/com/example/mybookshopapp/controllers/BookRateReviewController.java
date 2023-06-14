package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.services.BookRateService;
import com.example.mybookshopapp.services.BookService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class BookRateReviewController {

    private final BookRateService bookRateService;

    @Autowired
    public BookRateReviewController(BookRateService bookRateService) {
        this.bookRateService = bookRateService;
    }

    @PostMapping("/rateBook")
    @ResponseBody
    public String rateBook(HttpServletRequest request,
                           HttpServletResponse response) throws JsonProcessingException {
        String id = request.getParameter("bookId");
        String value = request.getParameter("value");

        bookRateService.rateBook(Integer.valueOf(id), Integer.valueOf(value));

        Map<String, Object> object = new HashMap<>();
        object.put("result", true);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }

    @PostMapping("/bookReview")
    public String bookReview(HttpServletRequest request,
                             HttpServletResponse response){

        //TODO add review for authorize users
        return "";
    }

    @PostMapping("/rateBookReview")
    public String rateBookReview(HttpServletRequest request,
                                 HttpServletResponse response){

        //TODO add (dis)likes for reviews for authorize users
        return "";
    }
}
