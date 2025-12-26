package com.example.demo.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "discount_applications")
public class DiscountApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "bundle_rule_id", nullable = false)
    private BundleRule bundleRule;

    @Column(nullable = false)
    private BigDecimal discountAmount;

    @Column(nullable = false)
    private Timestamp appliedAt;

    @PrePersist
    protected void onApply() {
        this.appliedAt = Timestamp.valueOf(LocalDateTime.now());
    }

    public Long getId() {
        return id;
    }

    public Cart getCart() {
        return cart;
    }

    public BundleRule getBundleRule() {
        return bundleRule;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public Timestamp getAppliedAt() {
        return appliedAt;
    }


    // ⚠️ Required by tests
    public void setId(Long id) {
        this.id = id;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public void setBundleRule(BundleRule bundleRule) {
        this.bundleRule = bundleRule;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public void setAppliedAt(LocalDateTime appliedAt) {
        this.appliedAt = Timestamp.valueOf(appliedAt);
    }
}
