package com.javaduolingo.service;

import com.javaduolingo.dto.CartItemDto;
import com.javaduolingo.dto.CheckoutForm;
import com.javaduolingo.model.*;
import com.javaduolingo.repository.OrderRepository;
import com.javaduolingo.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;
    private final CouponService couponService;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository,
                        CartService cartService, CouponService couponService) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.cartService = cartService;
        this.couponService = couponService;
    }

    @Transactional
    public Order createOrder(User user, CheckoutForm form) {
        List<CartItemDto> cartItems = cartService.parseCartJson(form.getCartJson());
        if (cartItems.isEmpty()) throw new IllegalArgumentException("Carrinho vazio.");

        Order order = new Order();
        order.setUser(user);
        BigDecimal subtotal = BigDecimal.ZERO;

        for (CartItemDto ci : cartItems) {
            Optional<Product> productOpt = productRepository.findById(ci.getId());
            if (productOpt.isEmpty() || !productOpt.get().isActive()) continue;

            Product product = productOpt.get();
            OrderItem item = new OrderItem();
            item.setProductId(product.getId());
            item.setProductName(product.getName());
            item.setUnitPrice(product.getPrice());
            item.setQuantity(ci.getQty() > 0 ? ci.getQty() : 1);
            item.setSize(ci.getSize());
            item.setColor(ci.getColor());
            order.getItems().add(item);
            subtotal = subtotal.add(item.getTotal());
        }

        if (order.getItems().isEmpty()) throw new IllegalArgumentException("Nenhum produto válido no carrinho.");

        BigDecimal shipping = cartService.calculateShipping(form.getShippingType(), subtotal);
        BigDecimal discount = BigDecimal.ZERO;

        if (form.getCouponCode() != null && !form.getCouponCode().isBlank()) {
            Optional<Coupon> couponOpt = couponService.validate(form.getCouponCode(), subtotal);
            if (couponOpt.isPresent()) {
                discount = couponOpt.get().calculateDiscount(subtotal);
                order.setCouponCode(form.getCouponCode().toUpperCase());
                couponService.markUsed(form.getCouponCode());
            }
        }

        order.setSubtotal(subtotal);
        order.setDiscount(discount);
        order.setShipping(shipping);
        order.setTotal(subtotal.subtract(discount).add(shipping));
        order.setPaymentMethod(form.getPaymentMethod());
        order.setShippingName(form.getShippingName());
        order.setShippingStreet(form.getShippingStreet());
        order.setShippingNumber(form.getShippingNumber());
        order.setShippingComplement(form.getShippingComplement());
        order.setShippingNeighborhood(form.getShippingNeighborhood());
        order.setShippingCity(form.getShippingCity());
        order.setShippingState(form.getShippingState());
        order.setShippingZip(form.getShippingZip());
        order.setStatus(Order.OrderStatus.PENDING);

        return orderRepository.save(order);
    }

    public Optional<Order> findById(Long id) { return orderRepository.findById(id); }
    public List<Order> findByUser(User user) { return orderRepository.findByUserOrderByCreatedAtDesc(user); }
    public List<Order> findAll() { return orderRepository.findAllByOrderByCreatedAtDesc(); }
    public List<Order> findRecent() { return orderRepository.findTop10ByOrderByCreatedAtDesc(); }

    @Transactional
    public Order updateStatus(Long id, Order.OrderStatus newStatus) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado."));
        order.setStatus(newStatus);
        return orderRepository.save(order);
    }

    @Transactional
    public Order updateTracking(Long id, String trackingCode) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado."));
        order.setTrackingCode(trackingCode);
        if (order.getStatus() == Order.OrderStatus.CONFIRMED || order.getStatus() == Order.OrderStatus.PROCESSING) {
            order.setStatus(Order.OrderStatus.SHIPPED);
        }
        return orderRepository.save(order);
    }

    public long countByStatus(Order.OrderStatus status) { return orderRepository.countByStatus(status); }
    public long countTotal() { return orderRepository.count(); }
}
