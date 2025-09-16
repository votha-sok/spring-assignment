package com.study.springbootassignment.dto.transaction;

import com.study.springbootassignment.dto.user.UserDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class TransactionResponse extends TransactionDto {
    private Long id;
    private String fromAccountNumber;
    private String toAccountNumber;
    private String transactionStatus;
    private String transactionType;
    private UserDto processBy;
    private LocalDateTime timestamp;

}
