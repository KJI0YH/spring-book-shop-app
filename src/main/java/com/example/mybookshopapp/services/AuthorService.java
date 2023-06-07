package com.example.mybookshopapp.services;

import com.example.mybookshopapp.repositories.AuthorRepository;
import com.example.mybookshopapp.data.AuthorEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.stream.Collectors.groupingBy;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public List<AuthorEntity> getAuthorsData(){
        return authorRepository.findAll();
    }

    public Map<String, List<AuthorEntity>> getAuthorsMap() {
        List<AuthorEntity> authors = getAuthorsData();
        return authors.stream().collect(groupingBy((AuthorEntity o) -> o.getLastName().toUpperCase().substring(0,1)));
    }
}
