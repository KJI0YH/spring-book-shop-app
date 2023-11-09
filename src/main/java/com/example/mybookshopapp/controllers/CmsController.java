package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.data.*;
import com.example.mybookshopapp.dto.*;
import com.example.mybookshopapp.errors.ApiWrongParameterException;
import com.example.mybookshopapp.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/cms")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CmsController {
    private final TagService tagService;
    private final GenreService genreService;
    private final AuthorService authorService;
    private final BookService bookService;
    private final UserService userService;
    private final BookReviewSerivce bookReviewSerivce;

    @GetMapping("/tag/all")
    public ResponseEntity<List<TagEntity>> getAllTags() {
        return ResponseEntity.ok(tagService.getAllTags());
    }

    @GetMapping("/tag/{tagId}")
    public ResponseEntity<TagEntity> getTag(@PathVariable("tagId") Integer tagId) throws ApiWrongParameterException {
        return ResponseEntity.ok(tagService.getTagById(tagId));
    }

    @PostMapping("/tag")
    public ResponseEntity<TagEntity> createTag(@RequestBody TagDto tagDto) throws ApiWrongParameterException {
        return ResponseEntity.ok(tagService.createTag(tagDto));
    }

    @PutMapping("/tag/{tagId}")
    public ResponseEntity<TagEntity> updateTag(@PathVariable("tagId") Integer tagId,
                                               @RequestBody TagDto tagDto) throws ApiWrongParameterException {
        return ResponseEntity.ok(tagService.updateTag(tagId, tagDto));
    }

    @DeleteMapping("/tag/{tagId}")
    public ResponseEntity<ApiResponse> deleteTag(@PathVariable("tagId") Integer tagId) throws ApiWrongParameterException {
        tagService.deleteTagById(tagId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ApiResponse(true));
    }

    @GetMapping("/genre/all")
    public ResponseEntity<List<GenreEntity>> getAllGenres() {
        return ResponseEntity.ok(genreService.getAllGenres());
    }

    @GetMapping("/genre/{genreId}")
    public ResponseEntity<GenreEntity> getGenre(@PathVariable("genreId") Integer genreId) throws ApiWrongParameterException {
        return ResponseEntity.ok(genreService.getGenreById(genreId));
    }

    @PostMapping("/genre")
    public ResponseEntity<GenreEntity> createGenre(@RequestBody GenreDto genreDto) throws ApiWrongParameterException {
        return ResponseEntity.ok(genreService.createGenre(genreDto));
    }

    @PutMapping("/genre/{genreId}")
    public ResponseEntity<GenreEntity> updateGenre(@PathVariable("genreId") Integer genreId,
                                                   @RequestBody GenreDto genreDto) throws ApiWrongParameterException {
        return ResponseEntity.ok(genreService.updateGenre(genreId, genreDto));
    }

    @DeleteMapping("/genre/{genreId}")
    public ResponseEntity<ApiResponse> deleteGenre(@PathVariable("genreId") Integer genreId) throws ApiWrongParameterException {
        genreService.deleteGenreById(genreId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ApiResponse(true));
    }

    @GetMapping("/author/all")
    public ResponseEntity<List<AuthorEntity>> getAllAuthors() {
        return ResponseEntity.ok(authorService.getAllAuthors());
    }

    @GetMapping("/author/{authorId}")
    public ResponseEntity<AuthorEntity> getAuthor(@PathVariable("authorId") Integer authorId) throws ApiWrongParameterException {
        return ResponseEntity.ok(authorService.getAuthorById(authorId));
    }

    @PostMapping("/author")
    public ResponseEntity<AuthorEntity> createAuthor(@RequestBody AuthorDto authorDto) throws ApiWrongParameterException {
        return ResponseEntity.ok(authorService.createAuthor(authorDto));
    }

    @PutMapping("/author/{authorId}")
    public ResponseEntity<AuthorEntity> updateAuthor(@PathVariable("authorId") Integer authorId,
                                                     @RequestBody AuthorDto authorDto) throws ApiWrongParameterException {
        return ResponseEntity.ok(authorService.updateAuthor(authorId, authorDto));
    }

    @DeleteMapping("/author/{authorId}")
    public ResponseEntity<ApiResponse> deleteAuthor(@PathVariable("authorId") Integer authorId) throws ApiWrongParameterException {
        authorService.deleteAuthor(authorId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ApiResponse(true));
    }

    @GetMapping("/book/all")
    public ResponseEntity<List<BookEntity>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<BookEntity> getBook(@PathVariable("bookId") Integer bookId) throws ApiWrongParameterException {
        return ResponseEntity.ok(bookService.getBookById(bookId));
    }

    @PostMapping("/book")
    public ResponseEntity<BookEntity> createBook(@RequestBody BookDto bookDto) throws ApiWrongParameterException {
        return ResponseEntity.ok(bookService.createBook(bookDto));
    }

    @PutMapping("/book/{bookId}")
    public ResponseEntity<BookEntity> updateBook(@PathVariable("bookId") Integer bookId,
                                                 @RequestBody BookDto bookDto) throws ApiWrongParameterException {
        return ResponseEntity.ok(bookService.updateBook(bookId, bookDto));
    }

    @DeleteMapping("/book/{bookId}")
    public ResponseEntity<ApiResponse> deleteBook(@PathVariable("bookId") Integer bookId) throws ApiWrongParameterException {
        bookService.deleteBook(bookId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ApiResponse(true));
    }

    @GetMapping("/book/tag/{tagId}")
    public ResponseEntity<List<BookEntity>> getBooksByTag(@PathVariable("tagId") Integer tagId) {
        return ResponseEntity.ok(bookService.getBooksByTagId(tagId));
    }

    @PostMapping("/book/tag")
    public ResponseEntity<List<Book2TagEntity>> createBook2Tag(@RequestBody Book2TagDto book2TagDto) {
        return ResponseEntity.ok(bookService.createBook2Tag(book2TagDto.getBookIds(), book2TagDto.getTagIds()));
    }

    @DeleteMapping("/book/{bookId}/tag/{tagId}")
    public ResponseEntity<ApiResponse> deleteBook2Tag(@PathVariable("bookId") Integer bookId,
                                                      @PathVariable("tagId") Integer tagId) throws ApiWrongParameterException {
        bookService.deleteBook2Tag(bookId, tagId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ApiResponse(true));
    }

    @GetMapping("/book/genre/{genreId}")
    public ResponseEntity<List<BookEntity>> getBooksByGenre(@PathVariable("genreId") Integer genreId) {
        return ResponseEntity.ok(bookService.getBooksByGenreId(genreId));
    }

    @PostMapping("/book/genre")
    public ResponseEntity<List<Book2GenreEntity>> createBook2Genre(@RequestBody Book2GenreDto book2GenreDto) {
        return ResponseEntity.ok(bookService.createBook2Genre(book2GenreDto.getBookIds(), book2GenreDto.getGenreIds()));
    }

    @DeleteMapping("/book/{bookId}/genre/{genreId}")
    public ResponseEntity<ApiResponse> deleteBook2Genre(@PathVariable("bookId") Integer bookId,
                                                        @PathVariable("genreId") Integer genreId) throws ApiWrongParameterException {
        bookService.deleteBook2Genre(bookId, genreId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ApiResponse(true));
    }

    @GetMapping("/book/author/{authorId}")
    public ResponseEntity<List<BookEntity>> getBooksByAuthor(@PathVariable("authorId") Integer authorId) {
        return ResponseEntity.ok(bookService.getBooksByAuthorId(authorId));
    }

    @PostMapping("/book/author")
    public ResponseEntity<List<Book2AuthorEntity>> createBook2Author(@RequestBody Book2AuthorDto book2AuthorDto) {
        return ResponseEntity.ok(bookService.createBook2Author(book2AuthorDto.getBookIds(), book2AuthorDto.getAuthors()));
    }

    @PutMapping("/book/author")
    public ResponseEntity<List<Book2AuthorEntity>> updateBook2Author(@RequestBody Book2AuthorDto book2AuthorDto) throws ApiWrongParameterException {
        return ResponseEntity.ok(bookService.updateBook2Author(book2AuthorDto));
    }

    @DeleteMapping("/book/{bookId}/author/{authorId}")
    public ResponseEntity<ApiResponse> deleteBook2Author(@PathVariable("bookId") Integer bookId,
                                                         @PathVariable("authorId") Integer authorId) throws ApiWrongParameterException {
        bookService.deleteBook2Author(bookId, authorId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ApiResponse(false));
    }

    @GetMapping("/user/all")
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<UserEntity> getUserById(@PathVariable("userId") Integer userId) throws ApiWrongParameterException {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @GetMapping("/book/user/{userId}")
    public ResponseEntity<List<BookEntity>> getUserBooks(@PathVariable("userId") Integer userId) {
        return ResponseEntity.ok(bookService.getAllUserBooks(userId));
    }

    @PostMapping("/book/user")
    public ResponseEntity<List<Book2UserEntity>> createBook2User(@RequestBody Book2UserDto book2UserDto) throws ApiWrongParameterException {
        return ResponseEntity.ok(bookService.createBook2User(book2UserDto));
    }

    @PutMapping("/book/user")
    public ResponseEntity<List<Book2UserEntity>> updateBook2User(@RequestBody Book2UserDto book2UserDto) throws ApiWrongParameterException {
        return ResponseEntity.ok(bookService.updateBook2User(book2UserDto));
    }

    @DeleteMapping("/user/{userId}/book/{bookId}")
    public ResponseEntity<ApiResponse> deleteBook2User(@PathVariable("userId") Integer userId,
                                                       @PathVariable("bookId") Integer bookId) throws ApiWrongParameterException {
        bookService.deleteBook2User(bookId, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ApiResponse(true));
    }

    @GetMapping("/bookReview/all")
    public ResponseEntity<List<BookReviewEntity>> getAllReviews() {
        return ResponseEntity.ok(bookReviewSerivce.getAllReviews());
    }

    @GetMapping("/bookReview/{reviewId}")
    public ResponseEntity<BookReviewEntity> getReviewById(@PathVariable("reviewId") Integer reviewId) throws ApiWrongParameterException {
        return ResponseEntity.ok(bookReviewSerivce.getReviewById(reviewId));
    }

    @GetMapping("/book/{bookId}/bookReview")
    public ResponseEntity<List<BookReviewEntity>> getReviewByBookId(@PathVariable("bookId") Integer bookId) {
        return ResponseEntity.ok(bookReviewSerivce.getReviewsByBookId(bookId));
    }

    @PutMapping("/bookReview/{reviewId}")
    public ResponseEntity<BookReviewEntity> updateBookReview(@PathVariable("reviewId") Integer reviewId,
                                                             @RequestBody BookReviewDto bookReviewDto) throws ApiWrongParameterException {
        return ResponseEntity.ok(bookReviewSerivce.updateBookReview(reviewId, bookReviewDto));
    }

    @DeleteMapping("/bookReview/{reviewId}")
    public ResponseEntity<ApiResponse> deleteBookReview(@PathVariable("reviewId") Integer reviewId) throws ApiWrongParameterException {
        bookReviewSerivce.deleteBookReview(reviewId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ApiResponse(true));
    }
}
