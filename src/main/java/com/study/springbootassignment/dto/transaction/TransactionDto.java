package com.study.springbootassignment.dto.transaction;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Data
@SuperBuilder
public class TransactionDto {
    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal amount;
    private String description;
}
