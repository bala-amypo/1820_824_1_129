package com.example.demo.service.impl;

import com.example.demo.model.DiscountApplication;
import com.example.demo.repository.DiscountApplicationRepository;
import com.example.demo.service.DiscountService;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscountServiceImpl implements DiscountService {

    private final DiscountApplicationRepository discountRepository;

    public DiscountServiceImpl(DiscountApplicationRepository discountRepository) {
        this.discountRepository = discountRepository;
    }

    @Override
    public List<DiscountApplication> evaluateDiscounts(Long cartId) {
        return discountRepository.findByCartId(cartId);
    }

    @Override
    public DiscountApplication getApplicationById(Long id) {
        return discountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("DiscountApplication not found"));
    }

    @Override
    public List<DiscountApplication> getApplicationsForCart(Long cartId) {
        return discountRepository.findByCartId(cartId);
    }
}
