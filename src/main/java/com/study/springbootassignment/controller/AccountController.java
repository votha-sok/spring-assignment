package com.study.springbootassignment.controller;

import com.study.springbootassignment.dto.account.AccountMapper;
import com.study.springbootassignment.dto.account.AccountResponse;
import com.study.springbootassignment.dto.account.CreateAccount;
import com.study.springbootassignment.entity.AccountEntity;
import com.study.springbootassignment.jwt.UserContext;
import com.study.springbootassignment.service.AccountService;
import com.study.springbootassignment.service.UserService;
import com.study.springbootassignment.util.RandomStringHelper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/accounts")

public class AccountController {
    private final AccountService accountService;
    private final RandomStringHelper accountHelper;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody CreateAccount body) {
        AccountEntity account = body.toEntity();
        account.setAccountNumber(accountHelper.randomAccountNumber(10));
        account.setCreatedBy(userService.findById(UserContext.getUserId()));
        AccountEntity savedAccount = accountService.save(account);
        AccountResponse response = AccountMapper.toDto(savedAccount);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountResponse> updateAccount(@PathVariable Long id, @Valid @RequestBody CreateAccount body) {
        AccountEntity account = body.toEntity();
        AccountEntity updateAccount = accountService.update(id, account);
        AccountResponse response = AccountMapper.toDto(updateAccount);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> getAllAccounts() {
        List<AccountResponse> list = accountService.findAll().stream().map(AccountMapper::toDto).toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccountById(@PathVariable Long id) {
        return ResponseEntity.ok(AccountMapper.toDto(accountService.findById(id)));
    }

    @GetMapping("/list")
    public ResponseEntity<Page<AccountResponse>> list(
            @RequestParam Map<String, String> params,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "order", defaultValue = "DESC") String order,
            @RequestParam(value = "sort", defaultValue = "id") String sor) {
        Page<AccountResponse> list = accountService.list(params, page, size).map(AccountMapper::toDto);
        return ResponseEntity.ok(list);
    }

}
