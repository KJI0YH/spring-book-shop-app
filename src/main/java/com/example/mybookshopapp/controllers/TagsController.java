package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.data.BookEntity;
import com.example.mybookshopapp.data.TagEntity;
import com.example.mybookshopapp.dto.BooksPageDto;
import com.example.mybookshopapp.dto.SearchWordDto;
import com.example.mybookshopapp.services.BookService;
import com.example.mybookshopapp.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/tags/{id}")
public class TagsController {

    private final BookService bookService;
    private final TagService tagService;

    @Autowired
    public TagsController(BookService bookService, TagService tagService) {
        this.bookService = bookService;
        this.tagService = tagService;
    }

    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto(){
        return new SearchWordDto();
    }

    @ModelAttribute("booksList")
    public List<BookEntity> booksByTag(@PathVariable("id") Integer id) {
        return bookService.getPageOfBooksByTag(id, 0, 20).getContent();
    }

    @ModelAttribute("tag")
    public TagEntity tag(@PathVariable("id") Integer id){
        Optional<TagEntity> tagEntity = tagService.getTagById(id);
        return tagEntity.orElseGet(TagEntity::new);
    }

    @GetMapping
    public String tagPage(@PathVariable("id") Integer id){
        return "/tags/index";
    }
}
