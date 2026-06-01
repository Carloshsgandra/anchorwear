package com.javaduolingo.controller.api;

import com.javaduolingo.model.Coupon;
import com.javaduolingo.service.CouponService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/coupon")
public class CouponApiController {

    private final CouponService couponService;

    public CouponApiController(CouponService couponService) {
        this.couponService = couponService;
    }

    @PostMapping("/validate")
    public ResponseEntity<Map<String, Object>> validate(@RequestBody Map<String, String> body) {
        String code = body.getOrDefault("code", "").trim();
        String subtotalStr = body.getOrDefault("subtotal", "0");
        BigDecimal subtotal;
        try { subtotal = new BigDecimal(subtotalStr); } catch (Exception e) { subtotal = BigDecimal.ZERO; }

        Optional<Coupon> couponOpt = couponService.validate(code, subtotal);
        if (couponOpt.isPresent()) {
            Coupon c = couponOpt.get();
            BigDecimal discount = c.calculateDiscount(subtotal);
            return ResponseEntity.ok(Map.of(
                "valid", true,
                "code", c.getCode(),
                "description", c.getDescription(),
                "discount", discount
            ));
        }
        return ResponseEntity.ok(Map.of("valid", false, "message", "Cupom inválido ou expirado."));
    }
}
