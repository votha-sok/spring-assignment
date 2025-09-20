package com.study.springbootassignment.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

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

    // 👇 prevent duplicates in Set
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RoleFeatureEntity)) return false;
        RoleFeatureEntity that = (RoleFeatureEntity) o;
        return Objects.equals(role.getId(), that.role.getId()) &&
                Objects.equals(feature.getId(), that.feature.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(role.getId(), feature.getId());
    }
}
