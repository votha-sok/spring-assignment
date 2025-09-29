package com.study.springbootassignment.dto.feature;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateFeaturePermission {
    private Long featureId;
    private List<Long> permissionIds;
}
