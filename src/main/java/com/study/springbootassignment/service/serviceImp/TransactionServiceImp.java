package com.study.springbootassignment.service.serviceImp;

import com.study.springbootassignment.configuration.SpecificationBuilder;
import com.study.springbootassignment.dto.transaction.CreateDeposit;
import com.study.springbootassignment.dto.transaction.CreateTransfer;
import com.study.springbootassignment.dto.transaction.CreateWithdraw;
import com.study.springbootassignment.entity.TransactionEntity;
import com.study.springbootassignment.entity.UserEntity;
import com.study.springbootassignment.repository.TransactionRepository;
import com.study.springbootassignment.service.TransactionService;
import com.study.springbootassignment.service.que.TransferQueueProcessor;
import com.study.springbootassignment.util.RandomStringHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TransactionServiceImp implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransferQueueManager transferQueueManager;
    private final TransactionProcessorService transactionProcessorService;
    private final RandomStringHelper randomStringHelper;

    @Override
    public void processTransfer(CreateTransfer request) {
        // Save transaction first (status = PENDING)
        TransferQueueProcessor queueManager = new TransferQueueProcessor(transactionProcessorService);
        queueManager.enqueue(request);
    }

    @Override
    public TransactionEntity processDeposit(CreateDeposit request) {
        TransactionEntity tx = request.toEntity();
        TransactionEntity savedTx = transactionRepository.save(tx);

        final Long txId = savedTx.getId();
//        transferQueueManager.submit(request.getAccountNumber(), () -> {
//            transactionProcessorService.handleDeposit(txId, request);
//        });

        return savedTx;
    }

    @Override
    public TransactionEntity processWithdraw(CreateWithdraw request) {
        TransactionEntity tx = request.toEntity();
        TransactionEntity savedTx = transactionRepository.save(tx);

        final Long txId = savedTx.getId();
//        transferQueueManager.submit(request.getAccountNumber(), () -> {
//            transactionProcessorService.handleWithdraw(txId, request);
//        });
        return savedTx;
    }

    @Override
    public Page<TransactionEntity> list(Map<String, String> params, int page, int size) {
        Specification<TransactionEntity> spec = SpecificationBuilder.buildFromParams(params, TransactionEntity.class);
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString("DESC"), "id"));
        return transactionRepository.findAll(spec, pageable);
    }
}
