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
public class CreateTransfer extends TransactionDto {
    @NotBlank
    private String fromAccountNumber;
    @NotBlank
    private String toAccountNumber;
    public TransactionEntity toEntity() {
        TransactionEntity transaction = new TransactionEntity();
        transaction.setTransactionStatus(TransactionStatus.PENDING);
        transaction.setTransactionType(TransactionType.TRANSFER);
        transaction.setFromAccountNumber(fromAccountNumber);
        transaction.setToAccountNumber(toAccountNumber);
        transaction.setDescription(getDescription());
        transaction.setAmount(getAmount());
        transaction.setTimestamp(LocalDateTime.now());
        return transaction;
    }
}
