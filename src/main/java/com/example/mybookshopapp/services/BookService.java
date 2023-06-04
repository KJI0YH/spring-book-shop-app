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
        List<Book> books = jdbcTemplate.query("SELECT b.id, b.title, b.price, b.discount, a.first_name, a.last_name FROM BOOK AS b JOIN book2author AS b2a JOIN author AS a WHERE b2a.book_id = b.id AND a.id = b2a.author_id",
                (ResultSet rs, int rowNum) -> {
            Book book = new Book();
            book.setId(rs.getInt("id"));
            book.setAuthor(rs.getString("first_name") + " " + rs.getString("last_name"));
            book.setTitle(rs.getString("title"));
            book.setPrice(rs.getInt("price"));
            book.setDiscount(rs.getInt("discount"));
            return book;
        });
        return new ArrayList<>(books);
    }
}
