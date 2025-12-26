package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
public class DiscountApplication {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Cart cart;

    @ManyToOne
    private BundleRule bundleRule;

    private BigDecimal discountAmount;
    private Timestamp appliedAt;

    @PrePersist
    public void onApply() {
        appliedAt = new Timestamp(System.currentTimeMillis());
    }

    public Long getId() { return id; }

    public Cart getCart() { return cart; }
    public void setCart(Cart cart) { this.cart = cart; }

    public BundleRule getBundleRule() { return bundleRule; }
    public void setBundleRule(BundleRule bundleRule) {
        this.bundleRule = bundleRule;
    }
    public void setId(Long id) {
    this.id = id;
}

public void setAppliedAt(java.time.LocalDateTime appliedAt) {
    this.appliedAt = appliedAt;
}


    public BigDecimal getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Timestamp getAppliedAt() { return appliedAt; }
}
