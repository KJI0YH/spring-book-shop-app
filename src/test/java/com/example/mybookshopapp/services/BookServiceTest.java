package com.example.mybookshopapp.services;

import com.example.mybookshopapp.SpringBootApplicationTest;
import com.example.mybookshopapp.data.Book2UserViewedEntity;
import com.example.mybookshopapp.data.BookEntity;
import com.example.mybookshopapp.data.BookReviewEntity;
import com.example.mybookshopapp.repositories.Book2UserViewedRepository;
import com.example.mybookshopapp.repositories.BookRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Sql(scripts = "classpath:/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:/delete-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class BookServiceTest extends SpringBootApplicationTest {
    private final BookRepository bookRepository;
    private final Book2UserViewedRepository book2UserViewedRepository;
    private final EntityManagerFactory entityManagerFactory;

    @Test
    void testGetPageOfRecentBooks() {
        Pageable nextPage = PageRequest.of(0, 3);
        List<BookEntity> bookList = bookRepository.findBooksByPubDateBetween(null, null, nextPage).getContent();
        Assertions.assertNotNull(bookList);
        Assertions.assertFalse(bookList.isEmpty());
        Assertions.assertEquals(3, bookList.size());
    }

    @Test
    void testGetPageOfPopularBooks() {
        Pageable nextPage = PageRequest.of(0, 3);
        List<BookEntity> bookList = bookRepository.findAllByOrderByPopularityDesc(nextPage).getContent();
        Assertions.assertNotNull(bookList);
        Assertions.assertFalse(bookList.isEmpty());
        Assertions.assertEquals(3, bookList.size());
    }

    @Test
    void testGetPageOfBooksByTagSlug() {
        Pageable nextPage = PageRequest.of(0, 3);
        List<BookEntity> bookList = bookRepository.findBooksByTagSlug("Tag-1", nextPage).getContent();
        Assertions.assertNotNull(bookList);
        Assertions.assertFalse(bookList.isEmpty());
        Assertions.assertEquals(3, bookList.size());
    }

    @Test
    void getPageOfBooksByTagId() {
        Pageable nextPage = PageRequest.of(0, 3);
        List<BookEntity> bookList = bookRepository.findBooksPageByTagId(1, nextPage).getContent();
        Assertions.assertNotNull(bookList);
        Assertions.assertFalse(bookList.isEmpty());
        Assertions.assertEquals(3, bookList.size());
    }

    @Test
    void testGetPageOfBooksByGenreSlug() {
        Pageable nextPage = PageRequest.of(0, 3);
        List<BookEntity> bookList = bookRepository.findBooksByGenreSlug("Genre-1", nextPage).getContent();
        Assertions.assertNotNull(bookList);
        Assertions.assertFalse(bookList.isEmpty());
        Assertions.assertEquals(3, bookList.size());
    }

    @Test
    void getPageOfBooksByGenreId() {
        Pageable nextPage = PageRequest.of(0, 3);
        List<BookEntity> bookList = bookRepository.findBooksPageByGenreId(1, nextPage).getContent();
        Assertions.assertNotNull(bookList);
        Assertions.assertFalse(bookList.isEmpty());
        Assertions.assertEquals(3, bookList.size());
    }

    @Test
    void testGetPageOfBooksByAuthorSlug() {
        Pageable nextPage = PageRequest.of(0, 3);
        List<BookEntity> bookList = bookRepository.findBooksByAuthorSlug("Author-1", nextPage).getContent();
        Assertions.assertNotNull(bookList);
        Assertions.assertFalse(bookList.isEmpty());
        Assertions.assertEquals(3, bookList.size());
    }

    @Test
    void getPageOfBooksByAuthorId() {
        Pageable nextPage = PageRequest.of(0, 3);
        List<BookEntity> bookList = bookRepository.findBooksPageByAuthorId(1, nextPage).getContent();
        Assertions.assertNotNull(bookList);
        Assertions.assertFalse(bookList.isEmpty());
        Assertions.assertEquals(3, bookList.size());
    }

    @Test
    void getPageOfViewedBooks() {
        Pageable nextPage = PageRequest.of(0, 3);
        List<BookEntity> bookList = book2UserViewedRepository.findAllByUserIdOrderByTimeDesc(1, nextPage)
                .getContent()
                .stream().map(Book2UserViewedEntity::getBook)
                .toList();
        Assertions.assertNotNull(bookList);
        Assertions.assertFalse(bookList.isEmpty());
        Assertions.assertEquals(3, bookList.size());
    }

    @Test
    void testGetBookBySlug() {
        BookEntity book = bookRepository.findBookEntityBySlug("Book-1");
        Assertions.assertNotNull(book);
        Assertions.assertEquals("Book-1", book.getSlug());
    }

    @Test
    void getAllBooksByUserStatus() {
        List<BookEntity> keptBooks = bookRepository.findBooksByUserType(1, 1);
        Assertions.assertNotNull(keptBooks);
        Assertions.assertFalse(keptBooks.isEmpty());

        List<BookEntity> cartBooks = bookRepository.findBooksByUserType(1, 2);
        Assertions.assertNotNull(cartBooks);
        Assertions.assertFalse(cartBooks.isEmpty());

        List<BookEntity> paidBooks = bookRepository.findBooksByUserType(1, 3);
        Assertions.assertNotNull(paidBooks);
        Assertions.assertFalse(paidBooks.isEmpty());

        List<BookEntity> archivedBooks = bookRepository.findBooksByUserType(1, 3);
        Assertions.assertNotNull(archivedBooks);
        Assertions.assertFalse(archivedBooks.isEmpty());
    }

    @Test
    void testGetBooksByIds() {
        Integer[] bookIds = new Integer[]{1, 2, 3};
        List<BookEntity> bookList = bookRepository.findBookEntitiesByIdIn(List.of(bookIds));
        Assertions.assertNotNull(bookList);
        Assertions.assertFalse(bookList.isEmpty());

        for (BookEntity book : bookList) {
            Assertions.assertTrue(Arrays.stream(bookIds).anyMatch(i -> Objects.equals(i, book.getId())));
        }
    }

    @Test
    void testBookReviewRating() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        TypedQuery<BookReviewEntity> reviewQuery = entityManager.createQuery(
                "SELECT r FROM BookReviewEntity r LEFT JOIN FETCH r.reviewLikeList WHERE r.book.id = 1", BookReviewEntity.class);
        List<BookReviewEntity> reviews = reviewQuery.getResultList();

        entityManager.getTransaction().commit();
        entityManager.close();

        Assertions.assertNotNull(reviews);
        Assertions.assertFalse(reviews.isEmpty());
        Collections.sort(reviews);

        long previousValue = reviews.get(0).getPopularityValue();
        for (BookReviewEntity bookReview : reviews) {
            Assertions.assertTrue(previousValue >= bookReview.getPopularityValue());
            previousValue = bookReview.getPopularityValue();
        }
    }
}