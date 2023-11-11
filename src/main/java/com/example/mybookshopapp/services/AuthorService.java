package com.example.mybookshopapp.services;

import com.example.mybookshopapp.data.AuthorEntity;
import com.example.mybookshopapp.data.BookEntity;
import com.example.mybookshopapp.dto.AuthorDto;
import com.example.mybookshopapp.errors.ApiWrongParameterException;
import com.example.mybookshopapp.repositories.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthorService {

    private final AuthorRepository authorRepository;
    @Value("${upload.default-author-cover}")
    private String defaultCover;

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

    public List<AuthorEntity> getAllAuthors() {
        return authorRepository.findAll();
    }

    public AuthorEntity getAuthorById(Integer authorId) throws ApiWrongParameterException {
        AuthorEntity author = authorRepository.findAuthorEntityById(authorId);
        if (author == null)
            throw new ApiWrongParameterException("Author with id " + authorId + " does not exists");
        return author;
    }

    public AuthorEntity createAuthor(AuthorDto authorDto) throws ApiWrongParameterException {
        if (!StringUtils.isNotBlank(authorDto.getFirstName()) ||
                !StringUtils.isNotBlank(authorDto.getLastName()) ||
                !StringUtils.isNotBlank(authorDto.getSlug()))
            throw new ApiWrongParameterException("Invalid author parameters");
        AuthorEntity author = new AuthorEntity();
        author.setFirstName(authorDto.getFirstName());
        author.setLastName(authorDto.getLastName());
        author.setSlug(authorDto.getSlug());
        author.setDescription(authorDto.getDescription());

        if (StringUtils.isNotBlank(authorDto.getPhoto()))
            author.setPhoto(authorDto.getPhoto());
        else
            author.setPhoto(defaultCover);

        AuthorEntity newAuthor;
        try {
            newAuthor = authorRepository.save(author);
        } catch (Exception e) {
            throw new ApiWrongParameterException("Can not save author: " + e.getMessage());
        }

        return newAuthor;
    }

    public AuthorEntity updateAuthor(Integer authorId, AuthorDto authorDto) throws ApiWrongParameterException {
        AuthorEntity author = getAuthorById(authorId);
        if (StringUtils.isNotBlank(authorDto.getFirstName()))
            author.setFirstName(authorDto.getFirstName());
        if (StringUtils.isNotBlank(authorDto.getLastName()))
            author.setLastName(authorDto.getLastName());
        if (StringUtils.isNotBlank(authorDto.getSlug()))
            author.setSlug(authorDto.getSlug());
        if (authorDto.getDescription() != null)
            author.setDescription(authorDto.getDescription());
        if (StringUtils.isNotBlank(authorDto.getPhoto()))
            author.setPhoto(authorDto.getPhoto());

        try {
            authorRepository.save(author);
        } catch (Exception e) {
            throw new ApiWrongParameterException("Can not update author: " + e.getMessage());
        }
        return author;
    }

    public void deleteAuthor(Integer authorId) throws ApiWrongParameterException {
        AuthorEntity author = getAuthorById(authorId);
        authorRepository.delete(author);
    }

    public List<AuthorEntity> getAuthorsByIds(Integer[] authorIds) {
        return authorRepository.findAuthorEntitiesByIdIn(List.of(authorIds));
    }

    public void updatePhoto(String authorSlug, String filePath) {
        AuthorEntity author = authorRepository.findAuthorEntityBySlug(authorSlug);
        if (author == null) return;
        author.setPhoto(filePath);
        authorRepository.save(author);
    }
}
