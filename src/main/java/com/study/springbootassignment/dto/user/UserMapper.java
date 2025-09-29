package com.study.springbootassignment.dto.user;


import com.study.springbootassignment.dto.feature.FeaturePermissionResponse;
import com.study.springbootassignment.dto.permission.PermissionDto;
import com.study.springbootassignment.entity.FeatureEntity;
import com.study.springbootassignment.entity.RoleEntity;
import com.study.springbootassignment.entity.UserEntity;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class UserMapper {

    public static UserInfoDto toInfoDto(UserEntity user) {
        Set<UserRoleDto> roleDtos = user.getUserRoles().stream()
                .map(ur -> new UserRoleDto(
                        ur.getRole().getId(),
                        ur.getRole().getName()
                ))
                .collect(Collectors.toSet());
        // Map features + permissions
        Set<FeaturePermissionResponse> permissions = user.getUserRoles().stream()
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
        return UserInfoDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .admin(user.getAdmin())
                .phone(user.getPhone())
                .userName(user.getUserName())
                .roles(roleDtos)
                .featurePermissions(permissions)
                .build();
    }
    public static UserInfoDto toAdminInfoDto(List<RoleEntity> roles, List<FeatureEntity> features , UserEntity user) {
        Set<UserRoleDto> roleDtos = roles.stream()
                .map(ur -> new UserRoleDto(
                        ur.getId(),
                        ur.getName()
                ))
                .collect(Collectors.toSet());
        // Map features + permissions
        Set<FeaturePermissionResponse> permissions = features.stream()
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
        return UserInfoDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .admin(user.getAdmin())
                .phone(user.getPhone())
                .userName(user.getUserName())
                .roles(roleDtos)
                .featurePermissions(permissions)
                .build();
    }

    public static UserDetailDto toDetailDto(UserEntity user) {
        Set<UserRoleDto> roleDto = user.getUserRoles().stream()
                .map(ur -> new UserRoleDto(
                        ur.getRole().getId(),
                        ur.getRole().getName()
                ))
                .collect(Collectors.toSet());
        return UserDetailDto.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .roles(roleDto)
                .admin(user.getAdmin())
                .build();
    }

    public static UserDto toDto(UserEntity user) {
        return UserDto.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .admin(user.getAdmin())
                .build();
    }
}
