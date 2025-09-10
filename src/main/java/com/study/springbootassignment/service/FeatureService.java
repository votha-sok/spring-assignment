package com.study.springbootassignment.service;


import com.study.springbootassignment.dto.feature.CreateFeaturePermission;
import com.study.springbootassignment.dto.feature.FeaturePermissionResponse;
import com.study.springbootassignment.dto.feature.FeatureResponse;
import com.study.springbootassignment.entity.FeatureEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FeatureService {
    FeatureEntity save(FeatureEntity feature);
    List<FeatureEntity> findAll();
    FeatureEntity findById(Long id);
    FeatureEntity update(Long id, FeatureEntity request);
    FeatureEntity applyPermissions(CreateFeaturePermission request);
    List<FeaturePermissionResponse> getPermissions(Long id);
    List<FeatureResponse> getMenuByUser(Long userId);
}
