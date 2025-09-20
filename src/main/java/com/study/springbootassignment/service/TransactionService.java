package com.study.springbootassignment.service;

import com.study.springbootassignment.dto.transaction.CreateDeposit;
import com.study.springbootassignment.dto.transaction.CreateTransfer;
import com.study.springbootassignment.dto.transaction.CreateWithdraw;
import com.study.springbootassignment.entity.TransactionEntity;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface TransactionService {
    void processTransfer(CreateTransfer request);
    TransactionEntity processDeposit(CreateDeposit request);
    TransactionEntity processWithdraw(CreateWithdraw request);
    Page<TransactionEntity> list(Map<String, String> params, int page, int size);
}
