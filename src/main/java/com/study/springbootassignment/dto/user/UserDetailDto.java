package com.study.springbootassignment.dto.user;

import com.study.springbootassignment.dto.feature.FeaturePermissionResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserDetailDto extends UserDto {
    Set<UserRoleDto> roles;
    Set<FeaturePermissionResponse> featurePermissions;

    public UserDetailDto(Long id, String userName, String email, String phone, Boolean isSuperAdmin,  Set<UserRoleDto> roleDtos, Set<FeaturePermissionResponse> permissions) {
        super(id, userName, email, phone, isSuperAdmin);
        this.roles = roleDtos;
        this.featurePermissions = permissions;
    }
}
