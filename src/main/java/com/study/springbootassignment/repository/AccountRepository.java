package com.study.springbootassignment.repository;

import com.study.springbootassignment.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AccountRepository extends JpaRepository<AccountEntity, Long> , JpaSpecificationExecutor<AccountEntity> {
}
