package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.data.BookEntity;
import com.example.mybookshopapp.data.UserEntity;
import com.example.mybookshopapp.services.BookService;
import com.example.mybookshopapp.services.CookieService;
import com.example.mybookshopapp.services.UserService;
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

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CartController extends AbstractHeaderFooterController {

    private final BookService bookService;
    private final CookieService cookieService;
    private final UserService userService;

    @ModelAttribute(name = "bookCart")
    public List<BookEntity> bookCart() {
        return new ArrayList<>();
    }

    @ModelAttribute("cartPrice")
    public Long cartPrice() {
        return 0L;
    }

    @ModelAttribute("cartPriceOld")
    public Long cartPriceOld() {
        return 0L;
    }

    @GetMapping
    public String handleCartRequest(@CookieValue(value = "cartContents", required = false) String cartContents,
                                    Model model) {
        UserEntity user = userService.getCurrentUser();
        List<BookEntity> booksInCart;

        // Authorized user
        if (user != null) {
            booksInCart = bookService.getAllBooksByUserStatus(user.getId(), "CART");
        }

        // Unauthorized user
        else {
            booksInCart = bookService.getBooksByIds(cookieService.getIntegerIds(cartContents));
        }

        model.addAttribute("isCartEmpty", booksInCart.isEmpty());
        model.addAttribute("bookCart", booksInCart);
        model.addAttribute("cartPrice", booksInCart.stream().mapToLong(BookEntity::getDiscountPrice).sum());
        model.addAttribute("cartPriceOld", booksInCart.stream().mapToLong(BookEntity::getPrice).sum());

        return "cart";
    }
}
