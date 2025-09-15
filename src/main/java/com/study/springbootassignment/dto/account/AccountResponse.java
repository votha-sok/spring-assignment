package com.study.springbootassignment.dto.account;

import com.study.springbootassignment.dto.user.UserDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponse extends AccountDto {
    private Long id;
    private String accountNumber;
    private Boolean status;
    private BigDecimal balance;
    private UserDto createBy;
    private LocalDateTime createdDate;
}
