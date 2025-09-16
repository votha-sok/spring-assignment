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
import com.study.springbootassignment.util.TransactionStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransactionProcessorService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    @Transactional
    public void handleTransfer(Long id, CreateTransfer request) {
        TransactionEntity tx = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found: " + id));

        try {
            AccountEntity fromAccount = accountRepository.findByAccountNumber(request.getFromAccountNumber())
                    .orElseThrow(() -> new AccountNotFoundException("From account not found: " + request.getFromAccountNumber()));
            AccountEntity toAccount = accountRepository.findByAccountNumber(request.getToAccountNumber())
                    .orElseThrow(() -> new AccountNotFoundException("To account not found: " + request.getToAccountNumber()));

            BigDecimal amount = tx.getAmount();

            if (fromAccount.getBalance().compareTo(amount) < 0) {
                throw new InsufficientFundsException(fromAccount.getAccountNumber(), amount, fromAccount.getBalance());
            }

            fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
            toAccount.setBalance(toAccount.getBalance().add(amount));

            accountRepository.save(fromAccount);
            accountRepository.save(toAccount);
            tx.setFromAccount(fromAccount);
            tx.setToAccount(toAccount);
            updateTransactionStatus(tx, TransactionStatus.COMMITTED);

        } catch (Exception e) {
            updateTransactionStatus(tx, TransactionStatus.FAILED);
            throw new RuntimeException("Transfer failed: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void handleDeposit(Long id, CreateDeposit request) {
        TransactionEntity tx = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found: " + id));

        try {
            AccountEntity account = accountRepository.findByAccountNumber(request.getAccountNumber())
                    .orElseThrow(() -> new AccountNotFoundException("Account not found: " + request.getAccountNumber()));

            BigDecimal amount = tx.getAmount();

            account.setBalance(account.getBalance().add(amount));
            accountRepository.save(account);
            tx.setToAccount(account);
            updateTransactionStatus(tx, TransactionStatus.COMMITTED);

        } catch (Exception e) {
            updateTransactionStatus(tx, TransactionStatus.FAILED);
            throw new RuntimeException("Deposit failed: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void handleWithdraw(Long id, CreateWithdraw request) {
        TransactionEntity tx = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found: " + id));

        try {
            AccountEntity account = accountRepository.findByAccountNumber(request.getAccountNumber())
                    .orElseThrow(() -> new AccountNotFoundException("Account not found: " + request.getAccountNumber()));

            BigDecimal amount = tx.getAmount();

            if (account.getBalance().compareTo(amount) < 0) {
                throw new InsufficientFundsException(account.getAccountNumber(), amount, account.getBalance());
            }

            account.setBalance(account.getBalance().subtract(amount));
            accountRepository.save(account);
            tx.setFromAccount(account);
            updateTransactionStatus(tx, TransactionStatus.COMMITTED);

        } catch (Exception e) {
            updateTransactionStatus(tx, TransactionStatus.FAILED);
            throw new RuntimeException("Withdrawal failed: " + e.getMessage(), e);
        }
    }

    private void updateTransactionStatus(TransactionEntity tx, TransactionStatus status) {
        UserEntity processor = userRepository.findById(UserContext.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found: " + UserContext.getUserId()));
        tx.setProcessedBy(processor);
        tx.setTransactionStatus(status);
        tx.setTimestamp(LocalDateTime.now());
        transactionRepository.save(tx);
    }
}