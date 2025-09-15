package com.study.springbootassignment.dto.account;

import com.study.springbootassignment.entity.AccountEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CreateAccount extends AccountDto {
    private Long id;
    public AccountEntity toEntity(){
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setId(id);
        accountEntity.setNationalId(getNationalId());
        accountEntity.setAccountType(getAccountType());
        accountEntity.setAccountHolderEmail(getAccountHolderEmail());
        accountEntity.setAccountHolderName(getAccountHolderName());
        accountEntity.setAccountHolderPhone(getAccountHolderPhone());
        return accountEntity;
    }
}
