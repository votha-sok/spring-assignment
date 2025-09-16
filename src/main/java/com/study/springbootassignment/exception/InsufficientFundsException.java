package com.study.springbootassignment.exception;

import java.math.BigDecimal;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(String accountNumber, BigDecimal attempted, BigDecimal available) {
        super("Insufficient funds in account " + accountNumber + ". Attempted: " + attempted + ", Available: " + available);
    }
}
