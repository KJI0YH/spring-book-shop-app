package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.dto.MessageDto;
import com.example.mybookshopapp.services.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApiContactsMessageController {

    private final MessageService messageService;

    @PostMapping(path = "/contacts/message",
    consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String handleContactMessage(MessageDto message){
        messageService.saveMessage(message);
        return "redirect:/contacts";
    }
}
