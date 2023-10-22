package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.data.UserEntity;
import com.example.mybookshopapp.dto.*;
import com.example.mybookshopapp.errors.ApiWrongParameterException;
import com.example.mybookshopapp.errors.PaymentInitiateException;
import com.example.mybookshopapp.services.UserService;
import com.example.mybookshopapp.services.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApiController {
    private final BookService bookService;
    private final CookieService cookieService;
    private final MessageService messageService;
    private final TransactionService transactionService;
    private final PaymentService paymentService;
    private final UserService userService;
    private final DateService dateService;
    private final BookSearchService bookSearchService;
    private final BookRecommendedService bookRecommendedService;

    @GetMapping("/books/recent")
    public ResponseEntity<BooksPageDto> getRecentBooksPage(@RequestParam("offset") Integer offset,
                                                           @RequestParam("limit") Integer limit,
                                                           @RequestParam(value = "from", required = false) String from,
                                                           @RequestParam(value = "to", required = false) String to) {
        return ResponseEntity.ok(new BooksPageDto(bookService.getPageOfRecentBooks(dateService.convertToLocalDate(from), dateService.convertToLocalDate(to), offset, limit)));
    }

    @GetMapping("/books/recommended")
    public ResponseEntity<BooksPageDto> getRecommendedBooksPage(@RequestParam("offset") Integer offset,
                                                                @RequestParam("limit") Integer limit) {
        return ResponseEntity.ok(new BooksPageDto(bookRecommendedService.getPageOfRecommendedBooks(offset, limit)));
    }

    @GetMapping("/books/popular")
    public ResponseEntity<BooksPageDto> getPopularBooksPage(@RequestParam("offset") Integer offset,
                                                            @RequestParam("limit") Integer limit) {
        return ResponseEntity.ok(new BooksPageDto(bookService.getPageOfPopularBooks(offset, limit)));
    }

    @GetMapping("/books/viewed")
    public ResponseEntity<BooksPageDto> getBooksViewedPage(@RequestParam("offset") Integer offset,
                                                           @RequestParam("limit") Integer limit) {
        UserEntity user = userService.getCurrentUser();
        return ResponseEntity.ok(new BooksPageDto((bookService.getPageOfViewedBooks(user.getId(), offset, limit))));
    }

    @GetMapping("/books/tag/{tagSlug}")
    public ResponseEntity<BooksPageDto> getBooksByTagPage(@PathVariable("tagSlug") String tagSlug,
                                                          @RequestParam("offset") Integer offset,
                                                          @RequestParam("limit") Integer limit) {
        return ResponseEntity.ok(new BooksPageDto(bookService.getPageOfBooksByTagSlug(tagSlug, offset, limit)));
    }

    @GetMapping("/books/genre/{genreId}")
    public ResponseEntity<BooksPageDto> getBooksByGenrePage(@PathVariable("genreId") Integer genreId,
                                                            @RequestParam("offset") Integer offset,
                                                            @RequestParam("limit") Integer limit) {
        return ResponseEntity.ok(new BooksPageDto(bookService.getPageOfBooksByGenreId(genreId, offset, limit)));
    }

    @GetMapping("/books/author/{authorId}")
    public ResponseEntity<BooksPageDto> getBooksByAuthorIdPage(@PathVariable("authorId") Integer authorId,
                                                               @RequestParam("offset") Integer offset,
                                                               @RequestParam("limit") Integer limit) {
        return ResponseEntity.ok(new BooksPageDto(bookService.getPageOfBooksByAuthorId(authorId, offset, limit)));
    }

    @GetMapping("/books/my")
    public ResponseEntity<BooksPageDto> getBooksMyPage(@RequestParam("offset") Integer offset,
                                                       @RequestParam("limit") Integer limit) {
        UserEntity user = userService.getCurrentUser();
        return ResponseEntity.ok(new BooksPageDto(bookService.getPageOfBooksByUserStatus(user.getId(), "PAID", offset, limit)));
    }

    @GetMapping("/books/my/archive")
    public ResponseEntity<BooksPageDto> getBooksMyArchivePage(@RequestParam("offset") Integer offset,
                                                              @RequestParam("limit") Integer limit) {
        UserEntity user = userService.getCurrentUser();
        return ResponseEntity.ok(new BooksPageDto(bookService.getPageOfBooksByUserStatus(user.getId(), "ARCHIVED", offset, limit)));
    }

    @PostMapping("/changeBookStatus")
    public ResponseEntity<ApiResponse> handleChangeBookStatus(HttpServletResponse response,
                                                              @RequestBody BookStatusDto bookStatusDto,
                                                              @CookieValue(value = "cartContents", required = false) String cartContents,
                                                              @CookieValue(value = "postponedContents", required = false) String keptContents) throws ApiWrongParameterException {
        UserEntity user = userService.getCurrentUser();

        // Authorized user
        if (user != null) {
            Integer[] booksIds = Arrays.stream(bookStatusDto.getBooksIds()).map(Integer::valueOf).toArray(Integer[]::new);
            bookService.updateBook2UserStatuses(List.of(booksIds), user.getId(), bookStatusDto.getStatus());
        }

        // Unauthorized user
        else {
            List<Cookie> cookies = cookieService.updateCookieBookStatuses(bookStatusDto.getBooksIds(), cartContents, keptContents, bookStatusDto.getStatus());
            cookies.forEach(response::addCookie);
        }
        return ResponseEntity.ok(new ApiResponse(true));
    }

    @PostMapping("/rateBook")
    public ResponseEntity<ApiResponse> rateBook(@RequestBody BookRateDto bookRateDto) throws ApiWrongParameterException {
        UserEntity user = userService.getCurrentUser();
        if (user == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse(false, "Only authorised users can rate the book"));

        bookService.rateBook(bookRateDto.getBookId(), user.getId(), bookRateDto.getValue());
        return ResponseEntity.ok(new ApiResponse(true));
    }

    @PostMapping("/bookReview")
    public ResponseEntity<ApiResponse> bookReview(@RequestBody BookReviewDto bookReviewDto) throws ApiWrongParameterException {
        UserEntity user = userService.getCurrentUser();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse(false, "Only authorised users can review the book"));
        }

        bookService.reviewBook(bookReviewDto.getBookId(), user.getId(), bookReviewDto.getText());
        return ResponseEntity.ok(new ApiResponse(true));
    }

    @PostMapping("/rateBookReview")
    public ResponseEntity<ApiResponse> rateBookReview(@RequestBody ReviewLikeDto reviewLikeDto) throws ApiWrongParameterException {
        UserEntity user = userService.getCurrentUser();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse(false, "Only authorised users can rate the book review"));
        }

        bookService.rateBookReview(reviewLikeDto.getReviewId(), user.getId(), reviewLikeDto.getValue());
        return ResponseEntity.ok(new ApiResponse(true));
    }

    @GetMapping("/search/{searchWord}")
    public ResponseEntity<BooksPageDto> getSearchPage(@PathVariable(value = "searchWord", required = false) String searchWord,
                                                      @RequestParam("offset") Integer offset,
                                                      @RequestParam("limit") Integer limit) {
        return ResponseEntity.ok(new BooksPageDto(bookSearchService.getPageOfBooksByQuery(searchWord, offset, limit)));
    }

    @PostMapping(value = "/contacts/message", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<ApiResponse> handleContactMessage(MessageDto messageDto) {
        messageService.saveMessage(messageDto);
        return ResponseEntity
                .status(HttpStatus.SEE_OTHER)
                .header("Location", "/contacts")
                .body(new ApiResponse(true));
    }

    @GetMapping("/transactions")
    public ResponseEntity<TransactionPageDto> handleTransactions(@RequestParam(name = "sort", required = false) String sort,
                                                                 @RequestParam(name = "offset") Integer offset,
                                                                 @RequestParam(name = "limit") Integer limit) {
        UserEntity user = userService.getCurrentUser();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(new TransactionPageDto(transactionService.getTransactionByUser(user, offset, limit, sort).getContent()));
    }

    // TODO fix redirect
    @PostMapping("/payment")
    public ResponseEntity<ApiResponse> handlePayment(@RequestBody PaymentDto payment) throws ApiWrongParameterException, PaymentInitiateException {
        String paymentUrl = paymentService.initiatePayment(payment.getSum(), payment.getHash());
        return ResponseEntity.ok()
                .body(new ApiResponse(true, paymentUrl));
    }
}
