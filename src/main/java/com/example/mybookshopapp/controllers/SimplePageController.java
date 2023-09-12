package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.data.UserEntity;
import com.example.mybookshopapp.dto.SearchWordDto;
import com.example.mybookshopapp.security.BookstoreUserRegister;
import com.example.mybookshopapp.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;

@Controller
public class SimplePageController extends AbstractHeaderFooterController{
    private final BookstoreUserRegister userRegister;
    private final BookService bookService;
    private final AuthorService authorService;
    private final GenreService genreService;
    private final TagService tagService;

    @Autowired
    public SimplePageController(BookstoreUserRegister userRegister, BookService bookService, AuthorService authorService, GenreService genreService, TagService tagService) {
        this.userRegister = userRegister;
        this.bookService = bookService;
        this.authorService = authorService;
        this.genreService = genreService;
        this.tagService = tagService;
    }

    @GetMapping("/about")
    public String aboutPage() {
        return "about";
    }

    @GetMapping("/contacts")
    public String contactsPage() {
        return "contacts";
    }

    @GetMapping("/documents")
    public String documentPage() {
        return "documents/index";
    }

    @GetMapping("/faq")
    public String faqPage() {
        return "faq";
    }

    @GetMapping("/books/recent")
    public String recentBooksPage(Model model) {
        model.addAttribute("booksList", bookService.getPageOfRecentBooks(LocalDate.now().minusMonths(1), LocalDate.now(), 0, 20).getContent());
        return "books/recent";
    }

    @GetMapping("/books/popular")
    public String popularBooksPage(Model model) {
        model.addAttribute("booksList", bookService.getPageOfPopularBooks(0, 20).getContent());
        return "books/popular";
    }

    @GetMapping("/authors")
    public String authorsPage(Model model) {
        model.addAttribute("authorsMap", authorService.getAuthorsMap());
        return "authors/index";
    }

    @GetMapping("/authors/{authorSlug}")
    public String authorPage(@PathVariable("authorSlug") String authorSlug,
                             Model model) {
        model.addAttribute("author", authorService.getAuthorBySlug(authorSlug));
        return "authors/slug";
    }

    @GetMapping("/books/author/{authorSlug}")
    public String authorBooksPage(@PathVariable("authorSlug") String authorSlug,
                                  Model model) {
        model.addAttribute("booksList", bookService.getPageOfBooksByAuthorSlug(authorSlug, 0, 20).getContent());
        model.addAttribute("author", authorService.getAuthorBySlug(authorSlug));
        return "books/author";
    }

    @GetMapping("/genres")
    public String genresPage(Model model) {
        model.addAttribute("genres", genreService.getAllRootGenres());
        return "genres/index";
    }

    @GetMapping("/genres/{genreSlug}")
    public String genrePage(@PathVariable("genreSlug") String genreSlug,
                            Model model) {
        model.addAttribute("booksList", bookService.getPageOfBooksByGenreSlug(genreSlug, 0, 20).getContent());
        model.addAttribute("genre", genreService.getGenreBySlug(genreSlug));
        model.addAttribute("breadcrumbs", genreService.getGenresBreadcrumbs(genreSlug));
        return "genres/slug";
    }

    @GetMapping("/tags/{tagSlug}")
    public String tagPage(@PathVariable("tagSlug") String tagSlug,
                          Model model) {
        model.addAttribute("booksList", bookService.getPageOfBooksByTagSlug(tagSlug, 0, 20).getContent());
        model.addAttribute("tag", tagService.getTagBySlug(tagSlug));
        return "tags/index";
    }
}
