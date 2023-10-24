package com.example.mybookshopapp.services;

import com.example.mybookshopapp.SpringBootApplicationTest;
import com.example.mybookshopapp.data.BookEntity;
import com.example.mybookshopapp.repositories.BookRepository;
import org.hibernate.Session;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Sql(scripts = "classpath:/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:/delete-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class BookServiceTest extends SpringBootApplicationTest {

    private final BookRepository bookRepository;

    @Autowired
    BookServiceTest(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Test
    void getPageOfRecommendedBooks() {
    }

    @Test
    void getPageOfBooksByTitle() {
        Pageable nextPage = PageRequest.of(0, 3);
        List<BookEntity> bookList = bookRepository.findBookEntitiesByTitleContainingIgnoreCase("title", nextPage).getContent();
        Assertions.assertNotNull(bookList);
        Assertions.assertFalse(bookList.isEmpty());

        for (BookEntity book : bookList) {
            Assertions.assertTrue(book.getTitle().contains("Title"));
        }
    }

    @Test
    void getPageOfRecentBooks() {
    }

    @Test
    void getPageOfPopularBooks() {
        Pageable nextPage = PageRequest.of(0, 3);
        List<BookEntity> bookList = bookRepository.findAllByOrderByPopularityDesc(nextPage).getContent();
        Assertions.assertNotNull(bookList);
        Assertions.assertFalse(bookList.isEmpty());

        Assertions.assertEquals(10, (int) bookList.get(0).getPopularity());
        Assertions.assertEquals(7, (int) bookList.get(1).getPopularity());
        Assertions.assertEquals(4, (int) bookList.get(2).getPopularity());
    }

    @Test
    void getPageOfBooksByTagSlug() {
        Pageable nextPage = PageRequest.of(0, 3);
        List<BookEntity> bookList = bookRepository.findBooksByTagSlug("Slug-1", nextPage).getContent();
        Assertions.assertNotNull(bookList);
        Assertions.assertFalse(bookList.isEmpty());

        // TODO fix lazy initialization
        Assertions.assertEquals(3, bookList.size());
    }

    @Test
    void getPageOfBooksByGenreSlug() {
        Pageable nextPage = PageRequest.of(0, 3);
        List<BookEntity> bookList = bookRepository.findBooksByGenreSlug("Slug-1", nextPage).getContent();
        Assertions.assertNotNull(bookList);
        Assertions.assertFalse(bookList.isEmpty());

        Assertions.assertEquals(3, bookList.size());

        // TODO fix lazy initialization
//        for (BookEntity book : bookList) {
//            Assertions.assertTrue(book.getGenreList().stream().anyMatch(g -> g.getSlug().equals("Slug-1")));
//        }
    }

    @Test
    void getPageOfBooksByAuthorSlug() {
        Pageable nextPage = PageRequest.of(0, 3);
        List<BookEntity> bookList = bookRepository.findBooksByAuthorSlug("Slug-1", nextPage).getContent();
        Assertions.assertNotNull(bookList);
        Assertions.assertFalse(bookList.isEmpty());

        Assertions.assertEquals(3, bookList.size());
    }

    @Test
    void getBookBySlug() {
        BookEntity book = bookRepository.findBookEntityBySlug("Slug-1");
        Assertions.assertNotNull(book);
        Assertions.assertEquals("Slug-1", book.getSlug());
    }

    @Test
    void getBooksByUserStatus() {
        List<BookEntity> keptBooks = bookRepository.findBooksByUserType(1, 1);
        Assertions.assertNotNull(keptBooks);
        Assertions.assertFalse(keptBooks.isEmpty());

        List<BookEntity> cartBooks = bookRepository.findBooksByUserType(1, 2);
        Assertions.assertNotNull(cartBooks);
        Assertions.assertFalse(cartBooks.isEmpty());

        List<BookEntity> paidBooks = bookRepository.findBooksByUserType(1, 3);
        Assertions.assertNotNull(paidBooks);
        Assertions.assertFalse(paidBooks.isEmpty());
    }

    @Test
    void getBooksByIds() {
        Integer[] bookIds = new Integer[]{1, 2, 3};
        List<BookEntity> bookList = bookRepository.findBookEntitiesByIdIn(List.of(bookIds));
        Assertions.assertNotNull(bookList);
        Assertions.assertFalse(bookList.isEmpty());

        for (BookEntity book : bookList) {
            Assertions.assertTrue(Arrays.stream(bookIds).anyMatch(i -> Objects.equals(i, book.getId())));
        }
    }
}