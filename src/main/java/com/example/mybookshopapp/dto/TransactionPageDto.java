package com.example.mybookshopapp.dto;

import com.example.mybookshopapp.data.BalanceTransactionEntity;
import lombok.Data;

import java.util.List;

@Data
public class TransactionPageDto {
    private Integer count;
    private List<BalanceTransactionEntity> transactions;

    public TransactionPageDto(List<BalanceTransactionEntity> transactions) {
        this.count = transactions.size();
        this.transactions = transactions;
    }
}
