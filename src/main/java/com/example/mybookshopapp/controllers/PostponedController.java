package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.data.BookEntity;
import com.example.mybookshopapp.data.UserEntity;
import com.example.mybookshopapp.services.UserService;
import com.example.mybookshopapp.services.BookService;
import com.example.mybookshopapp.services.CookieService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/postponed")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PostponedController extends AbstractHeaderFooterController {

    private final BookService bookService;
    private final CookieService cookieService;
    private final UserService userService;

    @ModelAttribute(name = "bookKept")
    public List<BookEntity> bookKept() {
        return new ArrayList<>();
    }

    @ModelAttribute(name = "bookKeptString")
    public String bookKeptString() {
        return "";
    }

    @GetMapping
    public String postponedPage(@CookieValue(value = "postponedContents", required = false) String postponedContents,
                                Model model) {
        UserEntity user = (UserEntity) userService.getCurrentUser();
        List<BookEntity> booksInPostponed;

        // Authorized user
        if (user != null) {
            booksInPostponed = bookService.getAllBooksByUserStatus(user.getId(), "KEPT");
        }

        // Unauthorized user
        else {
            booksInPostponed = bookService.getBooksByIds(cookieService.getIntegerIds(postponedContents));
        }

        model.addAttribute("isPostponedEmpty", booksInPostponed.isEmpty());
        model.addAttribute("bookKept", booksInPostponed);
        model.addAttribute("bookKeptString", booksInPostponed.stream()
                .map(book -> book.getId().toString())
                .collect(Collectors.joining(",")));

        return "postponed";
    }
}
