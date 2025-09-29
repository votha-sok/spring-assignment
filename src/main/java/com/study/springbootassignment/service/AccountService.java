package com.study.springbootassignment.service;

import com.study.springbootassignment.entity.AccountEntity;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface AccountService {
    AccountEntity save(AccountEntity account);
    AccountEntity update(Long id, AccountEntity account);
    List<AccountEntity> findAll();
    AccountEntity findById(Long id);
    Page<AccountEntity> list(Map<String, String> params, int page, int size);
}
