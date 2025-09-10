package com.study.springbootassignment.repository;

import com.study.springbootassignment.entity.FeatureEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeatureRepository extends JpaRepository<FeatureEntity, Long> {

    List<FeatureEntity> findAllByParentId(Long parentId);
}
