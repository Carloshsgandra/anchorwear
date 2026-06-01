package com.javaduolingo.service;

import com.javaduolingo.model.Coupon;
import com.javaduolingo.repository.CouponRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CouponService {

    private final CouponRepository couponRepository;

    public CouponService(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    public Optional<Coupon> validate(String code, BigDecimal orderValue) {
        return couponRepository.findByCodeIgnoreCase(code)
                .filter(c -> c.isValid(orderValue));
    }

    @Transactional
    public void markUsed(String code) {
        couponRepository.findByCodeIgnoreCase(code).ifPresent(c -> {
            c.setUsedCount(c.getUsedCount() + 1);
            couponRepository.save(c);
        });
    }

    public List<Coupon> findAll() { return couponRepository.findAll(); }

    @Transactional
    public Coupon save(Coupon coupon) { return couponRepository.save(coupon); }

    @Transactional
    public void delete(Long id) { couponRepository.deleteById(id); }
}
