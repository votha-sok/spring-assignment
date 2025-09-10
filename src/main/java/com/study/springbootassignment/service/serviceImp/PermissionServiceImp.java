package com.study.springbootassignment.service.serviceImp;


import com.study.springbootassignment.entity.PermissionEntity;
import com.study.springbootassignment.exception.ResourceNotFoundException;
import com.study.springbootassignment.repository.PermissionRepository;
import com.study.springbootassignment.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionServiceImp implements PermissionService {

    private final PermissionRepository permissionRepository;
    @Override
    public PermissionEntity save(PermissionEntity body) {
        return permissionRepository.save(body);
    }

    @Override
    public List<PermissionEntity> findAll() {
        return permissionRepository.findAll();
    }

    @Override
    public PermissionEntity findById(Long id) {
        return permissionRepository.findById(id).orElseThrow(() -> new RuntimeException("Permission Not Found"));
    }

    @Override
    public PermissionEntity update(Long id, PermissionEntity request) {
        PermissionEntity permission = permissionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Permission Not Found"));
        permission.setFunctionOrder(request.getFunctionOrder());
        permission.setFunctionName(request.getFunctionName());
        return permissionRepository.save(permission);
    }
}
