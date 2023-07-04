package com.example.mybookshopapp.services;

import com.example.mybookshopapp.SpringBootApplicationTest;
import com.example.mybookshopapp.data.*;
import com.example.mybookshopapp.repositories.*;
import com.example.mybookshopapp.security.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.awt.print.Book;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

class BookServiceTest extends SpringBootApplicationTest {

    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    private final Book2AuthorRepository book2AuthorRepository;

    private final UserRepository userRepository;

    private final Book2UserRepository book2UserRepository;

    private final Book2UserTypeRepository book2UserTypeRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    BookServiceTest(BookRepository bookRepository, AuthorRepository authorRepository, Book2AuthorRepository book2AuthorRepository, UserRepository userRepository, Book2UserRepository book2UserRepository, Book2UserTypeRepository book2UserTypeRepository, PasswordEncoder passwordEncoder) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.book2AuthorRepository = book2AuthorRepository;
        this.userRepository = userRepository;
        this.book2UserRepository = book2UserRepository;
        this.book2UserTypeRepository = book2UserTypeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @BeforeEach
    public void saveBooks() {

        // Create an author
        AuthorEntity authorEntity = AuthorEntity.builder()
                .photo("https://dummyimage.com/100/f1f1f1")
                .slug("Slug")
                .firstName("FirstName")
                .lastName("LastName")
                .description("Description")
                .build();
        authorRepository.save(authorEntity);

        // Create a user
        UserEntity userEntity = UserEntity.builder()
                .hash("hash")
                .password(passwordEncoder.encode("password"))
                .regTime(LocalDateTime.now())
                .balance(0)
                .email("email@email.com")
                .phone("+71111111111")
                .name("name")
                .build();
        userRepository.save(userEntity);

        // Create a book to user types
        book2UserTypeRepository.save(Book2UserTypeEntity.builder().code("KEPT").name("KEPT").build());
        book2UserTypeRepository.save(Book2UserTypeEntity.builder().code("CART").name("CART").build());
        book2UserTypeRepository.save(Book2UserTypeEntity.builder().code("PAID").name("PAID").build());

        for (int i = 1; i <= 3; i++) {

            // Create a book
            BookEntity bookEntity = BookEntity.builder()
                    .pubDate(LocalDate.now())
                    .isBestseller(i % 2 == 0)
                    .slug("Slug-" + i)
                    .title("Title-" + i)
                    .image("https://dummyimage.com/250/fcfcfc")
                    .description("Description-" + i)
                    .price(i * 50)
                    .discount(i * 15)
                    .build();
            bookRepository.save(bookEntity);

            // Creating a link between author and book
            Book2AuthorEntity book2Author = Book2AuthorEntity.builder()
                    .book(bookEntity)
                    .author(authorEntity)
                    .sortIndex(0)
                    .build();
            book2AuthorRepository.save(book2Author);

            // Creating a link between user and book
            Book2UserIdEntity book2UserId = Book2UserIdEntity.builder()
                    .userId(userEntity.getId())
                    .bookId(bookEntity.getId())
                    .build();
            Book2UserEntity book2user = Book2UserEntity.builder()
                    .id(book2UserId)
                    .user(userEntity)
                    .book(bookEntity)
                    .time(LocalDateTime.now())
                    .type(book2UserTypeRepository.findBook2UserTypeEntityById(i))
                    .build();
            book2UserRepository.save(book2user);
        }
    }

        @AfterEach
        public void dropData () {
            bookRepository.deleteAll();
            authorRepository.deleteAll();
            book2AuthorRepository.deleteAll();
            userRepository.deleteAll();
            book2UserRepository.deleteAll();
            book2UserTypeRepository.deleteAll();
        }

        @Test
        void getPageOfRecommendedBooks () {
        }

        @Test
        void getPageOfBooksByTitle () {
            Pageable nextPage = PageRequest.of(0, 3);
            List<BookEntity> bookList = bookRepository.findBookEntitiesByTitleContainingIgnoreCase("title", nextPage).getContent();
            Assertions.assertNotNull(bookList);
            Assertions.assertFalse(bookList.isEmpty());

            for (BookEntity book : bookList) {
                Assertions.assertTrue(book.getTitle().contains("Title"));
            }
        }

        @Test
        void getPageOfRecentBooks () {
        }

        @Test
        void getPageOfPopularBooks () {
            Pageable nextPage = PageRequest.of(0, 3);
            List<BookEntity> bookList = bookRepository.findAllByOrderByPopularityDesc(nextPage).getContent();
            Assertions.assertNotNull(bookList);
            Assertions.assertFalse(bookList.isEmpty());

            Assertions.assertEquals(4, (int) bookList.get(0).getPopularity());
            Assertions.assertEquals(7, (int) bookList.get(1).getPopularity());
            Assertions.assertEquals(10, (int) bookList.get(2).getPopularity());
        }

        @Test
        void getPageOfBooksByTagSlug () {
        }

        @Test
        void getPageOfBooksByGenreSlug () {
        }

        @Test
        void getPageOfBooksByAuthorSlug () {
            Pageable nextPage = PageRequest.of(0, 3);
            List<BookEntity> bookList = bookRepository.findBooksByAuthorSlug("Slug", nextPage).getContent();
            Assertions.assertNotNull(bookList);
            Assertions.assertFalse(bookList.isEmpty());

            Assertions.assertEquals(3, bookList.size());
        }

        @Test
        void getBookBySlug () {
            BookEntity book = bookRepository.findBookEntityBySlug("Slug-1");
            Assertions.assertNotNull(book);
            Assertions.assertEquals("Slug-1", book.getSlug());
        }

        @Test
        void getBooksByUserStatus () {
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
        void getBooksByIds () {
            Integer[] bookIds = new Integer[]{1, 2, 3};
            List<BookEntity> bookList = bookRepository.findBookEntitiesByIdIn(bookIds);
            Assertions.assertNotNull(bookList);
            Assertions.assertFalse(bookList.isEmpty());

            for (BookEntity book : bookList) {
                Assertions.assertTrue(Arrays.stream(bookIds).anyMatch(i -> Objects.equals(i, book.getId())));
            }
        }
    }