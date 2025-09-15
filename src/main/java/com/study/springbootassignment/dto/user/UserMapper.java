package com.study.springbootassignment.dto.user;


import com.study.springbootassignment.dto.feature.FeaturePermissionResponse;
import com.study.springbootassignment.dto.permission.PermissionDto;
import com.study.springbootassignment.entity.UserEntity;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class UserMapper {

    public static UserDetailDto toDetailDto(UserEntity user) {
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
                            .map(p -> new PermissionDto(p.getId(), p.getFunctionName(),p.getFunctionOrder()))
                            .toList();
                    return new FeaturePermissionResponse(
                            feature.getId(),
                            feature.getTitle(),
                            perms
                    );
                })
                .collect(Collectors.toSet());
        return new UserDetailDto(user.getId(), user.getUserName(), user.getEmail(), user.getPhone(), user.getAdmin(), roleDtos, permissions);
    }
    public static UserDto toDto(UserEntity user) {
        return new UserDto(user.getId(), user.getUserName(), user.getEmail(), user.getPhone(), user.getAdmin());
    }
}
