package com.study.springbootassignment.service;



import com.study.springbootassignment.dto.role.ApplyPermissionDto;
import com.study.springbootassignment.dto.role.CreateRoleFeature;
import com.study.springbootassignment.entity.FeatureEntity;
import com.study.springbootassignment.entity.RoleEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface RoleService {
    RoleEntity findById(Long id);
    List<RoleEntity> findAll();
    RoleEntity save(RoleEntity role);
    RoleEntity update(Long id ,RoleEntity role);
    RoleEntity applyRoleFeature(CreateRoleFeature request);
    void applyFeaturePermission(List<ApplyPermissionDto> requests, Long roleId);
    Page<RoleEntity> list(Map<String, String> params, int page, int size);

}
