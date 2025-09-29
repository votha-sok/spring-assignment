package com.study.springbootassignment.dto.feature;


import com.study.springbootassignment.dto.permission.PermissionDto;
import com.study.springbootassignment.entity.FeatureEntity;

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

    public static MenuPermissionResponse toMenuPermissionDto(FeatureEntity feature, List<FeatureEntity> allFeatures) {

        List<MenuPermissionResponse> childList = allFeatures.stream()
                .filter(f -> feature.getId().equals(f.getParentId()))
                .map(f -> toMenuPermissionDto(f, allFeatures))
                .toList();
        return MenuPermissionResponse.builder()
                .id(feature.getId())
                .featureName(feature.getTitle())
                .order(feature.getMenuOrder())
                .children(childList.isEmpty() ? null : childList)
                .build();
    }

    public static FeaturePermissionResponse toFeaturePermissionDto(FeatureEntity feature) {
        return toDetailDto(feature);
    }

}
