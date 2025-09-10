package com.study.springbootassignment.repository;

import com.study.springbootassignment.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoleRepository extends JpaRepository<RoleEntity,Long> {
    @Query("SELECT r FROM RoleEntity r LEFT JOIN FETCH r.roleFeature WHERE r.id = :id")
    RoleEntity findByIdWithFeatures(@Param("id") Long id);

}
