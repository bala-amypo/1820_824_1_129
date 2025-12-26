package com.example.demo.model;

import jakarta.persistence.*;

@Entity
public class BundleRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ruleName;

    private String requiredProductIds;

    private double discountPercentage;

    private Boolean active = true;

    // ======== TEST-REQUIRED METHODS ========

    public boolean isDiscountPercentageValid() {
        return discountPercentage >= 1 && discountPercentage <= 100;
    }

    // ======== GETTERS & SETTERS ========

    public Long getId() {
        return id;
    }

    // Tests explicitly call setId(...)
    public void setId(Long id) {
        this.id = id;
    }

    public String getRuleName() {
        return ruleName;
    }

    // Tests explicitly call setRuleName(...)
    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getRequiredProductIds() {
        return requiredProductIds;
    }

    public void setRequiredProductIds(String requiredProductIds) {
        this.requiredProductIds = requiredProductIds;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    // Tests pass double literals
    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
