package com.example.mybookshopapp.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.*;

@Controller
@RequestMapping("/api")
public class BookStatusController {

    @PostMapping("/changeBookStatus")
    @ResponseBody
    public String handleChangeBookStatus(HttpServletRequest request,
                                         HttpServletResponse response,
                                         @CookieValue(value = "cartContents", required = false) String cartContents ,
                                         @CookieValue(value = "postponedContents", required = false) String postponedContents) throws JsonProcessingException {

        //TODO: change status for registered users in db

        Map<String, String[]> params =  request.getParameterMap();

        String status = request.getParameter("status");
        String[] booksIds = params.get("booksIds[]");

        ArrayList<String> cartBooks = new ArrayList<>();
        if (cartContents != null && !cartContents.equals("")){
            cartBooks = new ArrayList<>(Arrays.asList(cartContents.split("/")));
        }

        ArrayList<String> postponedBooks = new ArrayList<>();
        if (postponedContents != null && !postponedContents.equals("")){
            postponedBooks = new ArrayList<>(Arrays.asList(postponedContents.split("/")));
        }

        switch (status){
            case "KEPT":
                for (String id: booksIds){
                    if (!postponedBooks.contains(id)){
                        postponedBooks.add(id);
                    }
                    cartBooks.remove(id);
                }
                break;
            case "CART":
                for (String id: booksIds){
                    if (!cartBooks.contains(id)){
                        cartBooks.add(id);
                    }
                    postponedBooks.remove(id);
                }
                break;
            case "UNLINK":
                for (String id: booksIds) {
                    cartBooks.remove(id);
                    postponedBooks.remove(id);
                }
                break;
        }

        Cookie cartCookie = new Cookie("cartContents", String.join("/", cartBooks));
        cartCookie.setPath("/");
        response.addCookie(cartCookie);

        Cookie postponedCookie = new Cookie("postponedContents", String.join("/", postponedBooks));
        postponedCookie.setPath("/");
        response.addCookie(postponedCookie);

        Map<String, Object> object = new HashMap<>();
        object.put("result", true);

        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
}
