package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.data.AuthorEntity;
import com.example.mybookshopapp.dto.SearchWordDto;
import com.example.mybookshopapp.security.BookstoreUserRegister;
import com.example.mybookshopapp.services.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("authors/{authorSlug}")
public class AuthorPageController {

    private final AuthorService authorService;
    private final BookstoreUserRegister userRegister;

    @Autowired
    public AuthorPageController(AuthorService authorService, BookstoreUserRegister userRegister) {
        this.authorService = authorService;
        this.userRegister = userRegister;
    }

    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }

    @ModelAttribute("author")
    public AuthorEntity author(@PathVariable("authorSlug") String authorSlug) {
        return authorService.getAuthorBySlug(authorSlug);
    }

    @ModelAttribute("curUsr")
    public Object curUsr(){
        return userRegister.getCurrentUser();
    }

    @GetMapping
    public String authorPage() {
        return "/authors/slug";
    }
}
