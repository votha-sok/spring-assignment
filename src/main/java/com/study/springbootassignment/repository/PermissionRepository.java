package com.study.springbootassignment.repository;

import com.study.springbootassignment.entity.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<PermissionEntity,Long> {
}
