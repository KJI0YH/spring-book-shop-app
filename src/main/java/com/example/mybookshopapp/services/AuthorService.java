package com.example.mybookshopapp.services;

import com.example.mybookshopapp.data.AuthorRepository;
import com.example.mybookshopapp.dto.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.util.*;

import static java.util.stream.Collectors.groupingBy;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public List<Author> getAuthorsData(){
        return authorRepository.findAll();
    }

    public Map<String, List<Author>> getAuthorsMap() {
        List<Author> authors = getAuthorsData();
        return authors.stream().collect(groupingBy((Author o) -> {return o.getLastName().toUpperCase().substring(0,1);}));
    }
}
