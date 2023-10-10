package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.data.BalanceTransactionEntity;
import com.example.mybookshopapp.data.BookEntity;
import com.example.mybookshopapp.data.UserEntity;
import com.example.mybookshopapp.dto.ApiResponse;
import com.example.mybookshopapp.dto.PaymentDto;
import com.example.mybookshopapp.dto.TransactionPageDto;
import com.example.mybookshopapp.errors.ApiWrongParameterException;
import com.example.mybookshopapp.errors.PaymentDoesNotExistsException;
import com.example.mybookshopapp.errors.PaymentStatusException;
import com.example.mybookshopapp.security.BookstoreUserRegister;
import com.example.mybookshopapp.services.BookService;
import com.example.mybookshopapp.services.PaymentService;
import com.example.mybookshopapp.services.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PaymentController {
    private final PaymentService paymentService;
    private final BookService bookService;
    private final TransactionService transactionService;
    private final BookstoreUserRegister userRegister;

    @PostMapping("/api/payment")
    public ResponseEntity<ApiResponse> handlePayment(@RequestBody PaymentDto payment) throws URISyntaxException, IOException, InterruptedException {

        UserEntity user = (UserEntity) userRegister.getCurrentUser();
        if (user == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, "User not authorized"));
        }

        int sum = 0;
        try {
            sum = paymentService.SumStringToLong(payment.getSum());
        } catch (NumberFormatException exception){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, "Invalid sum format"));
        }

        String paymentUrl = paymentService.createPayment(sum, user);
        return ResponseEntity.status(HttpStatus.FOUND).body(new ApiResponse(true, paymentUrl));
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

    // TODO move to service level
    @GetMapping("/payment/books")
    public String handlePaymentBooks(RedirectAttributes redirectAttributes){
        UserEntity user = (UserEntity) userRegister.getCurrentUser();

        List<BookEntity> cartBooks = bookService.getAllBooksByUserStatus(user.getId(), "CART");
        Integer paymentAmount = cartBooks.stream().mapToInt(BookEntity::getDiscountPrice).sum();

        // Balance are enough
        if (paymentAmount <= user.getBalance()){
            transactionService.saveBooksTransactions(user, cartBooks);
            try {
                bookService.updateBook2UserStatuses(cartBooks.stream().map(BookEntity::getId).collect(Collectors.toList()), user.getId(), "PAID");
            } catch (ApiWrongParameterException e) {
                return "";
            }
            return "redirect:/my";
        }
        // Balance are not enough
        else {
            int lack = paymentAmount - user.getBalance();
            redirectAttributes.addFlashAttribute("paymentError", "Insufficient funds. Refill to " + lack / 100 + '.' + lack % 100 + " rubles. Go to profile");
            return "redirect:/cart";
        }
    }

    @GetMapping("/api/transactions")
    public ResponseEntity<TransactionPageDto> handleTransactions(@RequestParam(name = "sort", required = false) String sort,
                                                                 @RequestParam(name = "offset") Integer offset,
                                                                 @RequestParam(name = "limit") Integer limit){
        UserEntity user = (UserEntity) userRegister.getCurrentUser();

        if (user == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<BalanceTransactionEntity> transactions;

        if (sort.equals("asc")){
            transactions = transactionService.getTransactionsByUserAsc(user, offset, limit).getContent();
        } else {
            transactions = transactionService.getTransactionsByUserDesc(user, offset, limit).getContent();
        }

        return ResponseEntity.ok(new TransactionPageDto(transactions));
    }
}