package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.data.UserEntity;
import com.example.mybookshopapp.errors.ApiWrongParameterException;
import com.example.mybookshopapp.errors.BalanceNotEnoughException;
import com.example.mybookshopapp.security.BookstoreUserRegister;
import com.example.mybookshopapp.services.PaymentService;
import com.example.mybookshopapp.services.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PaymentController {
    private final PaymentService paymentService;
    private final TransactionService transactionService;
    private final BookstoreUserRegister userRegister;

    @GetMapping("/payment/confirm")
    public String handlePaymentConfirm(@RequestParam("key") String key) {
        paymentService.processPayment(paymentService.getPaymentStatusById(key));
        return "redirect:/profile";
    }

    @PostMapping("/payment/cart")
    public String handlePaymentBooks() throws BalanceNotEnoughException, ApiWrongParameterException {
        UserEntity user = (UserEntity) userRegister.getCurrentUser();
        transactionService.cartBooksPayment(user);
        return "redirect:/my";
    }
}