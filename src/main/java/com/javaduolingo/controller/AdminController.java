package com.javaduolingo.controller;

import com.javaduolingo.dto.ProductForm;
import com.javaduolingo.model.Order;
import com.javaduolingo.model.Product;
import com.javaduolingo.service.DashboardService;
import com.javaduolingo.service.OrderService;
import com.javaduolingo.service.ProductService;
import com.javaduolingo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final DashboardService dashboardService;
    private final ProductService productService;
    private final OrderService orderService;
    private final UserService userService;

    public AdminController(DashboardService dashboardService, ProductService productService,
                           OrderService orderService, UserService userService) {
        this.dashboardService = dashboardService;
        this.productService = productService;
        this.orderService = orderService;
        this.userService = userService;
    }

    // Dashboard
    @GetMapping({"", "/"})
    public String dashboard(Model model) {
        model.addAttribute("stats", dashboardService.getStats());
        model.addAttribute("statusValues", Order.OrderStatus.values());
        model.addAttribute("adminPage", "dashboard");
        return "admin/dashboard";
    }

    // Produtos
    @GetMapping("/produtos")
    public String produtos(Model model) {
        model.addAttribute("products", productService.findAllIncludingInactive());
        model.addAttribute("adminPage", "produtos");
        return "admin/produtos";
    }

    @GetMapping("/produtos/novo")
    public String novoProduto(Model model) {
        model.addAttribute("form", new ProductForm());
        model.addAttribute("adminPage", "produtos");
        model.addAttribute("editMode", false);
        return "admin/produto-form";
    }

    @PostMapping("/produtos/novo")
    public String criarProduto(@Valid @ModelAttribute("form") ProductForm form,
                               BindingResult result, Model model,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("adminPage", "produtos");
            model.addAttribute("editMode", false);
            return "admin/produto-form";
        }
        try {
            productService.create(form);
            redirectAttributes.addFlashAttribute("successMsg", "Produto criado com sucesso!");
            return "redirect:/admin/produtos";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMsg", e.getMessage());
            model.addAttribute("adminPage", "produtos");
            model.addAttribute("editMode", false);
            return "admin/produto-form";
        }
    }

    @GetMapping("/produtos/{id}/editar")
    public String editarProduto(@PathVariable Long id, Model model) {
        Optional<Product> productOpt = productService.findById(id);
        if (productOpt.isEmpty()) return "redirect:/admin/produtos";
        model.addAttribute("form", productService.toForm(productOpt.get()));
        model.addAttribute("productId", id);
        model.addAttribute("adminPage", "produtos");
        model.addAttribute("editMode", true);
        return "admin/produto-form";
    }

    @PostMapping("/produtos/{id}/editar")
    public String atualizarProduto(@PathVariable Long id,
                                   @Valid @ModelAttribute("form") ProductForm form,
                                   BindingResult result, Model model,
                                   RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("productId", id);
            model.addAttribute("adminPage", "produtos");
            model.addAttribute("editMode", true);
            return "admin/produto-form";
        }
        try {
            productService.update(id, form);
            redirectAttributes.addFlashAttribute("successMsg", "Produto atualizado!");
            return "redirect:/admin/produtos";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMsg", e.getMessage());
            model.addAttribute("productId", id);
            model.addAttribute("adminPage", "produtos");
            model.addAttribute("editMode", true);
            return "admin/produto-form";
        }
    }

    @PostMapping("/produtos/{id}/excluir")
    public String excluirProduto(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        productService.delete(id);
        redirectAttributes.addFlashAttribute("successMsg", "Produto desativado.");
        return "redirect:/admin/produtos";
    }

    // Pedidos
    @GetMapping("/pedidos")
    public String pedidos(@RequestParam(required = false) String status, Model model) {
        if (status != null && !status.isBlank()) {
            try {
                Order.OrderStatus s = Order.OrderStatus.valueOf(status);
                model.addAttribute("orders", orderService.findAll().stream()
                        .filter(o -> o.getStatus() == s).toList());
                model.addAttribute("statusFilter", status);
            } catch (IllegalArgumentException e) {
                model.addAttribute("orders", orderService.findAll());
            }
        } else {
            model.addAttribute("orders", orderService.findAll());
        }
        model.addAttribute("statusValues", Order.OrderStatus.values());
        model.addAttribute("adminPage", "pedidos");
        return "admin/pedidos";
    }

    @GetMapping("/pedido/{id}")
    public String pedidoDetalhe(@PathVariable Long id, Model model) {
        Optional<Order> orderOpt = orderService.findById(id);
        if (orderOpt.isEmpty()) return "redirect:/admin/pedidos";
        model.addAttribute("order", orderOpt.get());
        model.addAttribute("statusValues", Order.OrderStatus.values());
        model.addAttribute("adminPage", "pedidos");
        return "admin/pedido";
    }

    @PostMapping("/pedido/{id}/status")
    public String atualizarStatus(@PathVariable Long id,
                                  @RequestParam String status,
                                  RedirectAttributes redirectAttributes) {
        try {
            Order.OrderStatus s = Order.OrderStatus.valueOf(status);
            orderService.updateStatus(id, s);
            redirectAttributes.addFlashAttribute("successMsg", "Status atualizado: " + s.getLabel());
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMsg", "Status inválido.");
        }
        return "redirect:/admin/pedido/" + id;
    }

    @PostMapping("/pedido/{id}/rastreio")
    public String atualizarRastreio(@PathVariable Long id,
                                    @RequestParam String trackingCode,
                                    RedirectAttributes redirectAttributes) {
        orderService.updateTracking(id, trackingCode);
        redirectAttributes.addFlashAttribute("successMsg", "Código de rastreio salvo!");
        return "redirect:/admin/pedido/" + id;
    }

    // Usuários
    @GetMapping("/usuarios")
    public String usuarios(Model model) {
        model.addAttribute("users", userService.findAll());
        model.addAttribute("adminPage", "usuarios");
        return "admin/usuarios";
    }

    @PostMapping("/usuarios/{id}/toggle")
    public String toggleUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        userService.toggleActive(id);
        redirectAttributes.addFlashAttribute("successMsg", "Status do usuário alterado.");
        return "redirect:/admin/usuarios";
    }
}
