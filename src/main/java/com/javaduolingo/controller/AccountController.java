package com.javaduolingo.controller;

import com.javaduolingo.model.Order;
import com.javaduolingo.model.User;
import com.javaduolingo.service.OrderService;
import com.javaduolingo.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/conta")
public class AccountController {

    private final UserService userService;
    private final OrderService orderService;

    public AccountController(UserService userService, OrderService orderService) {
        this.userService = userService;
        this.orderService = orderService;
    }

    private User getUser(UserDetails ud) {
        return userService.findByEmail(ud.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    @GetMapping({"", "/"})
    public String dashboard(@AuthenticationPrincipal UserDetails ud, Model model) {
        User user = getUser(ud);
        List<Order> orders = orderService.findByUser(user);
        long delivered = orders.stream()
                .filter(o -> o.getStatus() == Order.OrderStatus.DELIVERED).count();
        long active = orders.stream()
                .filter(o -> o.getStatus() == Order.OrderStatus.PENDING
                        || o.getStatus() == Order.OrderStatus.PROCESSING
                        || o.getStatus() == Order.OrderStatus.CONFIRMED
                        || o.getStatus() == Order.OrderStatus.SHIPPED).count();
        model.addAttribute("user", user);
        model.addAttribute("orders", orders);
        model.addAttribute("totalOrders", orders.size());
        model.addAttribute("deliveredCount", delivered);
        model.addAttribute("activeCount", active);
        model.addAttribute("page", "conta");
        return "conta/dashboard";
    }

    @GetMapping("/pedidos")
    public String orders(@AuthenticationPrincipal UserDetails ud, Model model) {
        User user = getUser(ud);
        model.addAttribute("user", user);
        model.addAttribute("orders", orderService.findByUser(user));
        model.addAttribute("page", "conta");
        return "conta/pedidos";
    }

    @GetMapping("/pedido/{id}")
    public String orderDetail(@PathVariable Long id,
                              @AuthenticationPrincipal UserDetails ud, Model model) {
        User user = getUser(ud);
        Optional<Order> orderOpt = orderService.findById(id);
        if (orderOpt.isEmpty() || !orderOpt.get().getUser().getId().equals(user.getId())) {
            return "redirect:/conta/pedidos";
        }
        model.addAttribute("user", user);
        model.addAttribute("order", orderOpt.get());
        model.addAttribute("page", "conta");
        return "conta/pedido";
    }

    @GetMapping("/perfil")
    public String profile(@AuthenticationPrincipal UserDetails ud, Model model) {
        User user = getUser(ud);
        model.addAttribute("user", user);
        model.addAttribute("page", "conta");
        return "conta/perfil";
    }

    @PostMapping("/perfil")
    public String updateProfile(@AuthenticationPrincipal UserDetails ud,
                                @RequestParam String name,
                                @RequestParam(required = false) String phone,
                                @RequestParam(required = false) String cpf,
                                RedirectAttributes redirectAttributes) {
        User user = getUser(ud);
        userService.updateProfile(user, name, phone, cpf);
        redirectAttributes.addFlashAttribute("successMsg", "Perfil atualizado com sucesso!");
        return "redirect:/conta/perfil";
    }
}
