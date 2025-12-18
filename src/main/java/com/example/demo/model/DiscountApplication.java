package com.example.demo.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class DiscountApplication {

    private Long id;
    private Cart cart;
    private BundleRule bundleRule;
    private BigDecimal discountAmount;
}
