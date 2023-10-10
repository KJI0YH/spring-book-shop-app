package com.example.mybookshopapp.repositories;

import com.example.mybookshopapp.data.BookEntity;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface BookRepository extends JpaRepository<BookEntity, Integer> {

    Page<BookEntity> findBookEntitiesByTitleContainingIgnoreCase(String bookTitle, Pageable nextPage);

    @Query(value = "select * FROM book WHERE pub_date BETWEEN COALESCE(?1, date '0001-01-01') AND COALESCE(?2, date '9999-12-31') ORDER BY pub_date DESC", nativeQuery = true)
    Page<BookEntity> findBooksByPubDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

    Page<BookEntity> findAllByOrderByPopularityDesc(Pageable pageable);

    @Query(value = "select b.* from book as b join book2tag as b2t on b2t.book_id = b.id join tag as t on b2t.tag_id = t.id where t.slug = ?1 order by b.pub_date desc", nativeQuery = true)
    Page<BookEntity> findBooksByTagSlug(String tagSlug, Pageable pageable);

    @Query(value = "select b.* from book as b join book2genre as b2g on b2g.book_id = b.id join genre as g on b2g.genre_id = g.id where g.slug = ?1 order by b.pub_date desc", nativeQuery = true)
    Page<BookEntity> findBooksByGenreSlug(String tagID, Pageable pageable);

    @Query(value = "select b.* from book as b join book2genre as b2g on b2g.book_id = b.id join genre as g on b2g.genre_id = g.id where g.id = ?1 order by b.pub_date desc", nativeQuery = true)
    Page<BookEntity> findBooksByGenreId(Integer genreId, Pageable pageable);

    @Query(value = "select b.* from book as b join book2author as b2a on b2a.book_id = b.id join author as a on b2a.author_id = a.id where a.slug = ?1 order by b.pub_date desc", nativeQuery = true)
    Page<BookEntity> findBooksByAuthorSlug(String authorSlug, Pageable pageable);

    @Query(value = "select b.* from book as b join book2author as b2a on b2a.book_id = b.id join author as a on b2a.author_id = a.id where a.id = ?1 order by b.pub_date desc", nativeQuery = true)
    Page<BookEntity> findBooksByAuthorId(Integer authorId, Pageable nextPage);

    BookEntity findBookEntityBySlug(String slug);

    List<BookEntity> findBookEntitiesByIdIn(Collection<Integer> id);

    BookEntity findBookEntityById(Integer id);

    @Query(value = "select distinct b.* from book as b join book2user as b2u on b2u.book_id = b.id join users as u on b2u.user_id = ?1 where b2u.type_id = ?2", nativeQuery = true)
    List<BookEntity> findBooksByUserType(Integer userId, Integer typeId);

    @Query(value = "select distinct b.* from book as b join book2user as b2u on b2u.book_id = b.id join users as u on b2u.user_id = ?1 where b2u.type_id = ?2", nativeQuery = true)
    Page<BookEntity> findBooksByUserType(Integer userId, Integer typeId, Pageable pageable);

    @Query(value = "SELECT COUNT(distinct b.id) FROM book AS b JOIN book2user AS b2u ON b2u.book_id = b.id JOIN users AS u ON b2u.user_id = ?1 WHERE b2u.type_id = ?2", nativeQuery = true)
    Long getCountOfBooksByUserType(Integer userId, Integer typeId);
}
