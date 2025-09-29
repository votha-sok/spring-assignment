package com.study.springbootassignment.dto.role;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplyPermissionDto {
    @NotNull(message = "Feature Id can not be null.")
    private Long featureId;
    private List<Long> permissionIds;
}
