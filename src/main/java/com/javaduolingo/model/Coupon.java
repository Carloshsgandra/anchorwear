package com.javaduolingo.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "coupons")
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code;

    @Enumerated(EnumType.STRING)
    private DiscountType type = DiscountType.PERCENT;

    @Column(name = "discount_value", nullable = false, precision = 10, scale = 2)
    private BigDecimal value;

    @Column(precision = 10, scale = 2)
    private BigDecimal minOrderValue = BigDecimal.ZERO;

    private int maxUses = 0;
    private int usedCount = 0;
    private boolean active = true;
    private LocalDate expiresAt;

    public enum DiscountType { PERCENT, FIXED }

    public Coupon() {}

    public boolean isValid(BigDecimal orderValue) {
        if (!active) return false;
        if (expiresAt != null && LocalDate.now().isAfter(expiresAt)) return false;
        if (maxUses > 0 && usedCount >= maxUses) return false;
        if (minOrderValue != null && orderValue.compareTo(minOrderValue) < 0) return false;
        return true;
    }

    public BigDecimal calculateDiscount(BigDecimal orderValue) {
        if (type == DiscountType.PERCENT) {
            return orderValue.multiply(value).divide(BigDecimal.valueOf(100));
        }
        return value.min(orderValue);
    }

    public String getDescription() {
        if (type == DiscountType.PERCENT) return value.intValue() + "% de desconto";
        return "R$ " + String.format("%.2f", value).replace('.', ',') + " de desconto";
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public DiscountType getType() { return type; }
    public void setType(DiscountType type) { this.type = type; }
    public BigDecimal getValue() { return value; }
    public void setValue(BigDecimal value) { this.value = value; }
    public BigDecimal getMinOrderValue() { return minOrderValue; }
    public void setMinOrderValue(BigDecimal v) { this.minOrderValue = v; }
    public int getMaxUses() { return maxUses; }
    public void setMaxUses(int maxUses) { this.maxUses = maxUses; }
    public int getUsedCount() { return usedCount; }
    public void setUsedCount(int usedCount) { this.usedCount = usedCount; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public LocalDate getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDate expiresAt) { this.expiresAt = expiresAt; }
}
