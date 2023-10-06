package com.example.mybookshopapp.services;

import com.example.mybookshopapp.data.AuthorEntity;
import com.example.mybookshopapp.repositories.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthorService {

    private final AuthorRepository authorRepository;

    public List<AuthorEntity> getAuthorsData() {
        return authorRepository.findAll();
    }

    public Map<String, List<AuthorEntity>> getAuthorsMap() {
        List<AuthorEntity> authors = getAuthorsData();
        Map<String, List<AuthorEntity>> authorsMap = authors.stream().collect(groupingBy((AuthorEntity o) -> o.getLastName().toUpperCase().substring(0, 1)));

        // Sort each group by last name
        authorsMap.values().forEach(group -> group.sort(Comparator.comparing(AuthorEntity::getLastName)));

        return authorsMap;
    }

    public AuthorEntity getAuthorBySlug(String slug) {
        return authorRepository.findAuthorEntityBySlug(slug);
    }
}
