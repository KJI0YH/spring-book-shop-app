package com.example.mybookshopapp.services;

import com.example.mybookshopapp.data.BookFileEntity;
import com.example.mybookshopapp.data.FileDownloadEntity;
import com.example.mybookshopapp.data.UserEntity;
import com.example.mybookshopapp.errors.ApiWrongParameterException;
import com.example.mybookshopapp.errors.FileDownloadException;
import com.example.mybookshopapp.errors.PaymentRequiredException;
import com.example.mybookshopapp.errors.UserUnauthorizedException;
import com.example.mybookshopapp.repositories.BookFileRepository;
import com.example.mybookshopapp.repositories.FileDownloadRepository;
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

    private final BookFileRepository bookFileRepository;
    private final FileDownloadRepository fileDownloadRepository;
    private final UserService userService;
    private final BookService bookService;
    @Value("${upload.book-covers}")
    String uploadBookCoversPath;
    @Value("${upload.author-covers}")
    String uploadAuthorCoversPath;
    @Value("${download.book-files}")
    String downloadBookFilesPath;

    public String saveNewBookImage(MultipartFile file, String bookSlug) throws IOException {
        String resourceURI = null;

        if (!file.isEmpty()) {
            if (!new File(uploadBookCoversPath).exists()) {
                Files.createDirectories(Paths.get(uploadBookCoversPath));
            }

            String fileName = bookSlug + "." + FilenameUtils.getExtension(file.getOriginalFilename());
            Path path = Paths.get(uploadBookCoversPath, fileName);
            resourceURI = "/book-covers/" + fileName;
            file.transferTo(path);
        }
        return resourceURI;
    }
    
    public String saveNewAuthorImage(MultipartFile file, String authorSlug) throws IOException{
        String resourceURI = null;

        if (!file.isEmpty()) {
            if (!new File(uploadAuthorCoversPath).exists()) {
                Files.createDirectories(Paths.get(uploadAuthorCoversPath));
            }

            String fileName = authorSlug + "." + FilenameUtils.getExtension(file.getOriginalFilename());
            Path path = Paths.get(uploadAuthorCoversPath, fileName);
            resourceURI = "/author-covers/" + fileName;
            file.transferTo(path);
        }
        return resourceURI;
    }

    public Path getBookFilePath(String hash) throws ApiWrongParameterException {
        BookFileEntity bookFile = bookFileRepository.findBookFileEntitiesByHash(hash);
        if (bookFile == null)
            throw new ApiWrongParameterException("Book file do not found");
        return Paths.get(bookFile.getPath());
    }

    public MediaType getBookFileMime(String hash) throws ApiWrongParameterException {
        BookFileEntity bookFile = bookFileRepository.findBookFileEntitiesByHash(hash);
        if (bookFile == null)
            throw new ApiWrongParameterException("Book file do not found");
        String mimeType = URLConnection.guessContentTypeFromName(Paths.get(bookFile.getPath()).getFileName().toString());
        if (mimeType != null) {
            return MediaType.parseMediaType(mimeType);
        } else {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }

    public byte[] getBookFileByteArray(String hash) throws UserUnauthorizedException, PaymentRequiredException, FileDownloadException, ApiWrongParameterException {
        UserEntity user = userService.getCurrentUser();
        if (user == null)
            throw new UserUnauthorizedException("Only authorized users can download the book");

        BookFileEntity bookFile = bookFileRepository.findBookFileEntitiesByHash(hash);
        if (bookFile == null)
            throw new ApiWrongParameterException("Book file do not found");

        if (!bookService.isBookPaid(bookFile.getBook().getId(), user.getId()))
            throw new PaymentRequiredException("You have to pay to download the book");

        Path path = Paths.get(downloadBookFilesPath, bookFile.getPath());
        byte[] fileBytes;
        try {
            fileBytes = Files.readAllBytes(path);
            updateFileDownloadStatistic(bookFile.getBook().getId(), user.getId());
        } catch (IOException e) {
            throw new FileDownloadException("Can not to download file");
        }
        return fileBytes;
    }

    private void updateFileDownloadStatistic(Integer bookId, Integer userId) {
        FileDownloadEntity fileDownloadStat = fileDownloadRepository.findFileDownloadEntityByBookIdAndUserId(bookId, userId);
        if (fileDownloadStat != null) {
            fileDownloadStat.setCount(fileDownloadStat.getCount() + 1);
        } else {
            fileDownloadStat = new FileDownloadEntity();
            fileDownloadStat.setBookId(bookId);
            fileDownloadStat.setUserId(userId);
            fileDownloadStat.setCount(1);
        }
        fileDownloadRepository.save(fileDownloadStat);
    }
}
