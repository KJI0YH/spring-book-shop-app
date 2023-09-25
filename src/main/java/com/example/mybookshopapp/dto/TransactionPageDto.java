package com.example.mybookshopapp.dto;

import com.example.mybookshopapp.data.BalanceTransactionEntity;
import lombok.Data;

import java.util.List;

@Data
public class TransactionPageDto {
    private List<BalanceTransactionEntity> transactions;
    private Integer count;

    public TransactionPageDto(List<BalanceTransactionEntity> transactions){
        this.transactions = transactions;
        this.count = transactions.size();
    }
}
