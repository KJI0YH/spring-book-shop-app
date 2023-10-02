package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.data.ApiResponse;
import com.example.mybookshopapp.data.UserEntity;
import com.example.mybookshopapp.dto.BookCookieStoreDto;
import com.example.mybookshopapp.dto.BookStatusDto;
import com.example.mybookshopapp.security.BookstoreUserRegister;
import com.example.mybookshopapp.services.BookStatusService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApiBookStatusController {

    private final BookStatusService bookStatusService;
    private final BookstoreUserRegister userRegister;

    @PostMapping("/changeBookStatus")
    @ResponseBody
    public ResponseEntity<ApiResponse> handleChangeBookStatus(@RequestBody BookStatusDto bookStatusDto,
                                                              HttpServletResponse response,
                                                              @CookieValue(value = "cartContents", required = false) String cartContents,
                                                              @CookieValue(value = "postponedContents", required = false) String postponedContents) throws JsonProcessingException {

        UserEntity user = (UserEntity) userRegister.getCurrentUser();

        // Unauthorized user, store data in cookies
        if (user == null){
            BookCookieStoreDto bookCookieStore = bookStatusService.changeBookCookiesStatus(bookStatusDto.getStatus(), cartContents, postponedContents, bookStatusDto.getBooksIds());
            response.addCookie(bookStatusService.createCookieFromBookIds(bookCookieStore.getCartContents(), "cartContents"));
            response.addCookie(bookStatusService.createCookieFromBookIds(bookCookieStore.getPostponedContents(), "postponedContents"));

            return ResponseEntity.ok(new ApiResponse(HttpStatus.OK, true));

            // Authorized user, store data in database
        } else {
            for (String id: bookStatusDto.getBooksIds()) {
                try {
                    bookStatusService.setStatus(Integer.valueOf(id), user.getId(), bookStatusDto.getStatus());
                } catch (NumberFormatException e){
                    //
                }
            }
            return ResponseEntity.ok(new ApiResponse(HttpStatus.OK, true));
        }
    }
}

