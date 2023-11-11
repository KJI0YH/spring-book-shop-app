package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.data.UserEntity;
import com.example.mybookshopapp.dto.MessageDto;
import com.example.mybookshopapp.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CommonPageController extends AbstractHeaderFooterController {
    private final UserService userService;
    private final BookService bookService;
    private final AuthorService authorService;
    private final GenreService genreService;
    private final TagService tagService;
    private final DocumentService documentService;
    private final FaqService faqService;
    private final CookieService cookieService;

    @Value("${upload.default-book-cover}")
    private String defaultBookImage;

    @Value("${upload.default-author-cover}")
    private String defaultAuthorImage;

    @GetMapping("/about")
    public String aboutPage() {
        return "about";
    }

    @GetMapping("/contacts")
    public String contactsPage(Model model) {
        model.addAttribute("message", new MessageDto());
        return "contacts";
    }

    @GetMapping("/documents")
    public String documentsPage(Model model) {
        model.addAttribute("documents", documentService.getDocuments());
        return "documents/index";
    }

    @GetMapping("/documents/{documentSlug}")
    public String documentPage(@PathVariable("documentSlug") String documentSlug,
                               Model model) {
        model.addAttribute("document", documentService.getDocumentBySlug(documentSlug));
        return "documents/slug";
    }

    @GetMapping("/faq")
    public String faqPage(Model model) {
        model.addAttribute("faqs", faqService.getFaqs());
        return "faq";
    }

    @GetMapping("/books/recent")
    public String recentBooksPage(Model model) {
        model.addAttribute("booksList", bookService.getPageOfRecentBooks(LocalDate.now().minusMonths(1), LocalDate.now(), 0, 20));
        return "books/recent";
    }

    @GetMapping("/books/popular")
    public String popularBooksPage(Model model) {
        model.addAttribute("booksList", bookService.getPageOfPopularBooks(0, 20));
        return "books/popular";
    }

    @GetMapping("/books/viewed")
    public String viewedBooksPage(@CookieValue(value = "viewedContents", required = false) String viewedContents,
                                  Model model) {
        UserEntity user = userService.getCurrentUser();
        if (user != null) {
            model.addAttribute("booksList", bookService.getPageOfViewedBooks(user.getId(), 0, 20));
        } else {
            model.addAttribute("booksList", bookService.getBooksByIds(cookieService.getIntegerIds(viewedContents)));
        }
        return "books/viewed";
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
        model.addAttribute("booksList", bookService.getPageOfBooksByAuthorSlug(authorSlug, 0, 20));
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
        model.addAttribute("booksList", bookService.getPageOfBooksByGenreSlug(genreSlug, 0, 20));
        model.addAttribute("genre", genreService.getGenreBySlug(genreSlug));
        model.addAttribute("breadcrumbs", genreService.getGenresBreadcrumbs(genreSlug));
        if (userService.getCurrentUser().isAdmin()) {
            model.addAttribute("genres", genreService.getAllGenres());
        }
        return "genres/slug";
    }

    @GetMapping("/tags/{tagSlug}")
    public String tagPage(@PathVariable("tagSlug") String tagSlug,
                          Model model) {
        model.addAttribute("booksList", bookService.getPageOfBooksByTagSlug(tagSlug, 0, 20));
        model.addAttribute("tag", tagService.getTagBySlug(tagSlug));
        return "tags/index";
    }

    @GetMapping("/cms")
    public String cmsPage(Model model) {
        model.addAttribute("tags", tagService.getAllTags());
        model.addAttribute("genres", genreService.getAllGenres());
        model.addAttribute("authors", authorService.getAllAuthors());
        model.addAttribute("defaultBookImage", defaultBookImage);
        model.addAttribute("defaultAuthorImage", defaultAuthorImage);
        return "cms";
    }
}
