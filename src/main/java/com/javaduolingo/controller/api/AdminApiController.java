package com.javaduolingo.controller.api;

import com.javaduolingo.model.Order;
import com.javaduolingo.service.DashboardService;
import com.javaduolingo.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminApiController {

    private final DashboardService dashboardService;
    private final OrderService orderService;

    public AdminApiController(DashboardService dashboardService, OrderService orderService) {
        this.dashboardService = dashboardService;
        this.orderService = orderService;
    }

    @GetMapping("/dashboard/stats")
    public ResponseEntity<DashboardService.Stats> stats() {
        return ResponseEntity.ok(dashboardService.getStats());
    }

    @PutMapping("/pedidos/{id}/status")
    public ResponseEntity<Map<String, Object>> updateOrderStatus(
            @PathVariable Long id, @RequestBody Map<String, String> body) {
        try {
            Order.OrderStatus status = Order.OrderStatus.valueOf(body.get("status"));
            Order order = orderService.updateStatus(id, status);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "status", order.getStatus().name(),
                "label", order.getStatus().getLabel()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
}
