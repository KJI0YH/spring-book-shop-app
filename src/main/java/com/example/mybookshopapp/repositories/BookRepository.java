package com.example.mybookshopapp.repositories;

import com.example.mybookshopapp.data.BookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.awt.print.Book;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public interface BookRepository extends JpaRepository<BookEntity, Integer> {

    Page<BookEntity> findBookEntitiesByTitleContainingIgnoreCase(String bookTitle, Pageable nextPage);

    @Query(value = "select * FROM book WHERE pub_date BETWEEN COALESCE(?1, date '0001-01-01') AND COALESCE(?2, date '9999-12-31') ORDER BY pub_date DESC", nativeQuery = true)
    Page<BookEntity> findBooksByPubDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

    default Page<BookEntity> findPopularBooks(Pageable pageable) {
        List<BookEntity> allBooks = findAll();
        allBooks.sort(Comparator.comparingDouble(BookEntity::getPopularity).reversed());

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<BookEntity> popularBooks;

        if (allBooks.size() < startItem) {
            popularBooks = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, allBooks.size());
            popularBooks = allBooks.subList(startItem, toIndex);
        }

        return new PageImpl<>(popularBooks, pageable, allBooks.size());
    }
}
