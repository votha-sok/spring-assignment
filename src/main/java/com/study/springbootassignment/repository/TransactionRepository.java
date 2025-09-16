package com.study.springbootassignment.repository;

import com.study.springbootassignment.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long>, JpaSpecificationExecutor<TransactionEntity> {
}
