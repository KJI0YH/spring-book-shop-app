package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.data.BookEntity;
import com.example.mybookshopapp.data.TagEntity;
import com.example.mybookshopapp.dto.SearchWordDto;
import com.example.mybookshopapp.dto.TagDto;
import com.example.mybookshopapp.security.BookstoreUserRegister;
import com.example.mybookshopapp.services.BookService;
import com.example.mybookshopapp.services.TagService;
import com.example.mybookshopapp.services.TagsPopularityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MainPageController {

    private final BookService bookService;
    private final TagService tagService;
    private final TagsPopularityService tagsPopularityService;
    private final BookstoreUserRegister userRegister;

    @Autowired
    public MainPageController(BookService bookService, TagService tagService, TagsPopularityService tagsPopularityService, BookstoreUserRegister userRegister) {
        this.bookService = bookService;
        this.tagService = tagService;
        this.tagsPopularityService = tagsPopularityService;
        this.userRegister = userRegister;
    }

    @ModelAttribute("recommendedBooks")
    public List<BookEntity> recommendedBooks() {
        return bookService.getPageOfRecommendedBooks(0, 6).getContent();
    }

    @ModelAttribute("recentBooks")
    public List<BookEntity> recentBooks() {
        return bookService.getPageOfRecentBooks(null, null, 0, 6).getContent();
    }

    @ModelAttribute("popularBooks")
    public List<BookEntity> popularBooks() {
        return bookService.getPageOfPopularBooks(0, 6).getContent();
    }

    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }

    @ModelAttribute("curUsr")
    public Object curUsr(){
        return userRegister.getCurrentUser();
    }

    @ModelAttribute("searchResults")
    public List<BookEntity> searchResults() {
        return new ArrayList<>();
    }

    @ModelAttribute("tags")
    public List<TagDto> tags() {
        List<TagEntity> tagEntities = tagService.getAllTags();
        List<TagDto> tagDtos = new ArrayList<TagDto>();
        for (TagEntity tagEntity : tagEntities){
            tagDtos.add(new TagDto(tagEntity, tagsPopularityService.getPopularityTag(tagEntity)));
        }
        return  tagDtos;
    }

    @GetMapping("/")
    public String mainPage() {
        return "index";
    }

    @GetMapping(value = {"/search", "/search/{searchWord}"})
    public String getSearchResults(@PathVariable(value = "searchWord", required = false) SearchWordDto searchWordDto, Model model) {
        model.addAttribute("searchWordDto", searchWordDto);
        model.addAttribute("searchResults", bookService.getPageOfBooksByTitle(searchWordDto.getExample(), 0, 20).getContent());
        return "search/index";
    }
}
