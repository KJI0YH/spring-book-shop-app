package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.data.UserEntity;
import com.example.mybookshopapp.errors.BalanceNotEnoughException;
import com.example.mybookshopapp.security.UserService;
import com.example.mybookshopapp.services.PaymentService;
import com.example.mybookshopapp.services.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PaymentController {
    private final PaymentService paymentService;
    private final TransactionService transactionService;
    private final UserService userService;

    @GetMapping("/payment/confirm")
    public String handlePaymentConfirm(@RequestParam("key") String key) {
        paymentService.processPayment(paymentService.getPaymentStatusById(key));
        return "redirect:/profile";
    }

    @PostMapping("/payment/cart")
    public String handlePaymentBooks() throws BalanceNotEnoughException {
        UserEntity user = (UserEntity) userService.getCurrentUser();
        transactionService.cartBooksPayment(user);
        return "redirect:/my";
    }
}