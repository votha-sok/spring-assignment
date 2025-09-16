package com.study.springbootassignment.controller;


import com.study.springbootassignment.dto.transaction.*;
import com.study.springbootassignment.entity.TransactionEntity;
import com.study.springbootassignment.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PreAuthorize("hasAnyAuthority('TRANSACTION_TRANSFER')")
    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponse> transfer(@Valid @RequestBody CreateTransfer transfer) {
        TransactionEntity tx = transfer.toEntity();
        TransactionResponse response = TransactionMapper.toDto(transactionService.processTransfer(tx));
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyAuthority('TRANSACTION_DEPOSIT')")
    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponse> deposit(@Valid @RequestBody CreateDeposit deposit) {
        TransactionEntity tx = deposit.toEntity();
        TransactionResponse response = TransactionMapper.toDto(transactionService.processDeposit(tx));
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyAuthority('TRANSACTION_WITHDRAW')")
    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponse> withdraw(@Valid @RequestBody CreateWithdraw withdraw) {
        TransactionEntity tx = withdraw.toEntity();
        TransactionResponse response = TransactionMapper.toDto(transactionService.processWithdraw(tx));
        return ResponseEntity.ok(response);
    }
}
