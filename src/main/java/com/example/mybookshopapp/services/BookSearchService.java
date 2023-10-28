package com.example.mybookshopapp.services;

import com.example.mybookshopapp.data.BookEntity;
import com.example.mybookshopapp.repositories.AuthorRepository;
import com.example.mybookshopapp.repositories.BookRepository;
import com.example.mybookshopapp.repositories.GenreRepository;
import com.example.mybookshopapp.repositories.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BookSearchService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final TagRepository tagRepository;

    public List<Integer> getBooksIdsByQuery(String query) {
        List<Integer> bookIds = new ArrayList<>();
        bookIds.addAll(getBooksIdsByTitle(query));
        bookIds.addAll(getBooksIdsByDescription(query));
        bookIds.addAll(getBooksIdsByAuthorName(query));
        bookIds.addAll(getBooksIdsByGenreName(query));
        bookIds.addAll(getBooksIdsByTagName(query));
        return new HashSet<>(bookIds).stream().toList();
    }

    public List<BookEntity> getPageOfBooksByQuery(String query, Integer offset, Integer limit) {
        List<Integer> bookIds = getBooksIdsByQuery(query);
        int start = Math.min(offset * limit, bookIds.size());
        int end = Math.min(start + limit, bookIds.size());
        return bookRepository.findBookEntitiesByIdIn(bookIds.subList(start, end));
    }

    public List<BookEntity> getPageOfBooks(List<Integer> bookIds, Integer offset, Integer limit) {
        int start = Math.min(offset * limit, bookIds.size());
        int end = Math.min(start + limit, bookIds.size());
        return bookRepository.findBookEntitiesByIdIn(bookIds.subList(start, end));
    }

    public List<Integer> getBooksIdsByTitle(String title) {
        return bookRepository.findBookEntitiesIdByTitleContainingIgnoreCase(title);
    }

    public List<Integer> getBooksIdsByDescription(String description) {
        return bookRepository.findBookEntitiesIdByDescriptionContainingIgnoreCase(description);
    }

    public List<Integer> getBooksIdsByGenreName(String genreName) {
        List<Integer> genreIds = getGenresIdsByName(genreName);
        if (genreIds.isEmpty()) return Collections.emptyList();
        return bookRepository.findBookEntitiesIdByGenreIdIn(genreIds);
    }

    public List<Integer> getBooksIdsByAuthorName(String authorName) {
        List<Integer> authorIds = getAuthorsIdsByName(authorName);
        if (authorIds.isEmpty()) return Collections.emptyList();
        return bookRepository.findBookEntitiesIdByAuthorIdIn(authorIds);
    }

    public List<Integer> getBooksIdsByTagName(String tagName) {
        List<Integer> tagIds = getTagsIdsByName(tagName);
        if (tagIds.isEmpty()) return Collections.emptyList();
        return bookRepository.findBookEntitiesIdByTagIdIn(tagIds);
    }


    private List<Integer> getGenresIdsByName(String name) {
        return genreRepository.findGenreEntitiesIdByNameContainingIgnoreCase(name);
    }

    private List<Integer> getTagsIdsByName(String name) {
        return tagRepository.findTagEntitiesIdByNameContainingIgnoreCase(name);
    }

    private List<Integer> getAuthorsIdsByName(String name) {
        return authorRepository.findAuthorEntitiesIdByFirstLastNameContainingIgnoreCase(name);
    }
}
