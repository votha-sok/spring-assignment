package com.study.springbootassignment.dto.feature;


import com.study.springbootassignment.dto.permission.PermissionDto;
import com.study.springbootassignment.entity.FeatureEntity;
import com.study.springbootassignment.entity.FeaturePermissionEntity;

import java.util.List;

public class FeatureMapper {
    public static FeatureResponse toDto(FeatureEntity feature) {
        return new FeatureResponse(feature.getId(), feature.getTitle(), feature.getIcon(), feature.getMenuOrder(), feature.getRouterLink(), null);
    }

    public static FeaturePermissionResponse toDetailDto(FeatureEntity feature) {
        List<PermissionDto> perms = feature.getPermissions().stream()
                .map(p -> new PermissionDto(p.getId(), p.getFunctionName(), p.getFunctionOrder()))
                .toList();
        return FeaturePermissionResponse.builder()
                .id(feature.getId())
                .featureName(feature.getTitle())
                .permissions(perms)
                .build();
    }
}
