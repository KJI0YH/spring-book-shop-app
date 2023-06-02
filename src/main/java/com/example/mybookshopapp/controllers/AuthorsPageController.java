package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.dto.Author;
import com.example.mybookshopapp.services.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Controller
@RequestMapping("/bookshop/authors")
public class AuthorsPageController {

    @Autowired
    private AuthorService authorService;

    public AuthorsPageController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    public String authorsPage(Model model){

        List<Author> authors = authorService.getAuthorsData();

        // Sort the authors
        Collections.sort(authors);

        // Group the authors by first letter of the surname
        Map<Character, List<Author>> groupedAuthors = new HashMap<>();
        for (Author author : authors) {
            char firstLetter = author.getSurname().toUpperCase().charAt(0);
            groupedAuthors.computeIfAbsent(firstLetter, k -> new ArrayList<>()).add(author);
        }

        model.addAttribute("authorData", groupedAuthors);
        return "authors/index";
    }
}
