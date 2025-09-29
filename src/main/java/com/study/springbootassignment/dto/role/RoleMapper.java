package com.study.springbootassignment.dto.role;


import com.study.springbootassignment.dto.feature.FeaturePermissionResponse;
import com.study.springbootassignment.dto.permission.PermissionDto;
import com.study.springbootassignment.dto.user.UserRoleDto;
import com.study.springbootassignment.entity.RoleEntity;
import com.study.springbootassignment.entity.RoleFeaturePermissionEntity;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RoleMapper {
    public static RoleDto toDto(RoleEntity role) {
        return new RoleDto(role.getId(), role.getName(), role.getDescription());
    }

    public static RoleFeaturePermissionDto toDetailDto(RoleEntity role) {
        // Map features + permissions
        Set<FeaturePermissionResponse> permission = role.getRoleFeature().stream()
                .flatMap(roleFeature -> roleFeature.getRole().getFeatures().stream())
                .map(feature -> {
                    List<PermissionDto> perms = feature.getPermissions().stream()
                            .map(p -> new PermissionDto(p.getId(), p.getFunctionName(), p.getFunctionOrder()))
                            .toList();
                    return new FeaturePermissionResponse(
                            feature.getId(),
                            feature.getTitle(),
                            perms
                    );
                })
                .collect(Collectors.toSet());
        return RoleFeaturePermissionDto.builder()
                .id(role.getId())
                .name(role.getName())
                .description(role.getDescription())
                .features(permission)
                .build();
    }

    public static List<FeaturePermissionCheckDto> toRoleFeaturePermissionDto(List<RoleFeaturePermissionEntity> list) {
        return list.stream()
                .collect(Collectors.groupingBy(
                        p -> p.getFeature().getId(),
                        Collectors.mapping(p -> p.getPermission().getId(), Collectors.toList())
                ))
                .entrySet()
                .stream()
                .map(entry -> FeaturePermissionCheckDto.builder()
                        .featureId(entry.getKey())
                        .permissionIds(entry.getValue())
                        .build())
                .toList();
    }
}
