package com.study.springbootassignment.dto.account;

import com.study.springbootassignment.dto.user.UserMapper;
import com.study.springbootassignment.entity.AccountEntity;

public class AccountMapper {
    public static AccountResponse toDto(AccountEntity account) {
        return AccountResponse.builder()
                .id(account.getId())
                .accountHolderName(account.getAccountHolderName())
                .accountHolderEmail(account.getAccountHolderEmail())
                .accountHolderPhone(account.getAccountHolderPhone())
                .nationalId(account.getNationalId())
                .accountType(account.getAccountType())
                .accountNumber(account.getAccountNumber())
                .status(account.getStatus())
                .balance(account.getBalance())
                .createBy(UserMapper.toDto(account.getCreatedBy()))
                .createdDate(account.getCreatedDate())
                .build();
    }
    public static AccountResponse toTransaction(AccountEntity account) {
        return AccountResponse.builder()
                .id(account.getId())
                .accountHolderName(account.getAccountHolderName())
                .accountHolderEmail(account.getAccountHolderEmail())
                .accountHolderPhone(account.getAccountHolderPhone())
                .accountNumber(account.getAccountNumber())
                .build();
    }
}
