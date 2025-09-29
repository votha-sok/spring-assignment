package com.study.springbootassignment.service.serviceImp;

import com.study.springbootassignment.dto.transaction.CreateDeposit;
import com.study.springbootassignment.dto.transaction.CreateTransfer;
import com.study.springbootassignment.dto.transaction.CreateWithdraw;
import com.study.springbootassignment.entity.AccountEntity;
import com.study.springbootassignment.entity.TransactionEntity;
import com.study.springbootassignment.entity.UserEntity;
import com.study.springbootassignment.exception.InsufficientFundsException;
import com.study.springbootassignment.repository.AccountRepository;
import com.study.springbootassignment.repository.TransactionRepository;
import com.study.springbootassignment.repository.UserRepository;
import com.study.springbootassignment.util.RandomStringHelper;
import com.study.springbootassignment.util.TransactionStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionProcessorService {
    private final TransactionLogService transactionLogService;
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final RandomStringHelper randomStringHelper;

    // --- Transfer ---
    @Transactional
    public TransactionEntity handleTransfer(CreateTransfer request) {
        AccountEntity from = accountRepository.findByAccountNumberForUpdate(request.getFromAccountNumber())
                .orElseThrow(() -> new RuntimeException("From account not found"));
        AccountEntity to = accountRepository.findByAccountNumberForUpdate(request.getToAccountNumber())
                .orElseThrow(() -> new RuntimeException("To account not found"));

        TransactionEntity tx = request.toEntity();
        BigDecimal amount = tx.getAmount();
        try {
            if (from.getBalance().compareTo(request.getAmount()) < 0) {
                throw new InsufficientFundsException("Insufficient funds");
            }
            from.setBalance(from.getBalance().subtract(amount));
            to.setBalance(to.getBalance().add(amount));
            accountRepository.save(from);
            accountRepository.save(to);
            tx.setFromAccount(from);
            tx.setToAccount(to);
            tx.setTransactionId(randomStringHelper.randomAccountNumber(11));
            tx.setProcessedBy(processByUser(request.getUserId()));
            tx.setTransactionStatus(TransactionStatus.COMMITTED);
            return transactionRepository.save(tx);
        } catch (Exception e) {
            tx.setFromAccount(from);
            tx.setToAccount(to);
            tx.setTransactionId(randomStringHelper.randomAccountNumber(11));
            tx.setProcessedBy(processByUser(request.getUserId()));
            tx.setTransactionStatus(TransactionStatus.FAILED);
            transactionLogService.saveFailedTransaction(tx);
            throw e; // rollback
        }
    }

    // --- Deposit ---
    @Transactional
    public TransactionEntity handleDeposit(CreateDeposit request) {
        AccountEntity account = accountRepository.findByAccountNumberForUpdate(request.getAccountNumber())
                .orElseThrow(() -> new RuntimeException("Account not found"));
        TransactionEntity tx = request.toEntity();
        try {
            account.setBalance(account.getBalance().add(tx.getAmount()));
            accountRepository.save(account);
            tx.setToAccount(account);
            tx.setTransactionId(randomStringHelper.randomAccountNumber(11));
            tx.setProcessedBy(processByUser(request.getUserId()));
            tx.setTransactionStatus(TransactionStatus.COMMITTED);
            return transactionRepository.save(tx);
        } catch (Exception e) {
            tx.setToAccount(account);
            tx.setTransactionId(randomStringHelper.randomAccountNumber(11));
            tx.setProcessedBy(processByUser(request.getUserId()));
            tx.setTransactionStatus(TransactionStatus.FAILED);
            transactionLogService.saveFailedTransaction(tx);
            throw e; // rollback
        }
    }

    // --- Withdraw ---
    @Transactional
    public TransactionEntity handleWithdraw(CreateWithdraw request) {

        AccountEntity account = accountRepository.findByAccountNumberForUpdate(request.getAccountNumber())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        TransactionEntity tx = request.toEntity();
        try {
            if (account.getBalance().compareTo(request.getAmount()) < 0) {
                throw new InsufficientFundsException("Insufficient funds");
            }
            BigDecimal amount = tx.getAmount();
            account.setBalance(account.getBalance().subtract(amount));
            accountRepository.save(account);
            tx.setFromAccount(account);
            tx.setTransactionId(randomStringHelper.randomAccountNumber(11));
            tx.setProcessedBy(processByUser(request.getUserId()));
            tx.setTransactionStatus(TransactionStatus.COMMITTED);
            return transactionRepository.save(tx);
        } catch (Exception e) {
            tx.setFromAccount(account);
            tx.setTransactionId(randomStringHelper.randomAccountNumber(11));
            tx.setProcessedBy(processByUser(request.getUserId()));
            tx.setTransactionStatus(TransactionStatus.FAILED);
            transactionLogService.saveFailedTransaction(tx);
            throw e;
        }
    }

    private UserEntity processByUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
    }
}
