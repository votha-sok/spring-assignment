package com.study.springbootassignment.dto.account;

import com.study.springbootassignment.util.AccountType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class AccountDto {
    private String accountHolderName;
    private String accountHolderEmail;
    private String accountHolderPhone;
    private String nationalId;
    @NotNull(message = "Account Type can not be null.")
    private AccountType accountType; // Enum
}
