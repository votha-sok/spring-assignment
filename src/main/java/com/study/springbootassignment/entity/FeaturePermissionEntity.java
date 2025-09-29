package com.study.springbootassignment.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "feature_permissions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeaturePermissionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "feature_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private FeatureEntity feature;

    @ManyToOne
    @JoinColumn(name = "permission_id")
    private PermissionEntity permission;

    @Column(name = "status")
    private Boolean status = true;
}
