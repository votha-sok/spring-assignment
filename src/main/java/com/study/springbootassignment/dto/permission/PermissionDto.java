package com.study.springbootassignment.dto.permission;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissionDto {
    private Long id;
    @NotBlank(message = "Feature name can not be null or empty.")
    private String functionName;
    @NotNull(message = "Feature order can not be null.")
    private Integer functionOrder;
}
