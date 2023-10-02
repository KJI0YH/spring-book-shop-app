package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.dto.BooksPageDto;
import com.example.mybookshopapp.dto.SearchWordDto;
import com.example.mybookshopapp.services.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApiSearchController {

    private final BookService bookService;
    
    @GetMapping("/{searchWord}")
    public BooksPageDto getNextSearchPage(@PathVariable(value = "searchWord", required = false) SearchWordDto searchWordDto,
                                          @RequestParam("offset") Integer offset,
                                          @RequestParam("limit") Integer limit) {
        return new BooksPageDto(bookService.getPageOfBooksByTitle(searchWordDto.getExample(), offset, limit).getContent());
    }

}
