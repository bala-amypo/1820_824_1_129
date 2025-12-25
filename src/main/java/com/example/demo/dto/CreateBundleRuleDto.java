package com.example.demo.dto;

public class CreateBundleRuleDto {
    private String ruleName;
    private String requiredProductIds;  // "1,2,3"
    private Double discountPercentage;
    
    // Constructors
    public CreateBundleRuleDto() {}
    
    // Getters and Setters
    public String getRuleName() { return ruleName; }
    public void setRuleName(String ruleName) { this.ruleName = ruleName; }
    
    public String getRequiredProductIds() { return requiredProductIds; }
    public void setRequiredProductIds(String requiredProductIds) { this.requiredProductIds = requiredProductIds; }
    
    public Double getDiscountPercentage() { return discountPercentage; }
    public void setDiscountPercentage(Double discountPercentage) { this.discountPercentage = discountPercentage; }
}
