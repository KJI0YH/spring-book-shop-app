package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.data.BookEntity;
import com.example.mybookshopapp.dto.SearchWordDto;
import com.example.mybookshopapp.dto.TagDto;
import com.example.mybookshopapp.services.BookRecommendedService;
import com.example.mybookshopapp.services.BookSearchService;
import com.example.mybookshopapp.services.BookService;
import com.example.mybookshopapp.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MainPageController extends AbstractHeaderFooterController {

    private final BookService bookService;
    private final TagService tagService;
    private final BookSearchService bookSearchService;
    private final BookRecommendedService bookRecommendedService;

    @ModelAttribute("recommendedBooks")
    public List<BookEntity> recommendedBooks() {
        return bookRecommendedService.getPageOfRecommendedBooks(0, 6);
    }

    @ModelAttribute("recentBooks")
    public List<BookEntity> recentBooks() {
        return bookService.getPageOfRecentBooks(null, null, 0, 6);
    }

    @ModelAttribute("popularBooks")
    public List<BookEntity> popularBooks() {
        return bookService.getPageOfPopularBooks(0, 6);
    }

    @ModelAttribute("tags")
    public List<TagDto> tags() {
        return tagService.getTagsWithPopularityClasses();
    }

    @GetMapping("/")
    public String mainPage() {
        return "index";
    }

    @GetMapping(value = {"/search", "/search/{searchWord}"})
    public String getSearchResults(@PathVariable(value = "searchWord", required = false) String searchWord,
                                   Model model) {
        List<Integer> bookIds = bookSearchService.getBooksIdsByQuery(searchWord);
        model.addAttribute("searchWordDto", new SearchWordDto(searchWord));
        model.addAttribute("booksFound", bookIds.size());
        model.addAttribute("booksList", bookSearchService.getPageOfBooks(bookIds, 0, 20));
        return "search/index";
    }
}
