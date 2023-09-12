package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.data.UserEntity;
import com.example.mybookshopapp.dto.PaymentDto;
import com.example.mybookshopapp.errors.PaymentDoesNotExistsException;
import com.example.mybookshopapp.errors.PaymentStatusException;
import com.example.mybookshopapp.security.BookstoreUserRegister;
import com.example.mybookshopapp.services.BookService;
import com.example.mybookshopapp.services.CartService;
import com.example.mybookshopapp.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;

@Controller
public class PaymentController {
    private final PaymentService paymentService;
    private final BookstoreUserRegister userRegister;
    private final CartService cartService;
    private final BookService bookService;

    @Autowired
    public PaymentController(PaymentService paymentService, BookstoreUserRegister userRegister, CartService cartService, BookService bookService){
        this.paymentService = paymentService;
        this.userRegister = userRegister;
        this.cartService = cartService;
        this.bookService = bookService;
    }

    @PostMapping("/payment")
    @CrossOrigin(origins = "https://yoomoney.ru")
    public String handlePayment(@RequestBody PaymentDto payment) throws URISyntaxException, IOException, InterruptedException {

        UserEntity user = (UserEntity) userRegister.getCurrentUser();
        if (user == null){
            // TODO add error gate
            return "";
        }

        int sum = 0;
        try {
            sum = paymentService.SumStringToLong(payment.getSum());
        } catch (NumberFormatException exception){

            // TODO add error gate
            return "";
        }

        String paymentUrl = paymentService.createPayment(sum, user);
        return "redirect:" + paymentUrl;
    }

    @GetMapping("/payment/confirm")
    public RedirectView handlePaymentConfirm(@RequestParam("key") String key){

        try {
            paymentService.updatePaymentStatus(key);
        } catch (PaymentDoesNotExistsException | PaymentStatusException exception){

            // TODO add error gate
            return new RedirectView("/profile");
        }

        return new RedirectView("/profile");
    }
}
