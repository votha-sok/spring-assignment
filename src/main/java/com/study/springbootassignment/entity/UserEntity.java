package com.study.springbootassignment.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name", nullable = false)
    private String userName;
    @Column(name = "email", nullable = false, unique = true )
    private String email;
    @Column(name = "phone", unique = true)
    private String phone;
    @Column(name = "last_name")
    private String firstName;
    @Column(name = "first_name")
    private String lastName;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "admin")
    private Boolean admin = false;
    @Column(name = "status")
    private Boolean status = true;

    public Set<RoleEntity> getRoles() {
        return userRoles.stream()
                .map(UserRoleEntity::getRole)
                .collect(Collectors.toSet());
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<UserRoleEntity> userRoles = new HashSet<>();

    // 🔹 Auditing fields
    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
}
