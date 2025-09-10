package com.study.springbootassignment.service;



import com.study.springbootassignment.dto.role.CreateRoleFeature;
import com.study.springbootassignment.entity.RoleEntity;

import java.util.List;

public interface RoleService {
    RoleEntity findById(Long id);
    List<RoleEntity> findAll();
    RoleEntity save(RoleEntity role);
    RoleEntity update(Long id ,RoleEntity role);
    RoleEntity applyRoleFeature(CreateRoleFeature request);
}
