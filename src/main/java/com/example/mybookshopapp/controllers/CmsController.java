package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.data.AuthorEntity;
import com.example.mybookshopapp.data.BookEntity;
import com.example.mybookshopapp.data.GenreEntity;
import com.example.mybookshopapp.data.TagEntity;
import com.example.mybookshopapp.dto.*;
import com.example.mybookshopapp.errors.ApiWrongParameterException;
import com.example.mybookshopapp.services.AuthorService;
import com.example.mybookshopapp.services.BookService;
import com.example.mybookshopapp.services.GenreService;
import com.example.mybookshopapp.services.TagService;
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
}
