package com.study.springbootassignment.dto.feature;


import com.study.springbootassignment.entity.FeatureEntity;

public class FeatureMapper {
    public static FeatureResponse toDto(FeatureEntity feature) {
        return new FeatureResponse(feature.getId(), feature.getTitle(), feature.getIcon(), feature.getMenuOrder(), feature.getRouterLink(), null);
    }
}
