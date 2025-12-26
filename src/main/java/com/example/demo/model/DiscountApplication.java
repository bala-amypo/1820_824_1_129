package com.example.demo.model;

public class DiscountApplication {

    private String description;

    private double amount;

    public DiscountApplication(String description, double amount) {
        this.description = description;
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }
}
