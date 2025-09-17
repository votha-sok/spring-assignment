package com.study.springbootassignment.dto.role;


import com.study.springbootassignment.dto.feature.FeaturePermissionResponse;
import com.study.springbootassignment.dto.permission.PermissionDto;
import com.study.springbootassignment.dto.user.UserRoleDto;
import com.study.springbootassignment.entity.RoleEntity;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RoleMapper {
    public static RoleDto toDto(RoleEntity role) {
        return new RoleDto(role.getId(), role.getName(), role.getDescription());
    }

    public static RoleFeaturePermissionDto toDetailDto(RoleEntity role) {
        // Map features + permissions
        Set<FeaturePermissionResponse> permission = role.getUserRoles().stream()
                .flatMap(userRole -> userRole.getRole().getFeatures().stream())
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
}
