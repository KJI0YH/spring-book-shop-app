package com.example.mybookshopapp.services;

import com.example.mybookshopapp.data.*;
import com.example.mybookshopapp.repositories.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BookRecommendedService {
    private final UserService userService;
    private final BookRepository bookRepository;
    private final BookService bookService;

    public List<BookEntity> getPageOfRecommendedBooks(Integer offset, Integer limit) {
        UserEntity user = userService.getCurrentUser();
        if (user == null) {
            Pageable nextPage = PageRequest.of(offset, limit);
            return bookRepository.findAllByOrderByPopularityDesc(nextPage).getContent();
        }

        List<BookEntity> userBooks = getAllUserBook(user.getId());
        if (userBooks.isEmpty()) {
            Pageable nextPage = PageRequest.of(offset, limit);
            return bookRepository.findAllByOrderByPopularityDesc(nextPage).getContent();
        }

        List<Integer> authorIds = getAllAuthorIdsFromBooks(userBooks);
        List<Integer> genreIds = getAllGenreIdsFromBooks(userBooks);
        List<Integer> tagIds = getAllTagIdsFromBooks(userBooks);

        List<Integer> allRecommendedIds = bookRepository.findBookEntitiesIdsByAuthorIdsGenreIdsTagIds(authorIds, genreIds, tagIds);
        List<Integer> userBookIds = userBooks.stream()
                .map(BookEntity::getId)
                .toList();

        allRecommendedIds.removeIf(userBookIds::contains);
        int start = Math.min(offset * limit, allRecommendedIds.size());
        int end = Math.min(start + limit, allRecommendedIds.size());
        List<Integer> pageRecommendedIds = allRecommendedIds.subList(start, end);
        List<BookEntity> pageRecommendedBooks = bookRepository.findBookEntitiesByIdIn(allRecommendedIds.subList(start, end));
        return sortByIdsOrder(pageRecommendedBooks, pageRecommendedIds);
    }

    private List<BookEntity> getAllUserBook(Integer userId) {
        List<BookEntity> books = new ArrayList<>();
        books.addAll(bookService.getAllBooksByUserStatus(userId, "ARCHIVED"));
        books.addAll(bookService.getAllBooksByUserStatus(userId, "PAID"));
        books.addAll(bookService.getAllBooksByUserStatus(userId, "CART"));
        books.addAll(bookService.getAllBooksByUserStatus(userId, "KEPT"));
        return new HashSet<>(books).stream().toList();
    }

    private List<Integer> getAllAuthorIdsFromBooks(List<BookEntity> userBooks) {
        return userBooks.stream()
                .flatMap(book -> book.getAuthorList().stream()
                        .map(AuthorEntity::getId)
                )
                .distinct()
                .toList();
    }

    private List<Integer> getAllGenreIdsFromBooks(List<BookEntity> userBooks) {
        return userBooks.stream()
                .flatMap(book -> book.getGenreList().stream()
                        .map(GenreEntity::getId)
                )
                .distinct()
                .toList();
    }

    private List<Integer> getAllTagIdsFromBooks(List<BookEntity> userBooks) {
        return userBooks.stream()
                .flatMap(book -> book.getTagList().stream()
                        .map(TagEntity::getId)
                )
                .distinct()
                .toList();
    }

    private List<BookEntity> sortByIdsOrder(List<BookEntity> books, List<Integer> ids) {

        return ids.stream()
                .map(id -> books.stream().filter(book -> Objects.equals(book.getId(), id)).findFirst().orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
