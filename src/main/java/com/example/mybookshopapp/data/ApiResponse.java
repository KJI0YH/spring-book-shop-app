package com.example.mybookshopapp.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Collection;

@Data
public class ApiResponse {
    private HttpStatus status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime timeStamp;
    private Boolean result;
    private String error;


    public ApiResponse() {
        this.timeStamp = LocalDateTime.now();
    }

    public ApiResponse(HttpStatus status, Boolean result) {
        this();
        this.status = status;
        this.result = result;
    }

    public ApiResponse(HttpStatus status, Boolean result, String error) {
        this();
        this.status = status;
        this.result = result;
        this.error = error;
    }
}
