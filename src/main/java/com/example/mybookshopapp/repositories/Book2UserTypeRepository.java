package com.example.mybookshopapp.repositories;

import com.example.mybookshopapp.data.Book2UserTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Book2UserTypeRepository extends JpaRepository<Book2UserTypeEntity, Integer> {
    Book2UserTypeEntity findBook2UserTypeEntityByCodeEqualsIgnoreCase(String code);
}
