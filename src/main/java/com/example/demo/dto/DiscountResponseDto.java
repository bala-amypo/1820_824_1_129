package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class DiscountResponseDto {
    private Long id;
    private String ruleName;
    private BigDecimal discountAmount;
    private LocalDateTime appliedAt;
    
    // Constructors, Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getRuleName() { return ruleName; }
    public void setRuleName(String ruleName) { this.ruleName = ruleName; }
    
    public BigDecimal getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }
    
    public LocalDateTime getAppliedAt() { return appliedAt; }
    public void setAppliedAt(LocalDateTime appliedAt) { this.appliedAt = appliedAt; }
}
