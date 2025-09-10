package com.study.springbootassignment.dto.role;


import com.study.springbootassignment.entity.RoleEntity;

public class RoleMapper {
    public static RoleDto toDto(RoleEntity role){
        return new RoleDto(role.getId(), role.getName(), role.getDescription());
    }
}
