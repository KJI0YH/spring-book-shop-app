package com.example.mybookshopapp.services;

import com.example.mybookshopapp.data.MessageEntity;
import com.example.mybookshopapp.data.UserEntity;
import com.example.mybookshopapp.dto.MessageDto;
import com.example.mybookshopapp.repositories.MessageRepository;
import com.example.mybookshopapp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public void saveMessage(MessageDto messageDto){
        MessageEntity message = new MessageEntity();
        message.setName(messageDto.getName());
        message.setEmail(messageDto.getMail());
        message.setSubject(messageDto.getTitle());
        message.setText(messageDto.getMessage());
        message.setTime(LocalDateTime.now());
        message.setUserId(null);

        UserEntity user = userRepository.findUserEntityByEmail(messageDto.getMail());
        if (user != null) {
            message.setUserId(user.getId());
        }
        messageRepository.save(message);
    }
}
