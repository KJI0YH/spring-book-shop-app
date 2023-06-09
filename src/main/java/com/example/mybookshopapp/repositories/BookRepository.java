package com.example.mybookshopapp.repositories;

import com.example.mybookshopapp.data.BookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.awt.print.Book;
import java.time.LocalDate;
import java.util.List;

public interface BookRepository extends JpaRepository<BookEntity, Integer> {

    List<BookEntity> findBookEntitiesByTitleContainingIgnoreCase(String title);

    List<BookEntity> findBookEntitiesByPriceBetween(Integer min, Integer max);

    List<BookEntity> findBookEntitiesByPriceIs(Integer price);

    @Query("from BookEntity where isBestseller=1")
    List<BookEntity> getBestsellers();

    @Query("from BookEntity where pubDate between ?1 and ?2")
    List<BookEntity> getRecents(LocalDate from, LocalDate to);

    Page<BookEntity> findBookEntitiesByTitleContainingIgnoreCase(String bookTitle, Pageable nextPage);
}
