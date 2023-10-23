package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.dto.ApiResponse;
import com.example.mybookshopapp.errors.*;
import org.springframework.data.repository.config.RepositoryConfigurationSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandlerController {

    @ExceptionHandler({
            AuthenticationException.class,
            UserAlreadyExistException.class,
            UsernameNotFoundException.class,
            ApiWrongParameterException.class,
            ApproveContactException.class
    })
    public ResponseEntity<ApiResponse> handleApiException(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(false, e.getMessage()));
    }

    @ExceptionHandler({
            PaymentInitiateException.class,
            PaymentStatusException.class,
            FileDownloadException.class
    })
    public ResponseEntity<ApiResponse> handlePaymentException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(false, e.getMessage()));
    }

    @ExceptionHandler(BalanceNotEnoughException.class)
    public ResponseEntity<ApiResponse> handleBalanceNotEnoughException(Exception e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ApiResponse(false, e.getMessage()));
    }

    @ExceptionHandler(PaymentRequiredException.class)
    public ResponseEntity<ApiResponse> handlePaymentRequiredException(Exception e){
        return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED)
                .body(new ApiResponse(false, e.getMessage()));
    }

    @ExceptionHandler(UserUnauthorizedException.class)
    public ResponseEntity<ApiResponse> handleUserUnauthorizedException(Exception e){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse(false, e.getMessage()));
    }
}
