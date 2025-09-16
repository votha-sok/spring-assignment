package com.study.springbootassignment.dto.transaction;

import com.study.springbootassignment.dto.user.UserMapper;
import com.study.springbootassignment.entity.TransactionEntity;

public class TransactionMapper {
    public static TransactionResponse toDto(TransactionEntity transaction) {
        return TransactionResponse.builder()
                .id(transaction.getId())
                .amount(transaction.getAmount())
                .description(transaction.getDescription())
                .fromAccountNumber(transaction.getFromAccountNumber())
                .toAccountNumber(transaction.getToAccountNumber())
                .transactionStatus(transaction.getTransactionStatus().toString())
                .processBy(UserMapper.toDto(transaction.getProcessedBy()))
                .timestamp(transaction.getTimestamp())
                .build();
    }
}
