package com.example.mybookshopapp.services;

import com.example.mybookshopapp.dto.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BookService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Book> getBooksData() {
        List<Book> books = jdbcTemplate.query("SELECT b.id, b.title, b.price, b.discount, a.first_name, a.last_name FROM book AS b JOIN book2author AS b2a ON b2a.book_id = b.id JOIN author AS a ON b2a.author_id = a.id",
                (ResultSet rs, int rowNum) -> {
                    return Book.builder()
                            .id(rs.getInt("id"))
                            .author(rs.getString("first_name") + " " + rs.getString("last_name"))
                            .title(rs.getString("title"))
                            .price(rs.getInt("price"))
                            .discount(rs.getInt("discount"))
                            .build();
        });
        return new ArrayList<>(books);
    }
}
