package com.study.springbootassignment.dto.feature;

import com.study.springbootassignment.dto.permission.PermissionDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeaturePermissionResponse {
    private Long id;
    private String featureName;
    private List<PermissionDto> permissions;
}
