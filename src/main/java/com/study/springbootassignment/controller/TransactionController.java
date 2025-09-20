package com.study.springbootassignment.controller;


import com.study.springbootassignment.dto.transaction.*;
import com.study.springbootassignment.dto.user.UserDto;
import com.study.springbootassignment.dto.user.UserMapper;
import com.study.springbootassignment.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    //    @PreAuthorize("hasAnyAuthority('CREATE_TRANSFER')")
    @PostMapping("transfer")
    public ResponseEntity<CreateTransfer> transfer(@Valid @RequestBody CreateTransfer transfer) {
        transactionService.processTransfer(transfer);
        return ResponseEntity.ok(transfer);
    }

    //    @PreAuthorize("hasAnyAuthority('CREATE_DEPOSIT')")
    @PostMapping("deposit")
    public ResponseEntity<TransactionResponse> deposit(@Valid @RequestBody CreateDeposit deposit) {
        TransactionResponse response = TransactionMapper.toDto(transactionService.processDeposit(deposit));
        return ResponseEntity.ok(response);
    }

    //    @PreAuthorize("hasAnyAuthority('CREATE_WITHDRAW')")
    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponse> withdraw(@Valid @RequestBody CreateWithdraw withdraw) {
        TransactionResponse response = TransactionMapper.toDto(transactionService.processWithdraw(withdraw));
        return ResponseEntity.ok(response);
    }

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
