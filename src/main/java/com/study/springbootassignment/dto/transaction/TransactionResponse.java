package com.study.springbootassignment.dto.transaction;

import com.study.springbootassignment.dto.account.AccountDto;
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
    private String transactionId;
    private String description;
    private AccountDto fromAccount;
    private AccountDto toAccount;
    private String transactionStatus;
    private String transactionType;
    private UserDto processBy;
    private LocalDateTime timestamp;

}
