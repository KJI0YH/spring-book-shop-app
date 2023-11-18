package com.example.mybookshopapp.services;

import com.example.mybookshopapp.data.*;
import com.example.mybookshopapp.dto.AuthorSortIndexDto;
import com.example.mybookshopapp.dto.Book2AuthorDto;
import com.example.mybookshopapp.dto.Book2UserDto;
import com.example.mybookshopapp.dto.BookDto;
import com.example.mybookshopapp.errors.ApiWrongParameterException;
import com.example.mybookshopapp.errors.PaymentRequiredException;
import com.example.mybookshopapp.errors.UserUnauthorizedException;
import com.example.mybookshopapp.repositories.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    private final Book2TagRepository book2TagRepository;
    private final Book2GenreRepository book2GenreRepository;
    private final Book2AuthorRepository book2AuthorRepository;
    private final BookRateRepository bookRateRepository;
    private final BookReviewRepository bookReviewRepository;
    private final BookReviewRateRepository bookReviewRateRepository;
    private final BalanceTransactionRepository transactionRepository;
    private final UserService userService;
    private final DateService dateService;
    private final TagService tagService;
    private final GenreService genreService;
    private final AuthorService authorService;
    @Value("${upload.default-book-cover}")
    private String defaultCover;

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
        return setBook2UserStatus(bookRepository.findBooksPageByTagId(tagId, nextPage).getContent());
    }

    public List<BookEntity> getPageOfBooksByGenreSlug(String genreSlug, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return setBook2UserStatus(bookRepository.findBooksByGenreSlug(genreSlug, nextPage).getContent());
    }

    public List<BookEntity> getPageOfBooksByGenreId(Integer genreId, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return setBook2UserStatus(bookRepository.findBooksPageByGenreId(genreId, nextPage).getContent());
    }

    public List<BookEntity> getPageOfBooksByAuthorSlug(String authorSlug, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return setBook2UserStatus(bookRepository.findBooksByAuthorSlug(authorSlug, nextPage).getContent());
    }

    public List<BookEntity> getPageOfBooksByAuthorId(Integer authorId, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return setBook2UserStatus(bookRepository.findBooksPageByAuthorId(authorId, nextPage).getContent());
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
        if (user != null && book != null) {
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

    public boolean isUserBook(Integer bookId, Integer userId) {
        Book2UserEntity book2User = book2UserRepository.findBook2UserEntityById(new Book2UserIdEntity(bookId, userId));
        if (book2User != null) {
            String book2UserName = book2User.getType().getName();
            return book2UserName.equals("PAID") || book2UserName.equals("ARCHIVED");
        }
        return false;
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

    public List<BookEntity> getAllBooks() {
        return bookRepository.findAll();
    }

    public BookEntity getBookById(Integer bookId) throws ApiWrongParameterException {
        BookEntity book = bookRepository.findBookEntityById(bookId);
        if (book == null)
            throw new ApiWrongParameterException("Book with id " + bookId + " does not exists");
        return book;
    }

    public BookEntity createBook(BookDto bookDto) throws ApiWrongParameterException {
        LocalDate pubDate = dateService.convertToLocalDate(bookDto.getPubDate());
        if (!StringUtils.isNotBlank(bookDto.getTitle()) ||
                !StringUtils.isNotBlank(bookDto.getSlug()) ||
                pubDate == null ||
                bookDto.getPrice() == null
        )
            throw new ApiWrongParameterException("Invalid book parameters");

        BookEntity book = new BookEntity();
        book.setTitle(bookDto.getTitle());
        book.setDescription(bookDto.getDescription());
        book.setPubDate(pubDate);
        book.setBestseller(bookDto.getIsBestseller() != null && bookDto.getIsBestseller());
        book.setSlug(bookDto.getSlug());
        book.setPrice(bookDto.getPrice());
        book.setDiscount(bookDto.getDiscount() == null ? 0 : bookDto.getDiscount());

        if (StringUtils.isNotBlank(bookDto.getImage()))
            book.setImage(bookDto.getImage());
        else
            book.setImage(defaultCover);

        BookEntity newBook;
        try {
            newBook = bookRepository.save(book);

            if (bookDto.getTagIds() != null && bookDto.getTagIds().length > 0) {
                createBook2Tag(new Integer[]{newBook.getId()}, bookDto.getTagIds());
            }
            if (bookDto.getGenreIds() != null && bookDto.getGenreIds().length > 0) {
                createBook2Genre(new Integer[]{newBook.getId()}, bookDto.getGenreIds());
            }
            if (bookDto.getAuthorIds() != null && bookDto.getAuthorIds().length > 0) {
                createBook2Author(new Integer[]{newBook.getId()}, bookDto.getAuthorIds());
            }


        } catch (Exception e) {
            throw new ApiWrongParameterException("Can not save the book: " + e.getMessage());
        }
        return newBook;
    }

    public BookEntity updateBook(Integer bookId, BookDto bookDto) throws ApiWrongParameterException {
        BookEntity book = getBookById(bookId);
        LocalDate pubDate = dateService.convertToLocalDate(bookDto.getPubDate());
        if (pubDate != null)
            book.setPubDate(pubDate);
        if (bookDto.getIsBestseller() != null)
            book.setBestseller(bookDto.getIsBestseller());
        if (StringUtils.isNotBlank(bookDto.getSlug()))
            book.setSlug(bookDto.getSlug());
        if (StringUtils.isNotBlank(bookDto.getTitle()))
            book.setTitle(bookDto.getTitle());
        if (StringUtils.isNotBlank(bookDto.getImage()))
            book.setImage(bookDto.getImage());
        if (bookDto.getDescription() != null)
            book.setDescription(bookDto.getDescription());
        if (bookDto.getPrice() != null)
            book.setPrice(bookDto.getPrice());
        if (bookDto.getDiscount() != null)
            book.setDiscount(bookDto.getDiscount());
        try {
            bookRepository.save(book);
        } catch (Exception e) {
            throw new ApiWrongParameterException("Can not update the book: " + e.getMessage());
        }
        return book;
    }

    public void deleteBook(Integer bookId) throws ApiWrongParameterException {
        BookEntity book = getBookById(bookId);
        bookRepository.delete(book);
    }

    public List<BookEntity> getBooksByTagId(Integer tagId) {
        return bookRepository.findBooksByTagId(tagId);
    }

    public List<Book2TagEntity> createBook2Tag(Integer[] bookIds, Integer[] tagIds) {
        List<BookEntity> books = getBooksByIds(bookIds);
        List<TagEntity> tags = tagService.getTagsByIds(tagIds);
        List<Book2TagEntity> book2TagList = new ArrayList<>();
        for (BookEntity book : books) {
            for (TagEntity tag : tags) {
                if (book2TagRepository.findBook2TagEntityByBookIdAndTagId(book.getId(), tag.getId()) == null) {
                    Book2TagEntity book2Tag = new Book2TagEntity();
                    book2Tag.setBook(book);
                    book2Tag.setTag(tag);
                    try {
                        book2TagList.add(book2TagRepository.save(book2Tag));
                    } catch (Exception ignored) {
                    }
                }
            }
        }
        return book2TagList;
    }

    public void deleteBook2Tag(Integer bookId, Integer tagId) throws ApiWrongParameterException {
        Book2TagEntity book2Tag = book2TagRepository.findBook2TagEntityByBookIdAndTagId(bookId, tagId);
        if (book2Tag == null)
            throw new ApiWrongParameterException("Book id " + bookId + " with tag id " + tagId + " does not exists");
        book2TagRepository.delete(book2Tag);
    }

    public List<BookEntity> getBooksByGenreId(Integer genreId) {
        return bookRepository.findBooksByGenreId(genreId);
    }

    public List<Book2GenreEntity> createBook2Genre(Integer[] bookIds, Integer[] genreIds) {
        List<BookEntity> books = getBooksByIds(bookIds);
        List<GenreEntity> genres = genreService.getGenresByIds(genreIds);
        List<Book2GenreEntity> book2GenreList = new ArrayList<>();
        for (BookEntity book : books) {
            for (GenreEntity genre : genres) {
                if (book2GenreRepository.findBook2GenreEntityByBookIdAndGenreId(book.getId(), genre.getId()) == null) {
                    Book2GenreEntity book2Genre = new Book2GenreEntity();
                    book2Genre.setBook(book);
                    book2Genre.setGenre(genre);
                    try {
                        book2GenreList.add(book2GenreRepository.save(book2Genre));
                    } catch (Exception ignored) {
                    }
                }
            }
        }
        return book2GenreList;
    }

    public void deleteBook2Genre(Integer bookId, Integer genreId) throws ApiWrongParameterException {
        Book2GenreEntity book2Genre = book2GenreRepository.findBook2GenreEntityByBookIdAndGenreId(bookId, genreId);
        if (book2Genre == null)
            throw new ApiWrongParameterException("Book id " + bookId + " with genre id " + genreId + " does not exists");
        book2GenreRepository.delete(book2Genre);
    }

    public List<BookEntity> getBooksByAuthorId(Integer authorId) {
        return bookRepository.findBooksByAuthorId(authorId);
    }

    public List<Book2AuthorEntity> createBook2Author(Integer[] bookIds, AuthorSortIndexDto[] authorSortIndexDtos) {
        List<BookEntity> books = getBooksByIds(bookIds);
        List<Book2AuthorEntity> book2AuthorList = new ArrayList<>();
        for (BookEntity book : books) {
            for (AuthorSortIndexDto authorSortIndex : authorSortIndexDtos) {
                try {
                    if (book2AuthorRepository.findBook2AuthorEntityByBookIdAndAuthorId(book.getId(), authorSortIndex.getAuthorId()) == null) {
                        AuthorEntity author = authorService.getAuthorById(authorSortIndex.getAuthorId());
                        Book2AuthorEntity book2Author = new Book2AuthorEntity();
                        book2Author.setBook(book);
                        book2Author.setAuthor(author);
                        Integer index = authorSortIndex.getSortIndex();
                        book2Author.setSortIndex(index == null ? 0 : index);
                        book2AuthorList.add(book2AuthorRepository.save(book2Author));
                    }
                } catch (ApiWrongParameterException ignored) {
                }
            }
        }
        return book2AuthorList;
    }

    public List<Book2AuthorEntity> updateBook2Author(Book2AuthorDto book2AuthorDto) throws ApiWrongParameterException {
        List<Book2AuthorEntity> book2AuthorList = new ArrayList<>();
        for (Integer bookId : book2AuthorDto.getBookIds()) {
            for (AuthorSortIndexDto authorSortIndex : book2AuthorDto.getAuthors()) {
                Book2AuthorEntity book2Author = book2AuthorRepository.findBook2AuthorEntityByBookIdAndAuthorId(bookId, authorSortIndex.getAuthorId());
                if (book2Author != null) {
                    Integer index = authorSortIndex.getSortIndex();
                    book2Author.setSortIndex(index == null ? 0 : index);
                    try {
                        book2AuthorList.add(book2AuthorRepository.save(book2Author));
                    } catch (Exception e) {
                        throw new ApiWrongParameterException("Can not update book to author: " + e.getMessage());
                    }
                }
            }
        }
        return book2AuthorList;
    }

    public void deleteBook2Author(Integer bookId, Integer authorId) throws ApiWrongParameterException {
        Book2AuthorEntity book2Author = book2AuthorRepository.findBook2AuthorEntityByBookIdAndAuthorId(bookId, authorId);
        if (book2Author == null)
            throw new ApiWrongParameterException("Book id " + bookId + " with author id " + authorId + " does not exists");
        book2AuthorRepository.delete(book2Author);
    }

    public List<BookEntity> getAllUserBooks(Integer userId) {
        return bookRepository.findBooksByUserId(userId);
    }

    public List<Book2UserEntity> createBook2User(Book2UserDto book2UserDto) throws ApiWrongParameterException {
        List<BookEntity> books = getBooksByIds(book2UserDto.getBookIds());
        Book2UserTypeEntity type = book2UserTypeRepository.findBook2UserTypeEntityByCodeEqualsIgnoreCase(book2UserDto.getStatus());
        if (type == null)
            throw new ApiWrongParameterException("Status " + book2UserDto.getStatus() + " does not exists");
        UserEntity user = userService.getUserById(book2UserDto.getUserId());
        if (user == null)
            throw new ApiWrongParameterException("User with id " + book2UserDto.getUserId() + " does not exists");
        List<Book2UserEntity> book2UserList = new ArrayList<>();
        for (BookEntity book : books) {
            Book2UserEntity book2User = new Book2UserEntity();
            book2User.setId(new Book2UserIdEntity(book.getId(), user.getId()));
            book2User.setBook(book);
            book2User.setUser(user);
            book2User.setType(type);
            book2User.setTime(LocalDateTime.now());
            try {
                book2UserList.add(book2UserRepository.save(book2User));
            } catch (Exception ignored) {
            }
        }
        return book2UserList;
    }

    public List<Book2UserEntity> updateBook2User(Book2UserDto book2UserDto) throws ApiWrongParameterException {
        List<BookEntity> books = getBooksByIds(book2UserDto.getBookIds());
        Book2UserTypeEntity type = book2UserTypeRepository.findBook2UserTypeEntityByCodeEqualsIgnoreCase(book2UserDto.getStatus());
        if (type == null)
            throw new ApiWrongParameterException("Status " + book2UserDto.getStatus() + " does not exists");
        UserEntity user = userService.getUserById(book2UserDto.getUserId());
        if (user == null)
            throw new ApiWrongParameterException("User with id " + book2UserDto.getUserId() + " does not exists");
        List<Book2UserEntity> book2UserList = new ArrayList<>();
        for (BookEntity book : books) {
            Book2UserEntity book2User = book2UserRepository.findBook2UserEntityById(new Book2UserIdEntity(book.getId(), user.getId()));
            if (book2User != null) {
                book2User.setType(type);
                try {
                    book2UserList.add(book2UserRepository.save(book2User));
                } catch (Exception ignored) {
                }
            }
        }
        return book2UserList;
    }

    public void deleteBook2User(Integer bookId, Integer userId) throws ApiWrongParameterException {
        Book2UserEntity book2User = book2UserRepository.findBook2UserEntityById(new Book2UserIdEntity(bookId, userId));
        if (book2User == null)
            throw new ApiWrongParameterException("User with id " + userId + " does not have a book with id " + bookId);
        book2UserRepository.delete(book2User);
    }

    public void updateImage(String bookSlug, String filePath) {
        BookEntity book = bookRepository.findBookEntityBySlug(bookSlug);
        if (book == null) return;
        book.setImage(filePath);
        bookRepository.save(book);
    }
}