package com.study.springbootassignment.dto.transaction;

import com.study.springbootassignment.entity.TransactionEntity;
import com.study.springbootassignment.util.TransactionStatus;
import com.study.springbootassignment.util.TransactionType;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class CreateDeposit extends TransactionDto {
    @NotBlank(message = "Account number can not be null.")
    private String accountNumber;
    public TransactionEntity toEntity() {
        TransactionEntity transaction = new TransactionEntity();
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setTransactionStatus(TransactionStatus.PENDING);
        transaction.setToAccountNumber(accountNumber);
        transaction.setDescription(getDescription());
        transaction.setAmount(getAmount());
        transaction.setTimestamp(LocalDateTime.now());
        return transaction;
    }
}
