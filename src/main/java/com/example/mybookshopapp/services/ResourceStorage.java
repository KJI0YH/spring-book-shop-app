package com.example.mybookshopapp.services;

import com.example.mybookshopapp.data.BookFileEntity;
import com.example.mybookshopapp.repositories.BookFileRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ResourceStorage {

    @Value("${upload.path}")
    String uploadPath;

    @Value("${download.path}")
    String downloadPath;

    private final BookFileRepository bookFileRepository;

    public String saveNewBookImage(MultipartFile file, String bookSlug) throws IOException {
        String resourceURI = null;

        if (!file.isEmpty()) {
            if (!new File(uploadPath).exists()) {
                Files.createDirectories(Paths.get(uploadPath));
            }

            String fileName = bookSlug + "." + FilenameUtils.getExtension(file.getOriginalFilename());
            Path path = Paths.get(uploadPath, fileName);
            resourceURI = "/book-covers/" + fileName;
            file.transferTo(path);
        }
        return resourceURI;
    }

    public Path getBookFilePath(String hash) {
        BookFileEntity bookFile = bookFileRepository.findBookFileEntitiesByHash(hash);
        return Paths.get(bookFile.getPath());
    }

    public MediaType getBookFileMime(String hash) {
        BookFileEntity bookFile = bookFileRepository.findBookFileEntitiesByHash(hash);
        String mimeType = URLConnection.guessContentTypeFromName(Paths.get(bookFile.getPath()).getFileName().toString());
        if (mimeType != null){
            return MediaType.parseMediaType(mimeType);
        } else {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }

    public byte[] getBookFileByteArray(String hash) throws IOException {
        BookFileEntity bookFile = bookFileRepository.findBookFileEntitiesByHash(hash);
        Path path = Paths.get(downloadPath, bookFile.getPath());
        return Files.readAllBytes(path);
    }
}
