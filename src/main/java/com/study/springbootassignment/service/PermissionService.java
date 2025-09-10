package com.study.springbootassignment.service;


import com.study.springbootassignment.entity.PermissionEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PermissionService {
    PermissionEntity save(PermissionEntity feature);
    List<PermissionEntity> findAll();
    PermissionEntity findById(Long id);
    PermissionEntity update(Long id, PermissionEntity request);
}
