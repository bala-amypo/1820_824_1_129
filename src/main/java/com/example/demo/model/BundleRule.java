package com.example.demo.model;

import jakarta.persistence.*;

@Entity
public class BundleRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String requiredProductIds;

    private Integer discountPercentage;

    private Boolean active = true;

    public boolean isDiscountPercentageValid() {
        return discountPercentage != null &&
                discountPercentage >= 1 &&
                discountPercentage <= 100;
    }

    public Long getId() {
        return id;
    }

    public String getRequiredProductIds() {
        return requiredProductIds;
    }

    public void setRequiredProductIds(String requiredProductIds) {
        this.requiredProductIds = requiredProductIds;
    }

    public Integer getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(Integer discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
