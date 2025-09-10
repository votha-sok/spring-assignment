package com.study.springbootassignment.dto.role;

import com.study.springbootassignment.entity.RoleEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
public class CreateRoleDto extends RoleDto {
    public RoleEntity toEntity() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(getId());
        roleEntity.setName(getName());
        roleEntity.setDescription(getDescription());
        return roleEntity;
    }
}
