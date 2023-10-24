package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.data.BookEntity;
import com.example.mybookshopapp.data.UserEntity;
import com.example.mybookshopapp.errors.ApiWrongParameterException;
import com.example.mybookshopapp.errors.FileDownloadException;
import com.example.mybookshopapp.errors.PaymentRequiredException;
import com.example.mybookshopapp.errors.UserUnauthorizedException;
import com.example.mybookshopapp.repositories.BookRepository;
import com.example.mybookshopapp.services.BookService;
import com.example.mybookshopapp.services.CookieService;
import com.example.mybookshopapp.services.ResourceStorage;
import com.example.mybookshopapp.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BookController extends AbstractHeaderFooterController {

    private final BookService bookService;
    private final ResourceStorage storage;
    private final BookRepository bookRepository;
    private final UserService userService;
    private final CookieService cookieService;

    @GetMapping("/{bookSlug}")
    public String getBookPage(@PathVariable("bookSlug") String bookSlug,
                              @CookieValue(value = "viewedContents", required = false) String viewedContents,
                              Model model,
                              HttpServletResponse response) {
        UserEntity user = userService.getCurrentUser();
        BookEntity book = bookService.getBookBySlug(bookSlug);

        if (book != null) {
            model.addAttribute("book", book);

            // Authorized user
            if (user != null) {
                bookService.setViewedBook(user.getId(), book.getId());
                if (bookService.isBookPaid(book.getId(), user.getId())) {
                    return "books/slugmy";
                }
            }

            // Unauthorized user
            else {
                response.addCookie(cookieService.addBookId(viewedContents, book.getId().toString()));
            }
        }
        return "books/slug";
    }

    @PostMapping("/{bookSlug}/img/save")
    public String saveNewBookImage(@PathVariable("bookSlug") String bookSlug,
                                   @RequestParam("file") MultipartFile file) throws IOException {

        String savePath = storage.saveNewBookImage(file, bookSlug);
        BookEntity bookToUpdate = bookService.getBookBySlug(bookSlug);
        bookToUpdate.setImage(savePath);
        bookRepository.save(bookToUpdate);

        return ("redirect:/books/" + bookSlug);
    }

    @GetMapping("/download/{bookFileHash}")
    public ResponseEntity<ByteArrayResource> handleDownloadBookFile(@PathVariable("bookFileHash") String hash) throws PaymentRequiredException, UserUnauthorizedException, ApiWrongParameterException, FileDownloadException {
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
