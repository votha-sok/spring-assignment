package com.study.springbootassignment.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class ApplyUserRoleDto {
    @NotNull(message = "User Id is required.")
    Long userId;
    @NotNull(message = "Roles Id is required.")
    List<Long> roleIds;
}
