package com.study.springbootassignment.dto.transaction;

import com.study.springbootassignment.entity.TransactionEntity;
import com.study.springbootassignment.util.TransactionStatus;
import com.study.springbootassignment.util.TransactionType;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
public class CreateTransfer extends TransactionDto {
    @NotBlank(message = "From Account number can not be null.")
    private String fromAccountNumber;
    @NotBlank(message = "To Account number can not be null.")
    private String toAccountNumber;
    public TransactionEntity toEntity() {
        TransactionEntity transaction = new TransactionEntity();
        transaction.setTransactionStatus(TransactionStatus.PENDING);
        transaction.setTransactionType(TransactionType.TRANSFER);
        transaction.setDescription(getDescription());
        transaction.setAmount(getAmount());
        transaction.setTimestamp(LocalDateTime.now());
        return transaction;
    }
}
