package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.data.ApiResponse;
import com.example.mybookshopapp.data.UserEntity;
import com.example.mybookshopapp.dto.BookRateDto;
import com.example.mybookshopapp.security.BookstoreUserRegister;
import com.example.mybookshopapp.services.BookRateService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class BookRateReviewController {

    private final BookRateService bookRateService;
    private final BookstoreUserRegister userRegister;

    @Autowired
    public BookRateReviewController(BookRateService bookRateService, BookstoreUserRegister userRegister) {
        this.bookRateService = bookRateService;
        this.userRegister = userRegister;
    }

    @PostMapping("/rateBook")
    @ResponseBody
    public ResponseEntity<ApiResponse> rateBook(@RequestBody BookRateDto bookRateDto) throws JsonProcessingException {
        UserEntity user = (UserEntity) userRegister.getCurrentUser();

        if (bookRateDto == null){
            return ResponseEntity
                    .badRequest()
                    .body(new ApiResponse(HttpStatus.BAD_REQUEST, false, "Invalid book rate parameters"));
        }

        if (user == null){
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse(HttpStatus.FORBIDDEN, false, "To rate the book you need to log in"));
        } else {
            bookRateService.rateBook(bookRateDto.getBookId(), user.getId(), bookRateDto.getValue());
            return ResponseEntity.ok(new ApiResponse(HttpStatus.OK, true));
        }
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
