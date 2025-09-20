package com.study.springbootassignment.dto.transaction;

import com.study.springbootassignment.dto.account.AccountMapper;
import com.study.springbootassignment.dto.user.UserMapper;
import com.study.springbootassignment.entity.TransactionEntity;

public class TransactionMapper {
    public static TransactionResponse toDto(TransactionEntity transaction) {
        return TransactionResponse.builder()
                .id(transaction.getId())
                .amount(transaction.getAmount())
                .transactionId(transaction.getTransactionId())
                .transactionType(transaction.getTransactionType().name())
                .description(transaction.getDescription())
                .fromAccount(
                        transaction.getFromAccount() != null
                                ? AccountMapper.toDto(transaction.getFromAccount())
                                : null
                )
                .toAccount(
                        transaction.getToAccount() != null
                                ? AccountMapper.toDto(transaction.getToAccount())
                                : null
                )
                .transactionStatus(transaction.getTransactionStatus().toString())
                .processBy(UserMapper.toDto(transaction.getProcessedBy()))
                .timestamp(transaction.getTimestamp())
                .build();
    }
}
