package com.example.mybookshopapp.services;

import com.example.mybookshopapp.data.Book2UserEntity;
import com.example.mybookshopapp.data.BookEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BooksRatingAndPopularityService {

    public Double getPopularity(BookEntity book) {
        List<Book2UserEntity> book2userList = book.getBook2userList();
        int kept = 0, cart = 0, paid = 0;
        for (Book2UserEntity book2user : book2userList) {
            switch (book2user.getType().getCode()) {
                case "KEPT" -> kept++;
                case "CART" -> cart++;
                case "PAID" -> paid++;
            }
        }
        return paid + 0.7 * cart + 0.4 * kept;
    }
}
