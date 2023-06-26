package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.data.ApiResponse;
import com.example.mybookshopapp.data.UserEntity;
import com.example.mybookshopapp.dto.BookRateDto;
import com.example.mybookshopapp.dto.BookReviewDto;
import com.example.mybookshopapp.security.BookstoreUserRegister;
import com.example.mybookshopapp.services.BookRateService;
import com.example.mybookshopapp.services.BookReviewService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class BookRateReviewController {

    private final BookRateService bookRateService;
    private final BookReviewService bookReviewService;
    private final BookstoreUserRegister userRegister;

    @Autowired
    public BookRateReviewController(BookRateService bookRateService, BookReviewService bookReviewService, BookstoreUserRegister userRegister) {
        this.bookRateService = bookRateService;
        this.bookReviewService = bookReviewService;
        this.userRegister = userRegister;
    }

    @PostMapping("/rateBook")
    @ResponseBody
    public ResponseEntity<ApiResponse> rateBook(@RequestBody BookRateDto bookRateDto) throws JsonProcessingException {
        if (bookRateDto == null){
            return ResponseEntity
                    .badRequest()
                    .body(new ApiResponse(HttpStatus.BAD_REQUEST, false, "Invalid book rate parameters"));
        }

        UserEntity user = (UserEntity) userRegister.getCurrentUser();
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
    @ResponseBody
    public ResponseEntity<ApiResponse> bookReview(@RequestBody BookReviewDto bookReviewDto){

        if (bookReviewDto == null){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(HttpStatus.BAD_REQUEST, false, "Invalid book review parameters"));
        }

        UserEntity user = (UserEntity) userRegister.getCurrentUser();
        if (user == null){
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse(HttpStatus.FORBIDDEN, false, "To review the book you need to log in"));
        } else {
            bookReviewService.reviewBook(bookReviewDto.getBookId(), user.getId(), bookReviewDto.getText());
            return ResponseEntity.ok(new ApiResponse(HttpStatus.OK, true));
        }
    }

    @PostMapping("/rateBookReview")
    public String rateBookReview(HttpServletRequest request,
                                 HttpServletResponse response){

        //TODO add (dis)likes for reviews for authorize users
        return "";
    }
}
