package com.study.springbootassignment.service.serviceImp;

import com.study.springbootassignment.configuration.SpecificationBuilder;
import com.study.springbootassignment.entity.AccountEntity;
import com.study.springbootassignment.repository.AccountRepository;
import com.study.springbootassignment.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class AccountServiceImp implements AccountService {
    private AccountRepository accountRepository;
    @Override
    public AccountEntity save(AccountEntity account) {
        return accountRepository.save(account);
    }

    @Override
    public AccountEntity update(Long id, AccountEntity account) {
        AccountEntity accountEntity = findById(id);
        accountEntity.setAccountType(account.getAccountType());
        accountEntity.setAccountHolderName(account.getAccountHolderName());
        accountEntity.setAccountHolderPhone(account.getAccountHolderPhone());
        accountEntity.setAccountHolderEmail(account.getAccountHolderEmail());
        accountEntity.setNationalId(account.getNationalId());
        return accountRepository.save(accountEntity);
    }

    @Override
    public List<AccountEntity> findAll() {
        return accountRepository.findAll();
    }

    @Override
    public AccountEntity findById(Long id) {
        return accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account not found"));
    }

    @Override
    public Page<AccountEntity> list(Map<String, String> params, int page, int size) {
        Specification<AccountEntity> spec = SpecificationBuilder.buildFromParams(params, AccountEntity.class);
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString("DESC"), "id"));
        return accountRepository.findAll(spec, pageable);
    }
}
