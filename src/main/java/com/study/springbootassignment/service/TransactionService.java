package com.study.springbootassignment.service;

import com.study.springbootassignment.dto.transaction.CreateDeposit;
import com.study.springbootassignment.dto.transaction.CreateTransfer;
import com.study.springbootassignment.dto.transaction.CreateWithdraw;
import com.study.springbootassignment.entity.TransactionEntity;
import org.springframework.stereotype.Service;

@Service
public interface TransactionService {
    TransactionEntity processTransfer(CreateTransfer request);
    TransactionEntity processDeposit(CreateDeposit request);
    TransactionEntity processWithdraw(CreateWithdraw request);
}
