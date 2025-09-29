package com.study.springbootassignment.service.serviceImp;

import com.study.springbootassignment.entity.RoleEntity;
import com.study.springbootassignment.entity.RoleFeaturePermissionEntity;
import com.study.springbootassignment.repository.RoleFeaturePermissionRepository;
import com.study.springbootassignment.service.RoleFeaturePermissionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RoleFeaturePermissionServiceImp implements RoleFeaturePermissionService {
    private RoleFeaturePermissionRepository roleFeaturePermissionRepository;
    @Override
    public List<RoleFeaturePermissionEntity> findAllByRole(RoleEntity role) {
        return roleFeaturePermissionRepository.findByRole(role);
    }
}
