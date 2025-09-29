package com.study.springbootassignment.dto.feature;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuPermissionResponse {
    private Long id;
    private String featureName;
    private Integer order;
    private List<MenuPermissionResponse> children;
}
