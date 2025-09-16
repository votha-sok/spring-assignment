package com.study.springbootassignment.service.serviceImp;

import com.study.springbootassignment.dto.transaction.CreateDeposit;
import com.study.springbootassignment.dto.transaction.CreateTransfer;
import com.study.springbootassignment.dto.transaction.CreateWithdraw;
import com.study.springbootassignment.entity.AccountEntity;
import com.study.springbootassignment.entity.TransactionEntity;
import com.study.springbootassignment.entity.UserEntity;
import com.study.springbootassignment.exception.InsufficientFundsException;
import com.study.springbootassignment.jwt.UserContext;
import com.study.springbootassignment.repository.AccountRepository;
import com.study.springbootassignment.repository.TransactionRepository;
import com.study.springbootassignment.repository.UserRepository;
import com.study.springbootassignment.service.TransactionService;
import com.study.springbootassignment.util.TransactionStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransactionServiceImp implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final TransferQueueManager transferQueueManager;
    private final TransactionServiceImp self; // Self-reference for transactional methods

    @Override
    public TransactionEntity processTransfer(TransactionEntity request) {
        // Create transaction in PENDING state
        request.setProcessedBy(processBy());
        TransactionEntity savedTx = transactionRepository.save(request);

        // Enqueue task for from-account using the transaction ID
        final Long transactionId = savedTx.getId();

        transferQueueManager.submit(request.getFromAccountNumber(), () -> {
            // Use self-invocation to ensure transactional behavior
            self.handleTransfer(transactionId);
        });

        return savedTx;
    }

    @Override
    public TransactionEntity processDeposit(TransactionEntity request) {

        request.setProcessedBy(processBy());
        TransactionEntity savedTx = transactionRepository.save(request);

        final Long transactionId = savedTx.getId();

        transferQueueManager.submit(request.getToAccountNumber(), () -> {
            self.handleDeposit(transactionId);
        });

        return savedTx;
    }

    @Override
    public TransactionEntity processWithdraw(TransactionEntity request) {
        request.setProcessedBy(processBy());
        TransactionEntity savedTx = transactionRepository.save(request);
        final Long transactionId = savedTx.getId();

        transferQueueManager.submit(request.getFromAccountNumber(), () -> {
            self.handleWithdraw(transactionId);
        });

        return savedTx;
    }

    @Transactional
    public void handleTransfer(Long id) {
        TransactionEntity tx = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found: " + id));

        try {
            AccountEntity fromAccount = accountRepository.findByAccountNumber(tx.getFromAccountNumber())
                    .orElseThrow(() -> new AccountNotFoundException("From account not found: " + tx.getFromAccountNumber()));
            AccountEntity toAccount = accountRepository.findByAccountNumber(tx.getToAccountNumber())
                    .orElseThrow(() -> new AccountNotFoundException("To account not found: " + tx.getToAccountNumber()));

            BigDecimal amount = tx.getAmount();

            // Validate sufficient funds
            if (fromAccount.getBalance().compareTo(amount) < 0) {
                throw new InsufficientFundsException(fromAccount.getAccountNumber(), amount, fromAccount.getBalance());
            }

            // Perform transfer
            fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
            toAccount.setBalance(toAccount.getBalance().add(amount));

            accountRepository.save(fromAccount);
            accountRepository.save(toAccount);

            // Update transaction status
            updateTransactionStatus(tx, TransactionStatus.COMMITTED);

        } catch (Exception e) {
            updateTransactionStatus(tx, TransactionStatus.FAILED);
            throw new RuntimeException("Transfer failed: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void handleDeposit(Long id) {
        TransactionEntity tx = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found: " + id));

        try {
            AccountEntity account = accountRepository.findByAccountNumber(tx.getToAccountNumber())
                    .orElseThrow(() -> new AccountNotFoundException("Account not found: " + tx.getToAccountNumber()));

            BigDecimal amount = tx.getAmount();

            // Perform deposit
            account.setBalance(account.getBalance().add(amount));
            accountRepository.save(account);

            // Update transaction status
            updateTransactionStatus(tx, TransactionStatus.COMMITTED);

        } catch (Exception e) {
            updateTransactionStatus(tx, TransactionStatus.FAILED);
            throw new RuntimeException("Deposit failed: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void handleWithdraw(Long id) {
        TransactionEntity tx = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found: " + id));

        try {
            AccountEntity account = accountRepository.findByAccountNumber(tx.getFromAccountNumber())
                    .orElseThrow(() -> new AccountNotFoundException("Account not found: " + tx.getFromAccountNumber()));

            BigDecimal amount = tx.getAmount();

            // Validate sufficient funds
            if (account.getBalance().compareTo(amount) < 0) {
                throw new InsufficientFundsException(account.getAccountNumber(), amount, account.getBalance());
            }

            // Perform withdrawal
            account.setBalance(account.getBalance().subtract(amount));
            accountRepository.save(account);

            // Update transaction status
            updateTransactionStatus(tx, TransactionStatus.COMMITTED);

        } catch (Exception e) {
            updateTransactionStatus(tx, TransactionStatus.FAILED);
            throw new RuntimeException("Withdrawal failed: " + e.getMessage(), e);
        }
    }

    // Helper method to update transaction status
    private void updateTransactionStatus(TransactionEntity tx, TransactionStatus status) {
        tx.setTransactionStatus(status);
        tx.setTimestamp(LocalDateTime.now());
        transactionRepository.save(tx);
    }

    private UserEntity processBy() {
        return userRepository.findById(UserContext.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found: " + UserContext.getUserId()));
    }
}