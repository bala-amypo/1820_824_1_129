package com.example.demo.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "discount_applications")
public class DiscountApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Cart cart;

    @ManyToOne(optional = false)
    private BundleRule bundleRule;

    private BigDecimal discountAmount;

    private Timestamp appliedAt;

    @PrePersist
    protected void onApply() {
        appliedAt = new Timestamp(System.currentTimeMillis());
    }

    // ---------- Getters & Setters ----------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public BundleRule getBundleRule() {
        return bundleRule;
    }

    public void setBundleRule(BundleRule bundleRule) {
        this.bundleRule = bundleRule;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Timestamp getAppliedAt() {
        return appliedAt;
    }

    public void setAppliedAt(Timestamp appliedAt) {
        this.appliedAt = appliedAt;
    }
}
