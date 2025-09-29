package com.study.springbootassignment.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "role_feature_permission")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleFeaturePermissionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many-to-One with Role
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private RoleEntity role;

    // Many-to-One with Feature
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feature_id", nullable = false)
    private FeatureEntity feature;

    // Many-to-One with Permission
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permission_id", nullable = false)
    private PermissionEntity permission;

}
