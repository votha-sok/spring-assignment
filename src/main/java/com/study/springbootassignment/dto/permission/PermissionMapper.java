package com.study.springbootassignment.dto.permission;

import com.study.springbootassignment.entity.PermissionEntity;

public class PermissionMapper {

    public static PermissionDto toDto(PermissionEntity permission) {
        return new PermissionDto(permission.getId(), permission.getFunctionName(), permission.getFunctionOrder());
    }
}
