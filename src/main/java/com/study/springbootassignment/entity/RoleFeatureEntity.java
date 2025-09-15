package com.study.springbootassignment.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "role_feature")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleFeatureEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many-to-one relation with User
    @ManyToOne
    @JoinColumn(name = "feature_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private FeatureEntity feature;

    // Many-to-one relation with Role
    @ManyToOne
    @JoinColumn(name = "role_id")
    private RoleEntity role;
    @Column(name = "status")
    private Boolean status = true;
}
