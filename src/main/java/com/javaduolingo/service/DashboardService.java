package com.javaduolingo.service;

import com.javaduolingo.model.Order;
import com.javaduolingo.repository.OrderRepository;
import com.javaduolingo.repository.ProductRepository;
import com.javaduolingo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class DashboardService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public DashboardService(OrderRepository orderRepository,
                            ProductRepository productRepository,
                            UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public Stats getStats() {
        Stats s = new Stats();
        s.totalOrders = orderRepository.count();
        s.pendingOrders = orderRepository.countByStatus(Order.OrderStatus.PENDING);
        s.totalRevenue = orderRepository.sumTotalRevenue();
        s.revenueThisMonth = orderRepository.sumRevenueFrom(LocalDate.now().withDayOfMonth(1).atStartOfDay());
        s.ordersToday = orderRepository.countOrdersFrom(LocalDate.now().atStartOfDay());
        s.totalProducts = productRepository.countByActive(true);
        s.totalUsers = userRepository.countByActive(true);
        s.recentOrders = orderRepository.findTop10ByOrderByCreatedAtDesc();
        return s;
    }

    public static class Stats {
        public long totalOrders;
        public long pendingOrders;
        public long ordersToday;
        public BigDecimal totalRevenue = BigDecimal.ZERO;
        public BigDecimal revenueThisMonth = BigDecimal.ZERO;
        public long totalProducts;
        public long totalUsers;
        public List<Order> recentOrders;

        public long getTotalOrders() { return totalOrders; }
        public long getPendingOrders() { return pendingOrders; }
        public long getOrdersToday() { return ordersToday; }
        public BigDecimal getTotalRevenue() { return totalRevenue; }
        public BigDecimal getRevenueThisMonth() { return revenueThisMonth; }
        public long getTotalProducts() { return totalProducts; }
        public long getTotalUsers() { return totalUsers; }
        public List<Order> getRecentOrders() { return recentOrders; }

        public String getTotalRevenueFormatted() {
            return "R$ " + String.format("%.2f", totalRevenue).replace('.', ',');
        }
        public String getRevenueThisMonthFormatted() {
            return "R$ " + String.format("%.2f", revenueThisMonth).replace('.', ',');
        }
    }
}
