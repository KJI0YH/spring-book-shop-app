package com.example.mybookshopapp.services;

import com.example.mybookshopapp.data.BalanceTransactionEntity;
import com.example.mybookshopapp.data.BookEntity;
import com.example.mybookshopapp.data.UserEntity;
import com.example.mybookshopapp.errors.ApiWrongParameterException;
import com.example.mybookshopapp.errors.BalanceNotEnoughException;
import com.example.mybookshopapp.repositories.BalanceTransactionRepository;
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
    private final BookService bookService;
    private final BalanceTransactionRepository transactionRepository;

    private Page<BalanceTransactionEntity> getTransactionsByUserAsc(UserEntity user, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return transactionRepository.findAllByUserOrderByTimeAsc(user, nextPage);
    }

    private Page<BalanceTransactionEntity> getTransactionsByUserDesc(UserEntity user, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return transactionRepository.findAllByUserOrderByTimeDesc(user, nextPage);
    }

    public Page<BalanceTransactionEntity> getTransactionByUser(UserEntity user, Integer offset, Integer limit, String order) {
        if (order.equals("asc"))
            return getTransactionsByUserAsc(user, offset, limit);
        else
            return getTransactionsByUserDesc(user, offset, limit);
    }

    public void saveBookTransaction(UserEntity user, BookEntity book) {
        BalanceTransactionEntity transaction = new BalanceTransactionEntity();
        transaction.setBook(book);
        transaction.setUser(user);
        transaction.setValue(-book.getDiscountPrice());
        transaction.setTime(LocalDateTime.now());
        transaction.setDescription("Book purchase: " + book.getTitle());
        transactionRepository.save(transaction);
    }

    public void saveReplenishmentTransaction(UserEntity user, Integer sum, String description) {
        BalanceTransactionEntity transaction = new BalanceTransactionEntity();
        transaction.setBook(null);
        transaction.setUser(user);
        transaction.setValue(sum);
        transaction.setTime(LocalDateTime.now());
        transaction.setDescription(description);
        transactionRepository.save(transaction);
    }

    public void saveBooksTransactions(UserEntity user, List<BookEntity> paidBooks) {
        for (BookEntity book : paidBooks) {
            saveBookTransaction(user, book);
        }
    }

    public void cartBooksPayment(UserEntity user) throws BalanceNotEnoughException, ApiWrongParameterException {
        List<BookEntity> cartBooks = bookService.getAllBooksByUserStatus(user.getId(), "CART");
        Integer paymentAmount = cartBooks.stream().mapToInt(BookEntity::getDiscountPrice).sum();

        if (isBalanceEnough(user, paymentAmount)) {
            saveBooksTransactions(user, cartBooks);
        } else {
            int lack = paymentAmount - user.getBalance();
            throw new BalanceNotEnoughException("Insufficient funds. Refill to " + lack / 100 + '.' + lack % 100 + " rubles");
        }
    }

    private boolean isBalanceEnough(UserEntity user, Integer paymentAmount) {
        return paymentAmount <= user.getBalance();
    }
}
