package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.services.AuthorService;
import com.example.mybookshopapp.services.ResourceStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/author")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthorController {
    
    private final ResourceStorage storage;
    private final AuthorService authorService;
    
    @PostMapping("/{authorSlug}/img/save")
    public String saveNewBookImage(@PathVariable("authorSlug") String authorSlug,
                                   @RequestParam("file") MultipartFile file) throws IOException {

        String filePath = storage.saveNewAuthorImage(file, authorSlug);
        authorService.updatePhoto(authorSlug, filePath);

        return ("redirect:/authors/" + authorSlug);
    }
}
