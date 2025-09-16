package com.study.springbootassignment.repository;

import com.study.springbootassignment.entity.AccountEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<AccountEntity, Long> , JpaSpecificationExecutor<AccountEntity> {
    // Pessimistic lock to prevent concurrent updates to same account
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select a from AccountEntity a where a.accountNumber = :acct")
    Optional<AccountEntity> findByAccountNumberForUpdate(@Param("acct") String accountNumber);

    Optional<AccountEntity> findByAccountNumber(String accountNumber);
}
