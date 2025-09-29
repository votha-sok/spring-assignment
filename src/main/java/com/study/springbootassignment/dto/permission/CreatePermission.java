package com.study.springbootassignment.dto.permission;


import com.study.springbootassignment.entity.PermissionEntity;

public class CreatePermission extends PermissionDto {
    public PermissionEntity toEntity() {
        PermissionEntity permissionEntity1 = new PermissionEntity();
        permissionEntity1.setId(getId());
        permissionEntity1.setFunctionName(getFunctionName());
        permissionEntity1.setFunctionOrder(getFunctionOrder());
        return permissionEntity1;
    }
}
