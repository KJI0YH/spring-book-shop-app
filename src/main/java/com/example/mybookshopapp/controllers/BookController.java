package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.data.BookEntity;
import com.example.mybookshopapp.dto.SearchWordDto;
import com.example.mybookshopapp.repositories.BookRepository;
import com.example.mybookshopapp.services.BookService;
import com.example.mybookshopapp.services.ResourceStorage;
import io.swagger.v3.oas.annotations.media.Content;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final ResourceStorage storage;
    private final BookRepository bookRepository;

    @Autowired
    public BookController(BookService bookService, ResourceStorage storage, BookRepository bookRepository) {
        this.bookService = bookService;
        this.storage = storage;
        this.bookRepository = bookRepository;
    }

    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto(){
        return new SearchWordDto();
    }

    @GetMapping("/{bookSlug}")
    public String getBookPage(@PathVariable("bookSlug") String bookSlug, Model model){
        model.addAttribute("book", bookService.getBookBySlug(bookSlug));
        return "/books/slug";
    }

    @PostMapping("/{bookSlug}/img/save")
    public String saveNewBookImage(@PathVariable("bookSlug") String bookSlug,
                                   @RequestParam("file")MultipartFile file) throws IOException {

        String savePath = storage.saveNewBookImage(file, bookSlug);
        BookEntity bookToUpdate = bookService.getBookBySlug(bookSlug);
        bookToUpdate.setImage(savePath);
        bookRepository.save(bookToUpdate);

        return ("redirect:/books/" + bookSlug);
    }

    @GetMapping("/download/{bookFileHash}")
    public ResponseEntity<ByteArrayResource> bookFile(@PathVariable("bookFileHash") String hash) throws IOException {
        Path path = storage.getBookFilePath(hash);

        MediaType mediaType = storage.getBookFileMime(hash);

        byte[] data = storage.getBookFileByteArray(hash);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + path.getFileName().toString())
                .contentType(mediaType)
                .contentLength(data.length)
                .body(new ByteArrayResource(data));

    }
}
