package com.study.springbootassignment.entity;

import com.study.springbootassignment.util.AccountType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class AccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_holder_name")
    private String accountHolderName;

    @Column(name = "account_holder_email")
    private String accountHolderEmail;

    @Column(name = "account_holder_phone")
    private String accountHolderPhone;

    @Column(name = "national_id")
    private String nationalId;

    @Column(name = "account_number", nullable = false, unique = true)
    private String accountNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "account_type")
    private AccountType accountType; // SAVINGS, CHECKING, BUSINESS

    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.valueOf(0.00); // Default 0.00

    @Column(nullable = false)
    private Boolean status = true; // Default true

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private UserEntity createdBy; // Which user created the account

    // 🔹 Auditing fields
    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
}
