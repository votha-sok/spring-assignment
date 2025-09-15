package com.study.springbootassignment.entity;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "features")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class FeatureEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;
    @Column(name = "icon")
    private String icon;
    @Column(name = "router_link")
    private List<String> routerLink = new ArrayList<>();

    @Column(name = "status")
    private Boolean status = true;

    @Column(name = "menu_order")
    private Integer menuOrder;

    // Parent reference
    @Column(name = "parent_id")
    private Long parentId;


    @OneToMany(mappedBy = "feature", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<FeaturePermissionEntity> featurePermission = new HashSet<>();


    @OneToMany(mappedBy = "feature", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<FeaturePermissionEntity> roleFeature = new HashSet<>();

    public Set<PermissionEntity> getPermissions() {
        return featurePermission.stream()
                .map(FeaturePermissionEntity::getPermission)
                .collect(Collectors.toSet());
    }

    // 🔹 Auditing fields
    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
}
