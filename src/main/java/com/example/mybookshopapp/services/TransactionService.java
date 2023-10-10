package com.example.mybookshopapp.services;

import com.example.mybookshopapp.data.BalanceTransactionEntity;
import com.example.mybookshopapp.data.BookEntity;
import com.example.mybookshopapp.data.UserEntity;
import com.example.mybookshopapp.repositories.BalanceTransactionRepository;
import com.example.mybookshopapp.security.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TransactionService {
    private final BalanceTransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public Page<BalanceTransactionEntity> getTransactionsByUserAsc(UserEntity user, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return transactionRepository.findAllByUserOrderByTimeAsc(user, nextPage);
    }

    public Page<BalanceTransactionEntity> getTransactionsByUserDesc(UserEntity user, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return transactionRepository.findAllByUserOrderByTimeDesc(user, nextPage);
    }

    public void saveBookTransaction(UserEntity user, BookEntity book) {
        BalanceTransactionEntity transaction = new BalanceTransactionEntity();
        transaction.setBook(book);
        transaction.setUser(user);
        transaction.setValue(-book.getDiscountPrice());
        transaction.setTime(LocalDateTime.now());
        transaction.setDescription("Book purchase: " + book.getTitle());
        transactionRepository.save(transaction);
        user.setBalance(user.getBalance() - book.getDiscountPrice());
        userRepository.save(user);
    }

    public void saveBooksTransactions(UserEntity user, List<BookEntity> paidBooks) {
        for (BookEntity book : paidBooks) {
            saveBookTransaction(user, book);
        }
    }

    public boolean isBookPaid(Integer bookId, Integer userId) {
        return null != transactionRepository.findByBookIdAndUserId(bookId, userId);
    }

}
