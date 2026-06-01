package com.javaduolingo.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.PENDING;

    @Column(precision = 10, scale = 2)
    private BigDecimal subtotal = BigDecimal.ZERO;

    @Column(precision = 10, scale = 2)
    private BigDecimal discount = BigDecimal.ZERO;

    @Column(precision = 10, scale = 2)
    private BigDecimal shipping = BigDecimal.ZERO;

    @Column(precision = 10, scale = 2)
    private BigDecimal total = BigDecimal.ZERO;

    private String couponCode;
    private String paymentMethod;
    private String trackingCode;

    private String shippingName;
    private String shippingStreet;
    private String shippingNumber;
    private String shippingComplement;
    private String shippingNeighborhood;
    private String shippingCity;
    private String shippingState;
    private String shippingZip;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private List<OrderItem> items = new ArrayList<>();

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Order() {}

    public String getSubtotalFormatted() {
        return "R$ " + String.format("%.2f", subtotal).replace('.', ',');
    }
    public String getDiscountFormatted() {
        return "R$ " + String.format("%.2f", discount).replace('.', ',');
    }
    public String getShippingFormatted() {
        return shipping.compareTo(BigDecimal.ZERO) == 0 ? "Grátis"
                : "R$ " + String.format("%.2f", shipping).replace('.', ',');
    }
    public String getTotalFormatted() {
        return "R$ " + String.format("%.2f", total).replace('.', ',');
    }
    public boolean isHasDiscount() {
        return discount != null && discount.compareTo(BigDecimal.ZERO) > 0;
    }

    public String getShippingAddress() {
        return shippingStreet + ", " + shippingNumber
                + (shippingComplement != null && !shippingComplement.isBlank() ? " - " + shippingComplement : "")
                + " - " + shippingNeighborhood + ", " + shippingCity + "/" + shippingState;
    }

    public enum OrderStatus {
        PENDING("Aguardando Pagamento", "warning"),
        CONFIRMED("Pagamento Confirmado", "info"),
        PROCESSING("Em Preparação", "primary"),
        SHIPPED("Enviado", "secondary"),
        DELIVERED("Entregue", "success"),
        CANCELLED("Cancelado", "danger");

        private final String label;
        private final String cssClass;

        OrderStatus(String label, String cssClass) {
            this.label = label;
            this.cssClass = cssClass;
        }

        public String getLabel() { return label; }
        public String getCssClass() { return cssClass; }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    public BigDecimal getDiscount() { return discount; }
    public void setDiscount(BigDecimal discount) { this.discount = discount; }
    public BigDecimal getShipping() { return shipping; }
    public void setShipping(BigDecimal shipping) { this.shipping = shipping; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    public String getCouponCode() { return couponCode; }
    public void setCouponCode(String couponCode) { this.couponCode = couponCode; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public String getTrackingCode() { return trackingCode; }
    public void setTrackingCode(String trackingCode) { this.trackingCode = trackingCode; }
    public String getShippingName() { return shippingName; }
    public void setShippingName(String n) { this.shippingName = n; }
    public String getShippingStreet() { return shippingStreet; }
    public void setShippingStreet(String s) { this.shippingStreet = s; }
    public String getShippingNumber() { return shippingNumber; }
    public void setShippingNumber(String n) { this.shippingNumber = n; }
    public String getShippingComplement() { return shippingComplement; }
    public void setShippingComplement(String c) { this.shippingComplement = c; }
    public String getShippingNeighborhood() { return shippingNeighborhood; }
    public void setShippingNeighborhood(String n) { this.shippingNeighborhood = n; }
    public String getShippingCity() { return shippingCity; }
    public void setShippingCity(String c) { this.shippingCity = c; }
    public String getShippingState() { return shippingState; }
    public void setShippingState(String s) { this.shippingState = s; }
    public String getShippingZip() { return shippingZip; }
    public void setShippingZip(String z) { this.shippingZip = z; }
    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
