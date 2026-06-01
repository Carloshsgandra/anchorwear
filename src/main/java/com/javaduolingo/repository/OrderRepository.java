package com.javaduolingo.repository;

import com.javaduolingo.model.Order;
import com.javaduolingo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserOrderByCreatedAtDesc(User user);
    List<Order> findByStatusOrderByCreatedAtDesc(Order.OrderStatus status);
    List<Order> findAllByOrderByCreatedAtDesc();
    long countByStatus(Order.OrderStatus status);

    @Query("SELECT COALESCE(SUM(o.total), 0) FROM Order o WHERE o.status NOT IN ('CANCELLED', 'PENDING')")
    BigDecimal sumTotalRevenue();

    @Query("SELECT COALESCE(SUM(o.total), 0) FROM Order o WHERE o.status NOT IN ('CANCELLED', 'PENDING') AND o.createdAt >= :start")
    BigDecimal sumRevenueFrom(LocalDateTime start);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.createdAt >= :start")
    long countOrdersFrom(LocalDateTime start);

    List<Order> findTop10ByOrderByCreatedAtDesc();
}
