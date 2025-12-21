package com.example.demo.model;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;

import java.util.List;

@Entity
public class BundleRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String ruleName;

    @ElementCollection
    private List<Long> requiredProductIds;

    private Double discountPercentage;
    private Boolean active;

    // ===== Getters =====

    public Long getId() {
        return id;
    }

    public String getRuleName() {
        return ruleName;
    }

    public List<Long> getRequiredProductIds() {
        return requiredProductIds;
    }

    public Double getDiscountPercentage() {
        return discountPercentage;
    }

    public Boolean getActive() {
        return active;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public void setRequiredProductIds(List<Long> requiredProductIds) {
        this.requiredProductIds = requiredProductIds;
    }

    public void setDiscountPercentage(Double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
