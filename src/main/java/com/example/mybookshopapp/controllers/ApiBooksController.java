package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.data.UserEntity;
import com.example.mybookshopapp.dto.BooksPageDto;
import com.example.mybookshopapp.security.BookstoreUserRegister;
import com.example.mybookshopapp.services.BookService;
import com.example.mybookshopapp.services.DateResolverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
public class ApiBooksController {

    private final BookService bookService;
    private final DateResolverService dateResolverService;
    private final BookstoreUserRegister userRegister;

    @Autowired
    public ApiBooksController(BookService bookService, DateResolverService dateResolverService, BookstoreUserRegister userRegister) {
        this.bookService = bookService;
        this.dateResolverService = dateResolverService;
        this.userRegister = userRegister;
    }

    @GetMapping("/recent")
    public ResponseEntity<BooksPageDto> getRecentBooksPage(@RequestParam("offset") Integer offset,
                                                           @RequestParam("limit") Integer limit,
                                                           @RequestParam(value = "from", required = false) String from,
                                                           @RequestParam(value = "to", required = false) String to) {
        return ResponseEntity.ok(new BooksPageDto(bookService.getPageOfRecentBooks(dateResolverService.resolve(from), dateResolverService.resolve(to), offset, limit).getContent()));
    }

    @GetMapping("/recommended")
    public ResponseEntity<BooksPageDto> getRecommendedBooksPage(@RequestParam("offset") Integer offset,
                                                                @RequestParam("limit") Integer limit) {
        return ResponseEntity.ok(new BooksPageDto(bookService.getPageOfRecommendedBooks(offset, limit).getContent()));
    }

    @GetMapping("/popular")
    public ResponseEntity<BooksPageDto> getPopularBooksPage(@RequestParam("offset") Integer offset,
                                                            @RequestParam("limit") Integer limit) {
        return ResponseEntity.ok(new BooksPageDto(bookService.getPageOfPopularBooks(offset, limit).getContent()));
    }

    @GetMapping("/tag/{tagSlug}")
    public ResponseEntity<BooksPageDto> getBooksByTagPage(@PathVariable("tagSlug") String tagSlug,
                                                          @RequestParam("offset") Integer offset,
                                                          @RequestParam("limit") Integer limit) {
        return ResponseEntity.ok(new BooksPageDto(bookService.getPageOfBooksByTagSlug(tagSlug, offset, limit).getContent()));
    }

    @GetMapping("/genre/{genreSlug}")
    public ResponseEntity<BooksPageDto> getBooksByGenrePage(@PathVariable("genreSlug") String genreSlug,
                                                            @RequestParam("offset") Integer offset,
                                                            @RequestParam("limit") Integer limit){
        return ResponseEntity.ok(new BooksPageDto(bookService.getPageOfBooksByGenreSlug(genreSlug, offset, limit).getContent()));
    }

    @GetMapping("/author/{authorSlug}")
    public ResponseEntity<BooksPageDto> getBooksByAuthorIdPage(@PathVariable("authorSlug") String authroSlug,
                                                               @RequestParam("offset") Integer offset,
                                                               @RequestParam("limit") Integer limit){
        return ResponseEntity.ok(new BooksPageDto(bookService.getPageOfBooksByAuthorSlug(authroSlug, offset, limit).getContent()));
    }

    @GetMapping("/my")
    public ResponseEntity<BooksPageDto> getBooksMyPage(@RequestParam("offset") Integer offset,
                                                       @RequestParam("limit") Integer limit){
        UserEntity user = (UserEntity) userRegister.getCurrentUser();
        return ResponseEntity.ok(new BooksPageDto(bookService.getPageOfBooksByUserStatus(user.getId(), "PAID", offset, limit).getContent()));
    }

    @GetMapping("/my/archive")
    public ResponseEntity<BooksPageDto> getBooksMyArchivePage(@RequestParam("offset") Integer offset,
                                                              @RequestParam("limit") Integer limit){
        UserEntity user = (UserEntity) userRegister.getCurrentUser();
        return ResponseEntity.ok(new BooksPageDto(bookService.getPageOfBooksByUserStatus(user.getId(), "ARCHIVED", offset, limit).getContent()));
    }
}
