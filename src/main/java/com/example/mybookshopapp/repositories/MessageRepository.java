package com.example.mybookshopapp.repositories;

import com.example.mybookshopapp.data.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<MessageEntity, Integer> {
}
