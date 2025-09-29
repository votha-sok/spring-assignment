package com.study.springbootassignment.dto.role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateRoleFeature {
    private Long roleId;
    private List<Long> featureIds;
}
