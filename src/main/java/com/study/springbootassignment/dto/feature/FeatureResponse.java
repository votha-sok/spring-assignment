package com.study.springbootassignment.dto.feature;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeatureResponse extends FeatureDto {
    private List<FeatureResponse> items; // for nested items

    public FeatureResponse(Long id, String title, String icon, Integer menuOrder, List<String> routerLink, List<FeatureResponse> items) {
        super(id, title, icon, menuOrder, routerLink);
        this.items = items;
    }
}
