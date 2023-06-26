package com.example.mybookshopapp.repositories;

import com.example.mybookshopapp.data.BookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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

    @Query(value = "select b.* from book as b join book2tag as b2t on b2t.book_id = b.id join tag as t on b2t.tag_id = t.id where t.slug = ?1 order by b.pub_date desc", nativeQuery = true)
    Page<BookEntity> findBooksByTagSlug(String tagSlug, Pageable pageable);

    @Query(value = "select b.* from book as b join book2genre as b2g on b2g.book_id = b.id join genre as g on b2g.genre_id = g.id where g.slug = ?1 order by b.pub_date desc", nativeQuery = true)
    Page<BookEntity> findBooksByGenreSlug(String tagID, Pageable pageable);

    @Query(value = "select b.* from book as b join book2author as b2a on b2a.book_id = b.id join author as a on b2a.author_id = a.id where a.slug = ?1 order by b.pub_date desc", nativeQuery = true)
    Page<BookEntity> findBooksByAuthorSlug(String id, Pageable pageable);

    BookEntity findBookEntityBySlug(String slug);

    List<BookEntity> findBookEntitiesBySlugIn(String[] slugs);

    List<BookEntity> findBookEntitiesByIdIn(Integer[] ids);

    BookEntity findBookEntityById(Integer id);
}
