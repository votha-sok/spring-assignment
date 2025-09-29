package com.study.springbootassignment.service;

import com.study.springbootassignment.entity.RoleEntity;
import com.study.springbootassignment.entity.RoleFeaturePermissionEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RoleFeaturePermissionService {
    List<RoleFeaturePermissionEntity> findAllByRole(RoleEntity role);
}
