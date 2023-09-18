package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.data.BookEntity;
import com.example.mybookshopapp.data.UserEntity;
import com.example.mybookshopapp.dto.PaymentDto;
import com.example.mybookshopapp.errors.PaymentDoesNotExistsException;
import com.example.mybookshopapp.errors.PaymentStatusException;
import com.example.mybookshopapp.security.BookstoreUserRegister;
import com.example.mybookshopapp.security.UserRepository;
import com.example.mybookshopapp.services.BookService;
import com.example.mybookshopapp.services.BookStatusService;
import com.example.mybookshopapp.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@Controller
public class PaymentController {
    private final PaymentService paymentService;
    private final BookService bookService;
    private final BookStatusService bookStatusService;
    private final BookstoreUserRegister userRegister;
    private final UserRepository userRepository;

    @Autowired
    public PaymentController(PaymentService paymentService, BookstoreUserRegister userRegister, BookService bookService, BookService bookService1, BookStatusService bookStatusService, UserRepository userRepository){
        this.paymentService = paymentService;
        this.userRegister = userRegister;
        this.bookService = bookService1;
        this.bookStatusService = bookStatusService;
        this.userRepository = userRepository;
    }

    @PostMapping("/payment")
    public ResponseEntity<?> handlePayment(@RequestBody PaymentDto payment) throws URISyntaxException, IOException, InterruptedException {

        UserEntity user = (UserEntity) userRegister.getCurrentUser();
        if (user == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authorized");
        }

        int sum = 0;
        try {
            sum = paymentService.SumStringToLong(payment.getSum());
        } catch (NumberFormatException exception){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid sum format");
        }

        String paymentUrl = paymentService.createPayment(sum, user);
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .header("Location", paymentUrl)
                .header("Access-Control-Allow-Origin", "https://yoomoney.ru")
                .build();
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

    @GetMapping("/payment/books")
    public String handlePaymentBooks(RedirectAttributes redirectAttributes){
        UserEntity user = (UserEntity) userRegister.getCurrentUser();

        List<BookEntity> cartBooks = bookService.getBooksByUserStatus(user.getId(), "CART");
        Integer paymentAmount = cartBooks.stream().mapToInt(BookEntity::getDiscountPrice).sum();

        // Balance are enough
        if (paymentAmount <= user.getBalance()){
            user.setBalance(user.getBalance() - paymentAmount);
            userRepository.save(user);
            bookStatusService.setStatuses(cartBooks, user.getId(), "PAID");
            return "redirect:/my";
        }
        // Balance are not enough
        else {
            int lack = paymentAmount - user.getBalance();
            redirectAttributes.addFlashAttribute("paymentError", "Insufficient funds. Refill to " + lack / 100 + '.' + lack % 100 + " rubles. Go to profile");
            return "redirect:/cart";
        }
    }
}