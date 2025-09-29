package com.study.springbootassignment.repository;

import com.study.springbootassignment.entity.FeatureEntity;
import com.study.springbootassignment.entity.RoleEntity;
import com.study.springbootassignment.entity.RoleFeaturePermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleFeaturePermissionRepository extends JpaRepository<RoleFeaturePermissionEntity, Long> {
//    @Modifying
//    @Query("DELETE FROM RoleFeaturePermissionEntity rfp WHERE rfp.role = :role AND rfp.feature = :feature")
//    void deleteByRoleAndFeature(@Param("role") RoleEntity role, @Param("feature") FeatureEntity feature);
    void deleteByRoleAndFeature(RoleEntity role, FeatureEntity feature);

    List<RoleFeaturePermissionEntity> findByRole(RoleEntity role);
}