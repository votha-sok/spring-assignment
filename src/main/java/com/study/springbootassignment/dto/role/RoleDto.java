package com.study.springbootassignment.dto.role;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class RoleDto {
    private Long id;
    @NotBlank(message = "Role name not allow null or empty .")
    private String name;
    private String description;
}
