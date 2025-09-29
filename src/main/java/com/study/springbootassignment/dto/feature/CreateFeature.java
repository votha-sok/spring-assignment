package com.study.springbootassignment.dto.feature;

import com.study.springbootassignment.entity.FeatureEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CreateFeature extends FeatureDto {
    private Long parentId;
    public FeatureEntity toEntity() {
        FeatureEntity featureEntity = new FeatureEntity();
        featureEntity.setRouterLink(getRouterLink());
        featureEntity.setMenuOrder(getMenuOrder());
        featureEntity.setTitle(getTitle());
        featureEntity.setIcon(getIcon());
        featureEntity.setParentId(parentId);
        return featureEntity;
    }
}
