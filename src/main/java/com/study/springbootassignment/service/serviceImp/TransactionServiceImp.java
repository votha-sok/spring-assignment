package com.study.springbootassignment.service.serviceImp;

import com.study.springbootassignment.dto.transaction.CreateDeposit;
import com.study.springbootassignment.dto.transaction.CreateTransfer;
import com.study.springbootassignment.dto.transaction.CreateWithdraw;
import com.study.springbootassignment.entity.TransactionEntity;
import com.study.springbootassignment.repository.TransactionRepository;
import com.study.springbootassignment.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionServiceImp implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransferQueueManager transferQueueManager;
    private final TransactionProcessorService transactionProcessorService;

    @Override
    public TransactionEntity processTransfer(CreateTransfer request) {

        TransactionEntity savedTx = transactionRepository.save(request.toEntity());
        final Long transactionId = savedTx.getId();
        transferQueueManager.submit(request.getFromAccountNumber(), () -> {
            transactionProcessorService.handleTransfer(transactionId, request);
        });
        return savedTx;
    }

    @Override
    public TransactionEntity processDeposit(CreateDeposit request) {
        TransactionEntity savedTx = transactionRepository.save(request.toEntity());
        final Long transactionId = savedTx.getId();
        transferQueueManager.submit(request.getAccountNumber(), () -> {
            transactionProcessorService.handleDeposit(transactionId, request);
        });
        return savedTx;
    }

    @Override
    public TransactionEntity processWithdraw(CreateWithdraw request) {
        TransactionEntity savedTx = transactionRepository.save(request.toEntity());
        final Long transactionId = savedTx.getId();
        transferQueueManager.submit(request.getAccountNumber(), () -> {
            transactionProcessorService.handleWithdraw(transactionId, request);
        });
        return savedTx;
    }
}