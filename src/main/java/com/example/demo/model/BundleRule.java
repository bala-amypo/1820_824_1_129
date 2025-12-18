package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    name = "bundle_rules",
    uniqueConstraints = @UniqueConstraint(columnNames = "ruleName")
)
public class BundleRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String ruleName;

    @Column(nullable = false)
    private String requiredProductIds; // CSV: "1,2,3"

    @Column(nullable = false)
    private Double discountPercentage;

    private Boolean active = true;

    @PrePersist
    @PreUpdate
    public void validateDiscount() {
        if (discountPercentage == null ||
            discountPercentage < 0 ||
            discountPercentage > 100) {
            throw new RuntimeException("Invalid discount");
        }
    }

    // getters and setters
}
