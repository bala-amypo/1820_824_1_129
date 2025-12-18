package com.example.demo.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BundleRule {

    private Long id;
    private String ruleName;
    private List<Long> requiredProductIds;
    private Double discountPercentage;
    private Boolean active;
}
