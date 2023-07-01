package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.data.BookEntity;
import com.example.mybookshopapp.data.UserEntity;
import com.example.mybookshopapp.dto.SearchWordDto;
import com.example.mybookshopapp.repositories.BookRepository;
import com.example.mybookshopapp.security.BookstoreUserRegister;
import com.example.mybookshopapp.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/postponed")
public class PostponedController {

    private final BookService bookService;
    private final BookstoreUserRegister userRegister;

    @Autowired
    public PostponedController(BookService bookService, BookstoreUserRegister userRegister) {
        this.bookService = bookService;
        this.userRegister = userRegister;
    }

    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto(){
        return new SearchWordDto();
    }

    @ModelAttribute("curUsr")
    public Object curUsr(){
        return userRegister.getCurrentUser();
    }

    @ModelAttribute(name = "bookKept")
    public List<BookEntity> bookKept() {
        return new ArrayList<>();
    }

    @ModelAttribute(name = "bookKeptString")
    public String bookKeptString(){
        return "";
    }

    @GetMapping
    public String postponedPage(@CookieValue(value = "postponedContents", required = false) String postponedContents,
                                Model model){

        // Authorized user
        UserEntity user = (UserEntity) userRegister.getCurrentUser();
        if (user != null){
            List<BookEntity> booksInPostponed = bookService.getBooksByUserStatus(user.getId(), "KEPT");

            if (booksInPostponed.size() > 0){
                model.addAttribute("isPostponedEmpty", false);
            } else {
                model.addAttribute("isPostponedEmpty", true);
            }
            model.addAttribute("bookKept", booksInPostponed);
            return "postponed";
        }

        // Unauthorized user
        if (postponedContents == null || postponedContents.equals("")) {
            model.addAttribute("isPostponedEmpty", true);
        } else {
            model.addAttribute("isPostponedEmpty", false);
            postponedContents = postponedContents.startsWith("/") ? postponedContents.substring(1) : postponedContents;
            postponedContents = postponedContents.endsWith("/") ? postponedContents.substring(0, postponedContents.length() - 1) : postponedContents;
            String[] postponedItems = postponedContents.split("/");
            Integer[] cookieIds = Arrays.stream(postponedItems)
                    .map(Integer::valueOf)
                    .toArray(Integer[]::new);
            List<BookEntity> booksFromCookiesIds = bookService.getBooksByIds(cookieIds);
            model.addAttribute("bookKept", booksFromCookiesIds);
            model.addAttribute("bookKeptString", String.join(",", postponedItems));
        }
        return "postponed";
    }
}
