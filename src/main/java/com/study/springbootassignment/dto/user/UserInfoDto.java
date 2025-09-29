package com.study.springbootassignment.dto.user;

import com.study.springbootassignment.dto.feature.FeaturePermissionResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class UserInfoDto extends UserDto {
    Set<UserRoleDto> roles;
    Set<FeaturePermissionResponse> featurePermissions;
}