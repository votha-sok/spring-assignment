package com.study.springbootassignment.service.serviceImp;

import com.study.springbootassignment.entity.TransactionEntity;
import com.study.springbootassignment.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransactionLogService {
    private final TransactionRepository transactionRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveFailedTransaction(TransactionEntity tx) {
        transactionRepository.save(tx);
    }
}
