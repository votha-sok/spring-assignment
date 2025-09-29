package com.study.springbootassignment.dto.role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeaturePermissionCheckDto {
    private Long featureId;
    private List<Long> permissionIds;
}
