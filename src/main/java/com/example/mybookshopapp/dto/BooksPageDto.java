package com.example.mybookshopapp.dto;

import com.example.mybookshopapp.data.BookEntity;
import lombok.Data;

import java.util.List;

@Data
public class BooksPageDto {

    private Integer count;
    private List<BookEntity> books;

    public BooksPageDto(List<BookEntity> books) {
        this.books = books;
        this.count = books.size();
    }
}
