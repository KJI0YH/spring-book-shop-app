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

    List<BookEntity> findBookEntitiesByPriceBetween(Integer min, Integer max);

    List<BookEntity> findBookEntitiesByPriceIs(Integer price);

    @Query("from BookEntity where isBestseller=1")
    List<BookEntity> getBestsellers();

    List<BookEntity> findBookEntitiesByTitleContainingIgnoreCase(String title);

    Page<BookEntity> findBookEntitiesByTitleContainingIgnoreCase(String bookTitle, Pageable nextPage);

    @Query(value = "select * FROM book WHERE pub_date BETWEEN COALESCE(?1, date '0001-01-01') AND COALESCE(?2, date '9999-12-31') ORDER BY pub_date DESC", nativeQuery = true)
    Page<BookEntity> findBooksByPubDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
}
