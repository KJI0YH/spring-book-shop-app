package com.example.mybookshopapp.services;

import com.example.mybookshopapp.data.*;
import com.example.mybookshopapp.errors.ApiWrongParameterException;
import com.example.mybookshopapp.errors.FileDownloadException;
import com.example.mybookshopapp.errors.PaymentRequiredException;
import com.example.mybookshopapp.errors.UserUnauthorizedException;
import com.example.mybookshopapp.repositories.BookFileRepository;
import com.example.mybookshopapp.repositories.BookFileTypeRepository;
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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ResourceStorage {

    private final BookFileRepository bookFileRepository;
    private final FileDownloadRepository fileDownloadRepository;
    private final BookFileTypeRepository bookFileTypeRepository;
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

    public String saveNewAuthorImage(MultipartFile file, String authorSlug) throws IOException {
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

    public void saveNewBookFile(MultipartFile file, String bookSlug) throws IOException, ApiWrongParameterException {
        BookEntity book = bookService.getBookBySlug(bookSlug);
        if (book == null)
            throw new ApiWrongParameterException("Book with slug + " + bookSlug + " does not exists");

        if (!file.isEmpty()) {
            if (!new File(downloadBookFilesPath).exists()) {
                Files.createDirectories(Paths.get(downloadBookFilesPath));
            }

            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            BookFileType fileType = bookFileTypeRepository.findBookFileTypeByNameIgnoreCase(extension);

            if (fileType == null)
                throw new ApiWrongParameterException("Extension " + extension + " does not exists");

            String fileName = book.getSlug() + "." + extension;
            Path path = Paths.get(downloadBookFilesPath, fileName);
            file.transferTo(path);

            BookFileEntity bookFile = new BookFileEntity();
            bookFile.setBook(book);
            bookFile.setPath(fileName);
            bookFile.setTypeId(fileType.getId());
            bookFile.setHash(generateHash(bookFile));
            bookFileRepository.save(bookFile);
        }
    }

    private String generateHash(BookFileEntity bookFile) {
        String input = bookFile.getBook().getId() + ":" + bookFile.getPath() + ":" + bookFile.getBookFileExtensionString();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] inputBytes = input.getBytes(StandardCharsets.UTF_8);
            messageDigest.update(inputBytes);
            byte[] digest = messageDigest.digest();
            StringBuilder builder = new StringBuilder();
            for (byte b : digest) {
                builder.append(String.format("%02x", b));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            return bookFile.getPath();
        }
    }

    public void deleteBookFile(String bookFileHash) throws ApiWrongParameterException, IOException {
        BookFileEntity bookFile = bookFileRepository.findBookFileEntitiesByHash(bookFileHash);
        if (bookFile == null)
            throw new ApiWrongParameterException("Book file with hash " + bookFileHash + " does not exists");
        Path filePath = Paths.get(downloadBookFilesPath, bookFile.getPath());
        Files.delete(filePath);
        bookFileRepository.delete(bookFile);
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


    public List<BookFileEntity> getAllBookFiles(Integer bookId) {
        return bookFileRepository.findBookFileEntitiesByBookId(bookId); 
    }
}
