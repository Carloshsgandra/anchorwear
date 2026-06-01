package com.javaduolingo.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaduolingo.dto.CartItemDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    private final ObjectMapper objectMapper;

    public CartService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public List<CartItemDto> parseCartJson(String cartJson) {
        if (cartJson == null || cartJson.isBlank()) return new ArrayList<>();
        try {
            return objectMapper.readValue(cartJson, new TypeReference<List<CartItemDto>>() {});
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public BigDecimal subtotal(List<CartItemDto> items) {
        return items.stream()
                .map(CartItemDto::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculateShipping(String shippingType, BigDecimal subtotal) {
        if (subtotal.compareTo(BigDecimal.valueOf(299)) >= 0) return BigDecimal.ZERO;
        return "SEDEX".equalsIgnoreCase(shippingType)
                ? BigDecimal.valueOf(29.90)
                : BigDecimal.valueOf(14.90);
    }
}
