package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.data.UserEntity;
import com.example.mybookshopapp.dto.BooksPageDto;
import com.example.mybookshopapp.security.BookstoreUserRegister;
import com.example.mybookshopapp.services.BookService;
import com.example.mybookshopapp.services.BookViewedService;
import com.example.mybookshopapp.services.DateResolverService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApiBooksController {

    private final BookService bookService;
    private final DateResolverService dateResolverService;
    private final BookstoreUserRegister userRegister;
    private final BookViewedService bookViewedService;

    @GetMapping("/recent")
    public ResponseEntity<BooksPageDto> getRecentBooksPage(@RequestParam("offset") Integer offset,
                                                           @RequestParam("limit") Integer limit,
                                                           @RequestParam(value = "from", required = false) String from,
                                                           @RequestParam(value = "to", required = false) String to) {
        return ResponseEntity.ok(new BooksPageDto(bookService.getPageOfRecentBooks(dateResolverService.resolve(from), dateResolverService.resolve(to), offset, limit)));
    }

    @GetMapping("/recommended")
    public ResponseEntity<BooksPageDto> getRecommendedBooksPage(@RequestParam("offset") Integer offset,
                                                                @RequestParam("limit") Integer limit) {
        return ResponseEntity.ok(new BooksPageDto(bookService.getPageOfRecommendedBooks(offset, limit)));
    }

    @GetMapping("/popular")
    public ResponseEntity<BooksPageDto> getPopularBooksPage(@RequestParam("offset") Integer offset,
                                                            @RequestParam("limit") Integer limit) {
        return ResponseEntity.ok(new BooksPageDto(bookService.getPageOfPopularBooks(offset, limit)));
    }

    @GetMapping("/tag/{tagSlug}")
    public ResponseEntity<BooksPageDto> getBooksByTagPage(@PathVariable("tagSlug") String tagSlug,
                                                          @RequestParam("offset") Integer offset,
                                                          @RequestParam("limit") Integer limit) {
        return ResponseEntity.ok(new BooksPageDto(bookService.getPageOfBooksByTagSlug(tagSlug, offset, limit)));
    }

    @GetMapping("/genre/{genreId}")
    public ResponseEntity<BooksPageDto> getBooksByGenrePage(@PathVariable("genreId") Integer genreId,
                                                            @RequestParam("offset") Integer offset,
                                                            @RequestParam("limit") Integer limit){
        return ResponseEntity.ok(new BooksPageDto(bookService.getPageOfBooksByGenreId(genreId, offset, limit)));
    }

    @GetMapping("/author/{authorId}")
    public ResponseEntity<BooksPageDto> getBooksByAuthorIdPage(@PathVariable("authorId") Integer authorId,
                                                               @RequestParam("offset") Integer offset,
                                                               @RequestParam("limit") Integer limit){
        return ResponseEntity.ok(new BooksPageDto(bookService.getPageOfBooksByAuthorId(authorId, offset, limit)));
    }

    @GetMapping("/my")
    public ResponseEntity<BooksPageDto> getBooksMyPage(@RequestParam("offset") Integer offset,
                                                       @RequestParam("limit") Integer limit){
        UserEntity user = (UserEntity) userRegister.getCurrentUser();
        return ResponseEntity.ok(new BooksPageDto(bookService.getPageOfBooksByUserStatus(user.getId(), "PAID", offset, limit)));
    }

    @GetMapping("/my/archive")
    public ResponseEntity<BooksPageDto> getBooksMyArchivePage(@RequestParam("offset") Integer offset,
                                                              @RequestParam("limit") Integer limit){
        UserEntity user = (UserEntity) userRegister.getCurrentUser();
        return ResponseEntity.ok(new BooksPageDto(bookService.getPageOfBooksByUserStatus(user.getId(), "ARCHIVED", offset, limit)));
    }

    @GetMapping("/viewed")
    public ResponseEntity<BooksPageDto> getBooksViewedPage(@RequestParam("offset") Integer offset,
                                                           @RequestParam("limit") Integer limit){
        UserEntity user = (UserEntity) userRegister.getCurrentUser();
        return ResponseEntity.ok(new BooksPageDto((bookViewedService.getPageOfViewedBooks(user.getId(), offset, limit))));
    }
}
