package com.study.springbootassignment.service.serviceImp;

import com.study.springbootassignment.configuration.SpecificationBuilder;
import com.study.springbootassignment.dto.transaction.CreateDeposit;
import com.study.springbootassignment.dto.transaction.CreateTransfer;
import com.study.springbootassignment.dto.transaction.CreateWithdraw;
import com.study.springbootassignment.entity.TransactionEntity;
import com.study.springbootassignment.entity.UserEntity;
import com.study.springbootassignment.repository.TransactionRepository;
import com.study.springbootassignment.service.TransactionService;
import com.study.springbootassignment.service.que.DepositQueueProcessor;
import com.study.springbootassignment.service.que.TransferQueueProcessor;
import com.study.springbootassignment.service.que.WithdrawQueueProcessor;
import com.study.springbootassignment.util.RandomStringHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class TransactionServiceImp implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionProcessorService transactionProcessorService;


    @Override
    public CompletableFuture<TransactionEntity> processTransfer(CreateTransfer request) {
        // Save transaction first (status = PENDING)
        TransferQueueProcessor queueManager = new TransferQueueProcessor(transactionProcessorService);
        return queueManager.enqueue(request);
    }

    @Override
    public CompletableFuture<TransactionEntity> processDeposit(CreateDeposit request) {
        DepositQueueProcessor queueManager = new DepositQueueProcessor(transactionProcessorService);
        return queueManager.enqueue(request);
    }

    @Override
    public CompletableFuture<TransactionEntity> processWithdraw(CreateWithdraw request) {
        WithdrawQueueProcessor queueManager = new WithdrawQueueProcessor(transactionProcessorService);
        return queueManager.enqueue(request);
    }

    @Override
    public Page<TransactionEntity> list(Map<String, String> params, int page, int size) {
        Specification<TransactionEntity> spec = SpecificationBuilder.buildFromParams(params, TransactionEntity.class);
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString("DESC"), "id"));
        return transactionRepository.findAll(spec, pageable);
    }
}
