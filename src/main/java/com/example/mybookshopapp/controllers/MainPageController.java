package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.data.BookEntity;
import com.example.mybookshopapp.data.TagEntity;
import com.example.mybookshopapp.dto.SearchWordDto;
import com.example.mybookshopapp.dto.TagDto;
import com.example.mybookshopapp.services.BookService;
import com.example.mybookshopapp.services.TagService;
import com.example.mybookshopapp.services.TagsPopularityService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MainPageController extends AbstractHeaderFooterController {

    private final BookService bookService;
    private final TagService tagService;
    private final TagsPopularityService tagsPopularityService;

    @ModelAttribute("recommendedBooks")
    public List<BookEntity> recommendedBooks() {
        return bookService.getPageOfRecommendedBooks(0, 6);
    }

    @ModelAttribute("recentBooks")
    public List<BookEntity> recentBooks() {
        return bookService.getPageOfRecentBooks(null, null, 0, 6);
    }

    @ModelAttribute("popularBooks")
    public List<BookEntity> popularBooks() {
        return bookService.getPageOfPopularBooks(0, 6);
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
        return tagDtos;
    }

    @GetMapping("/")
    public String mainPage() {
        return "index";
    }

    @GetMapping(value = {"/search", "/search/{searchWord}"})
    public String getSearchResults(@PathVariable(value = "searchWord", required = false) SearchWordDto searchWordDto, Model model) {
        model.addAttribute("searchWordDto", searchWordDto);
        model.addAttribute("searchResults", bookService.getPageOfBooksByTitle(searchWordDto.getExample(), 0, 20));
        return "search/index";
    }
}
