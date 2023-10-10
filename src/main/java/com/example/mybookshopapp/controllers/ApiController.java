package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.data.UserEntity;
import com.example.mybookshopapp.dto.*;
import com.example.mybookshopapp.errors.ApiWrongParameterException;
import com.example.mybookshopapp.security.BookstoreUserRegister;
import com.example.mybookshopapp.services.BookService;
import com.example.mybookshopapp.services.CookieService;
import com.example.mybookshopapp.services.DateService;
import com.example.mybookshopapp.services.MessageService;
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
    private final BookstoreUserRegister userRegister;
    private final DateService dateService;

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
        return ResponseEntity.ok(new BooksPageDto(bookService.getPageOfRecommendedBooks(offset, limit)));
    }

    @GetMapping("/books/popular")
    public ResponseEntity<BooksPageDto> getPopularBooksPage(@RequestParam("offset") Integer offset,
                                                            @RequestParam("limit") Integer limit) {
        return ResponseEntity.ok(new BooksPageDto(bookService.getPageOfPopularBooks(offset, limit)));
    }

    @GetMapping("/books/viewed")
    public ResponseEntity<BooksPageDto> getBooksViewedPage(@RequestParam("offset") Integer offset,
                                                           @RequestParam("limit") Integer limit) {
        UserEntity user = (UserEntity) userRegister.getCurrentUser();
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
        UserEntity user = (UserEntity) userRegister.getCurrentUser();
        return ResponseEntity.ok(new BooksPageDto(bookService.getPageOfBooksByUserStatus(user.getId(), "PAID", offset, limit)));
    }

    @GetMapping("/books/my/archive")
    public ResponseEntity<BooksPageDto> getBooksMyArchivePage(@RequestParam("offset") Integer offset,
                                                              @RequestParam("limit") Integer limit) {
        UserEntity user = (UserEntity) userRegister.getCurrentUser();
        return ResponseEntity.ok(new BooksPageDto(bookService.getPageOfBooksByUserStatus(user.getId(), "ARCHIVED", offset, limit)));
    }

    @PostMapping("/changeBookStatus")
    public ResponseEntity<ApiResponse> handleChangeBookStatus(HttpServletResponse response,
                                                              @RequestBody BookStatusDto bookStatusDto,
                                                              @CookieValue(value = "cartContents", required = false) String cartContents,
                                                              @CookieValue(value = "postponedContents", required = false) String keptContents) {
        UserEntity user = (UserEntity) userRegister.getCurrentUser();
        try {

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
        } catch (ApiWrongParameterException e){
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
        return ResponseEntity.ok(new ApiResponse(true));
    }

    @PostMapping("/rateBook")
    public ResponseEntity<ApiResponse> rateBook(@RequestBody BookRateDto bookRateDto) {
        UserEntity user = (UserEntity) userRegister.getCurrentUser();
        if (user == null)
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse(false, "Only authorised users can rate the book"));
        try {
            bookService.rateBook(bookRateDto.getBookId(), user.getId(), bookRateDto.getValue());
        } catch (ApiWrongParameterException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
        return ResponseEntity.ok(new ApiResponse( true));
    }

    @PostMapping("/bookReview")
    public ResponseEntity<ApiResponse> bookReview(@RequestBody BookReviewDto bookReviewDto){
        UserEntity user = (UserEntity) userRegister.getCurrentUser();
        if (user == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse( false, "Only authorised users can review the book"));
        }
        try{
            bookService.reviewBook(bookReviewDto.getBookId(), user.getId(), bookReviewDto.getText());
        } catch (ApiWrongParameterException e){
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
        return ResponseEntity.ok(new ApiResponse(true));
    }

    @PostMapping("/rateBookReview")
    public ResponseEntity<ApiResponse> rateBookReview(@RequestBody ReviewLikeDto reviewLikeDto){
        UserEntity user = (UserEntity) userRegister.getCurrentUser();
        if (user == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse(false, "Only authorised users can rate the book review"));
        }
        try {
            bookService.rateBookReview(reviewLikeDto.getReviewId(), user.getId(), reviewLikeDto.getValue());
        } catch (ApiWrongParameterException e){
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
        return ResponseEntity.ok(new ApiResponse(true));
    }

    @GetMapping("/search/{searchWord}")
    public ResponseEntity<BooksPageDto> getSearchPage(@PathVariable(value = "searchWord", required = false) SearchWordDto searchWordDto,
                                                      @RequestParam("offset") Integer offset,
                                                      @RequestParam("limit") Integer limit) {
        return ResponseEntity.ok(new BooksPageDto(bookService.getPageOfBooksByTitle(searchWordDto.getExample(), offset, limit)));
    }

    @PostMapping(value = "/contacts/message", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<ApiResponse> handleContactMessage(MessageDto messageDto){
        messageService.saveMessage(messageDto);
        return ResponseEntity
                .status(HttpStatus.SEE_OTHER)
                .header("Location", "/contacts")
                .body(new ApiResponse(true));
    }
}
