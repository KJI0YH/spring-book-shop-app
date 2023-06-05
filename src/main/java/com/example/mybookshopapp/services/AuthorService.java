package com.example.mybookshopapp.services;

import com.example.mybookshopapp.dto.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.util.*;

import static java.util.stream.Collectors.groupingBy;

@Service
public class AuthorService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AuthorService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Author> getAuthorsData(){
        return jdbcTemplate.query("SELECT * FROM author", (ResultSet rs, int rowNum) -> {
            Author author = new Author();
            author.setId(rs.getInt("id"));
            author.setFirstName(rs.getString("first_name"));
            author.setLastName(rs.getString("last_name"));
            return author;
        });
    }

    public Map<String, List<Author>> getAuthorsMap() {
        List<Author> authors = getAuthorsData();
        return authors.stream().collect(groupingBy((Author o) -> {return o.getLastName().toUpperCase().substring(0,1);}));
    }
}
