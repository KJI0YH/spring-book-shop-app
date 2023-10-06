package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.data.ApiResponse;
import com.example.mybookshopapp.data.UserEntity;
import com.example.mybookshopapp.dto.BookRateDto;
import com.example.mybookshopapp.dto.BookReviewDto;
import com.example.mybookshopapp.dto.ReviewLikeDto;
import com.example.mybookshopapp.security.BookstoreUserRegister;
import com.example.mybookshopapp.services.BookRateService;
import com.example.mybookshopapp.services.BookReviewRateService;
import com.example.mybookshopapp.services.BookReviewService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApiBookRateController {

    private final BookRateService bookRateService;
    private final BookReviewService bookReviewService;
    private final BookReviewRateService bookReviewRateService;
    private final BookstoreUserRegister userRegister;

    @PostMapping("/rateBook")
    @ResponseBody
    public ResponseEntity<ApiResponse> rateBook(@RequestBody BookRateDto bookRateDto) throws JsonProcessingException {
        if (bookRateDto == null || bookRateDto.getBookId() == null || bookRateDto.getValue() == null){
            return ResponseEntity
                    .badRequest()
                    .body(new ApiResponse(HttpStatus.BAD_REQUEST, false, "Invalid book rate parameters"));
        }

        UserEntity user = (UserEntity) userRegister.getCurrentUser();
        if (user == null) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse(HttpStatus.FORBIDDEN, false, "To rate the book you need to log in"));
        }
        bookRateService.rateBook(bookRateDto.getBookId(), user.getId(), bookRateDto.getValue());
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK, true));
    }

    @PostMapping("/bookReview")
    @ResponseBody
    public ResponseEntity<ApiResponse> bookReview(@RequestBody BookReviewDto bookReviewDto){
        if (bookReviewDto == null || bookReviewDto.getBookId() == null || bookReviewDto.getText() == null){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(HttpStatus.BAD_REQUEST, false, "Invalid book review parameters"));
        }

        UserEntity user = (UserEntity) userRegister.getCurrentUser();
        if (user == null){
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse(HttpStatus.FORBIDDEN, false, "To review the book you need to log in"));
        }

        bookReviewService.reviewBook(bookReviewDto.getBookId(), user.getId(), bookReviewDto.getText());
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK, true));
    }

    @PostMapping("/rateBookReview")
    public ResponseEntity<ApiResponse> rateBookReview(@RequestBody ReviewLikeDto reviewLikeDto){
        if (reviewLikeDto == null || reviewLikeDto.getReviewId() == null || reviewLikeDto.getValue() == null){
            return ResponseEntity
                    .badRequest()
                    .body(new ApiResponse(HttpStatus.BAD_REQUEST, false, "Invalid review rate parameters"));
        }

        UserEntity user = (UserEntity) userRegister.getCurrentUser();
        if (user == null){
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse(HttpStatus.FORBIDDEN, false, "To rate the review you need to log in"));
        }
        bookReviewRateService.rateReview(reviewLikeDto.getReviewId(), user.getId(), reviewLikeDto.getValue());
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK, true));
    }
}
