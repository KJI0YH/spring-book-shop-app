package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.data.BookEntity;
import com.example.mybookshopapp.data.UserEntity;
import com.example.mybookshopapp.dto.SearchWordDto;
import com.example.mybookshopapp.repositories.BookRepository;
import com.example.mybookshopapp.security.BookstoreUserRegister;
import com.example.mybookshopapp.services.BookService;
import com.example.mybookshopapp.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final BookService bookService;
    private final CartService cartService;
    private final BookstoreUserRegister userRegister;

    @Autowired
    public CartController(BookService bookService, CartService cartService, BookstoreUserRegister userRegister) {
        this.bookService = bookService;
        this.cartService = cartService;
        this.userRegister = userRegister;
    }

    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }

    @ModelAttribute(name = "bookCart")
    public List<BookEntity> bookCart() {
        return new ArrayList<>();
    }

    @ModelAttribute("curUsr")
    public Object curUsr(){
        return userRegister.getCurrentUser();
    }

    @GetMapping
    public String handleCartRequest(@CookieValue(value = "cartContents", required = false) String cartContents,
                                    Model model) {

        // Authorized user
        UserEntity user = (UserEntity) userRegister.getCurrentUser();
        if (user != null){
            List<BookEntity> booksInCart = bookService.getBooksByUserStatus(user.getId(), "CART");

            if (booksInCart.size() > 0){
                model.addAttribute("isCartEmpty", false);
            } else {
                model.addAttribute("isCartEmpty", true);
            }
            model.addAttribute("bookCart", booksInCart);
            return "cart";
        }

        // Unauthorized user
        if (cartContents == null || cartContents.equals("")) {
            model.addAttribute("isCartEmpty", true);
        } else {
            model.addAttribute("isCartEmpty", false);
            String[] cookieIds = cartService.getCookiesIds(cartContents);
            List<BookEntity> booksFromCookiesIds = bookService.getBooksByIds(Arrays.stream(cookieIds).map(Integer::valueOf).toArray(Integer[]::new));
            model.addAttribute("bookCart", booksFromCookiesIds);
        }
        return "cart";
    }
}
