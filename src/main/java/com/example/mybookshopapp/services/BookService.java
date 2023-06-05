package com.example.mybookshopapp.services;

import com.example.mybookshopapp.dto.Author;
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
        List<Book> books = jdbcTemplate.query("SELECT* FROM book",
                (ResultSet rs, int rowNum) -> {
            Book book = new Book();
            book.setId(rs.getInt("id"));
            book.setAuthor(getAuthorById(rs.getString("author_id")));
            book.setTitle(rs.getString("title"));
            book.setPrice(rs.getInt("price"));
            book.setDiscount(rs.getInt("discount"));
            return book;
        });
        return new ArrayList<>(books);
    }

    private String getAuthorById(String authorId) {
        List<Author> authors = jdbcTemplate.query("SELECT * FROM author WHERE author.id = " + authorId, (ResultSet rs, int rowNum) -> {
            Author author = new Author();
            author.setId(rs.getInt("id"));
            author.setFirstName(rs.getString("first_name"));
            author.setLastName(rs.getString("last_name"));
            return author;
        });
        return authors.get(0).toString();
    }
}
