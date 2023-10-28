package com.example.mybookshopapp.services;

import com.example.mybookshopapp.data.*;
import com.example.mybookshopapp.errors.ApiWrongParameterException;
import com.example.mybookshopapp.errors.PaymentRequiredException;
import com.example.mybookshopapp.errors.UserUnauthorizedException;
import com.example.mybookshopapp.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BookService {

    private final BookRepository bookRepository;
    private final Book2UserTypeRepository book2UserTypeRepository;
    private final Book2UserRepository book2UserRepository;
    private final Book2UserViewedRepository book2UserViewedRepository;
    private final BookRateRepository bookRateRepository;
    private final BookReviewRepository bookReviewRepository;
    private final BookReviewRateRepository bookReviewRateRepository;
    private final BalanceTransactionRepository transactionRepository;
    private final UserService userService;

    public List<BookEntity> getPageOfRecentBooks(LocalDate from, LocalDate to, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return setBook2UserStatus(bookRepository.findBooksByPubDateBetween(from, to, nextPage).getContent());
    }

    public List<BookEntity> getPageOfPopularBooks(Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return setBook2UserStatus(bookRepository.findAllByOrderByPopularityDesc(nextPage).getContent());
    }

    public List<BookEntity> getPageOfBooksByTagSlug(String tagSlug, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return setBook2UserStatus(bookRepository.findBooksByTagSlug(tagSlug, nextPage).getContent());
    }

    public List<BookEntity> getPageOfBooksByTagId(Integer tagId, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return setBook2UserStatus(bookRepository.findBooksByTagId(tagId, nextPage).getContent());
    }

    public List<BookEntity> getPageOfBooksByGenreSlug(String genreSlug, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return setBook2UserStatus(bookRepository.findBooksByGenreSlug(genreSlug, nextPage).getContent());
    }

    public List<BookEntity> getPageOfBooksByGenreId(Integer genreId, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return setBook2UserStatus(bookRepository.findBooksByGenreId(genreId, nextPage).getContent());
    }

    public List<BookEntity> getPageOfBooksByAuthorSlug(String authorSlug, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return setBook2UserStatus(bookRepository.findBooksByAuthorSlug(authorSlug, nextPage).getContent());
    }

    public List<BookEntity> getPageOfBooksByAuthorId(Integer authorId, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return setBook2UserStatus(bookRepository.findBooksByAuthorId(authorId, nextPage).getContent());
    }

    public List<BookEntity> getPageOfViewedBooks(Integer userId, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        Page<Book2UserViewedEntity> result = book2UserViewedRepository.findAllByUserIdOrderByTimeDesc(userId, nextPage);
        List<BookEntity> bookList = new ArrayList<>();
        for (Book2UserViewedEntity book2UserViewed : result) {
            bookList.add(book2UserViewed.getBook());
        }
        return setBook2UserStatus(bookList);
    }

    public BookEntity getBookBySlug(String slug) {
        return setBook2UserStatus(bookRepository.findBookEntityBySlug(slug));
    }

    public List<BookEntity> getAllBooksByUserStatus(Integer userId, String status) {
        Book2UserTypeEntity book2UserType = book2UserTypeRepository.findBook2UserTypeEntityByCodeEqualsIgnoreCase(status);
        if (book2UserType != null) {
            return bookRepository.findBooksByUserType(userId, book2UserType.getId());
        }
        return new ArrayList<>();
    }

    public List<BookEntity> getPageOfBooksByUserStatus(String status, Integer offset, Integer limit) throws UserUnauthorizedException {
        UserEntity user = userService.getCurrentUser();
        if (user == null)
            throw new UserUnauthorizedException("User not authorized");
        Pageable nextPage = PageRequest.of(offset, limit);
        Book2UserTypeEntity book2UserType = book2UserTypeRepository.findBook2UserTypeEntityByCodeEqualsIgnoreCase(status);
        return setBook2UserStatus(bookRepository.findBooksByUserType(user.getId(), book2UserType.getId(), nextPage).getContent());
    }

    public Long getCountOfBooksByUserStatus(Integer userId, String status) {
        Book2UserTypeEntity book2UserType = book2UserTypeRepository.findBook2UserTypeEntityByCodeEqualsIgnoreCase(status);
        if (book2UserType != null) {
            return bookRepository.getCountOfBooksByUserType(userId, book2UserType.getId());
        }
        return 0L;
    }

    public List<BookEntity> getBooksByIds(Integer[] bookIds) {
        return setBook2UserStatus(bookRepository.findBookEntitiesByIdIn(List.of(bookIds)));
    }

    // TODO refactor
    private List<BookEntity> setBook2UserStatus(List<BookEntity> books) {
        UserEntity user = userService.getCurrentUser();
        if (user != null) {
            Book2UserIdEntity id = new Book2UserIdEntity();
            id.setUserId(user.getId());
            for (BookEntity book : books) {
                id.setBookId(book.getId());
                Book2UserEntity book2user = book2UserRepository.findBook2UserEntityById(id);
                if (book2user != null) {
                    book.setStatus(book2user.getType().getCode());
                }
            }
        }
        return books;
    }

    private BookEntity setBook2UserStatus(BookEntity book) {
        UserEntity user = userService.getCurrentUser();
        if (user != null) {
            Book2UserIdEntity id = new Book2UserIdEntity(book.getId(), user.getId());
            Book2UserEntity book2user = book2UserRepository.findBook2UserEntityById(id);
            if (book2user != null) {
                book.setStatus(book2user.getType().getCode());
            }
        }
        return book;
    }

    public Book2UserViewedEntity setViewedBook(Integer userId, Integer bookId) {
        Book2UserIdEntity id = new Book2UserIdEntity(bookId, userId);
        Book2UserViewedEntity book2UserViewed = new Book2UserViewedEntity();
        book2UserViewed.setId(id);
        book2UserViewed.setTime(LocalDateTime.now());
        return book2UserViewedRepository.save(book2UserViewed);
    }

    public void updateBook2UserStatus(Integer bookId, Integer userId, String status) throws ApiWrongParameterException, PaymentRequiredException {
        Book2UserTypeEntity newBook2UserType = book2UserTypeRepository.findBook2UserTypeEntityByCodeEqualsIgnoreCase(status);
        if (newBook2UserType == null)
            throw new ApiWrongParameterException("Invalid status parameter value");

        Book2UserIdEntity book2UserId = new Book2UserIdEntity(bookId, userId);
        String newStatusCode = newBook2UserType.getCode();
        boolean isBookPaid = isBookPaid(bookId, userId);

        // Catch invalid statuses updates
        if ((newStatusCode.equals("PAID") || newStatusCode.equals("ARCHIVED")) && !isBookPaid) {
            throw new PaymentRequiredException("You need to pay for the book");
        } else if ((newStatusCode.equals("CART") ||
                newStatusCode.equals("KEPT") ||
                newStatusCode.equals("UNLINK")) &&
                isBookPaid) {
            throw new ApiWrongParameterException("You can not " + newStatusCode.toLowerCase() + " the paid book");
        }

        // Delete book2user relation
        if (newStatusCode.equals("UNLINK")) {
            book2UserRepository.delete(book2UserRepository.findBook2UserEntityById(book2UserId));
        }

        // Create or update book2user relation
        else {
            Book2UserEntity book2User = new Book2UserEntity();
            book2User.setId(book2UserId);
            book2User.setTime(LocalDateTime.now());
            book2User.setType(newBook2UserType);
            book2UserRepository.save(book2User);
        }
    }

    public void updateBook2UserStatuses(Collection<Integer> booksIds, Integer userId, String status) throws ApiWrongParameterException, PaymentRequiredException {
        for (Integer bookId : booksIds) {
            updateBook2UserStatus(bookId, userId, status);
        }
    }

    public void rateBook(Integer bookId, Integer value) throws ApiWrongParameterException, UserUnauthorizedException {
        UserEntity user = userService.getCurrentUser();
        if (user == null)
            throw new UserUnauthorizedException("Only authorized users can rate the book");

        if (value < 1 || value > 5)
            throw new ApiWrongParameterException("Invalid rate value");

        BookEntity book = bookRepository.findBookEntityById(bookId);
        if (book == null) {
            throw new ApiWrongParameterException("Invalid book id value");
        }

        BookRateIdEntity bookRateId = new BookRateIdEntity(bookId, user.getId());
        BookRateEntity bookRate = new BookRateEntity();
        bookRate.setId(bookRateId);
        bookRate.setRate(value);
        bookRateRepository.save(bookRate);
    }

    public void reviewBook(Integer bookId, String reviewText) throws ApiWrongParameterException, UserUnauthorizedException {
        UserEntity user = userService.getCurrentUser();
        if (user == null)
            throw new UserUnauthorizedException("Only authorized users can review the book");

        if (reviewText.isEmpty())
            throw new ApiWrongParameterException("The text of the review can not be empty");

        BookEntity book = bookRepository.findBookEntityById(bookId);
        if (book == null)
            throw new ApiWrongParameterException("Invalid book id parameter");

        BookReviewEntity bookReview = new BookReviewEntity();
        bookReview.setBook(book);
        bookReview.setUser(user);
        bookReview.setText(reviewText);
        bookReview.setTime(LocalDateTime.now());
        bookReviewRepository.save(bookReview);
    }

    public void rateBookReview(Integer reviewId, Integer value) throws ApiWrongParameterException, UserUnauthorizedException {
        UserEntity user = userService.getCurrentUser();
        if (user == null)
            throw new UserUnauthorizedException("Only authorized users can rate the book review");

        if (value != -1 && value != 1)
            throw new ApiWrongParameterException("Invalid rate value of a book review");

        Optional<BookReviewEntity> review = bookReviewRepository.findById(reviewId);
        if (review.isEmpty())
            throw new ApiWrongParameterException("Invalid review id parameter");

        BookReviewLikeIdEntity reviewLikeId = new BookReviewLikeIdEntity(reviewId, user.getId());
        BookReviewLikeEntity reviewLike = new BookReviewLikeEntity();
        reviewLike.setId(reviewLikeId);
        reviewLike.setValue(value);
        reviewLike.setTime(LocalDateTime.now());
        bookReviewRateRepository.save(reviewLike);
    }

    public boolean isBookPaid(Integer bookId, Integer userId) {
        return null != transactionRepository.findByBookIdAndUserId(bookId, userId);
    }

    public void mergeCartBooks(Integer[] bookIds, Integer userId) {
        for (Integer bookId : bookIds) {
            try {
                updateBook2UserStatus(bookId, userId, "CART");
            } catch (ApiWrongParameterException | PaymentRequiredException ignored) {
            }
        }
    }

    public void mergePostponedBooks(Integer[] bookIds, Integer userId) {
        for (Integer bookId : bookIds) {
            try {
                updateBook2UserStatus(bookId, userId, "KEPT");
            } catch (ApiWrongParameterException | PaymentRequiredException ignored) {
            }
        }
    }

    public void mergeViewedBooks(Integer[] bookIds, Integer userId) {
        for (Integer bookId : bookIds) {
            Book2UserViewedEntity viewedBook = book2UserViewedRepository.findBook2UserViewedEntityById(new Book2UserIdEntity(bookId, userId));
            if (viewedBook == null) {
                setViewedBook(userId, bookId);
            }
        }
    }
}