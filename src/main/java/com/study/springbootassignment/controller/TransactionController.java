package com.study.springbootassignment.controller;


import com.study.springbootassignment.dto.transaction.*;
import com.study.springbootassignment.entity.TransactionEntity;
import com.study.springbootassignment.exception.InsufficientFundsException;
import com.study.springbootassignment.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.Map;
import java.util.concurrent.ExecutionException;

@Slf4j
@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PreAuthorize("hasAnyAuthority('CREATE_TRANSFER')")
    @PostMapping("transfer")
    public ResponseEntity<TransactionResponse> transfer(@Valid @RequestBody CreateTransfer transfer) {
        try {
            TransactionEntity result = transactionService.processTransfer(transfer).get(); // wait
            return ResponseEntity.ok(TransactionMapper.toDto(result));
        } catch (ExecutionException | InterruptedException e) {
            throw new InsufficientFundsException(e.getCause().getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('CREATE_DEPOSIT')")
    @PostMapping("deposit")
    public ResponseEntity<TransactionResponse> deposit(@Valid @RequestBody CreateDeposit deposit) {
        try {
            TransactionEntity result = transactionService.processDeposit(deposit).get();
            return ResponseEntity.ok(TransactionMapper.toDto(result));
        } catch (Exception e) {
            throw new RuntimeException(e.getCause().getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('CREATE_WITHDRAW')")
    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponse> withdraw(@Valid @RequestBody CreateWithdraw request) {
        try {
            TransactionEntity result = transactionService.processWithdraw(request).get(); // wait
            return ResponseEntity.ok(TransactionMapper.toDto(result));
        } catch (ExecutionException | InterruptedException e) {
            throw new InsufficientFundsException(e.getCause().getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('VIEW_HISTORY')")
    @GetMapping("list")
    public ResponseEntity<Page<TransactionResponse>> list(
            @RequestParam Map<String, String> params,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "order", defaultValue = "DESC") String order,
            @RequestParam(value = "sort", defaultValue = "id") String sor
    ) {
        Page<TransactionResponse> trans = transactionService.list(params, page, size).map(TransactionMapper::toDto);
        return ResponseEntity.ok(trans);
    }
}
