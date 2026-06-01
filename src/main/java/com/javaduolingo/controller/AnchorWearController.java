package com.javaduolingo.controller;

import com.javaduolingo.model.Product;
import com.javaduolingo.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class AnchorWearController {

    private final ProductService productService;

    public AnchorWearController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    public String root() {
        return "redirect:/anchorwear";
    }

    @GetMapping({"/anchorwear", "/anchorwear/"})
    public String home(Model model) {
        model.addAttribute("featured", productService.findFeatured());
        model.addAttribute("newArrivals", productService.findNew());
        model.addAttribute("bestsellers", productService.findBestsellers());
        model.addAttribute("page", "home");
        return "anchorwear/index";
    }

    @GetMapping("/anchorwear/shop")
    public String shop(@RequestParam(required = false) String categoria, Model model) {
        if (categoria != null && !categoria.isBlank() && !categoria.equals("Todos")) {
            model.addAttribute("products", productService.findByCategory(categoria));
            model.addAttribute("activeCategory", categoria);
        } else {
            model.addAttribute("products", productService.findAll());
            model.addAttribute("activeCategory", "Todos");
        }
        model.addAttribute("categories", productService.findCategories());
        model.addAttribute("page", "shop");
        return "anchorwear/shop";
    }

    @GetMapping("/anchorwear/produto/{id}")
    public String product(@PathVariable Long id, Model model) {
        Optional<Product> productOpt = productService.findById(id);
        if (productOpt.isEmpty()) return "redirect:/anchorwear/shop";
        Product product = productOpt.get();
        model.addAttribute("product", product);
        model.addAttribute("related", productService.findRelated(product));
        model.addAttribute("page", "shop");
        return "anchorwear/product";
    }

    @GetMapping("/anchorwear/personalizar")
    public String personalize(Model model) {
        model.addAttribute("page", "personalize");
        return "anchorwear/personalize";
    }

    @GetMapping("/anchorwear/sobre")
    public String about(Model model) {
        model.addAttribute("page", "about");
        return "anchorwear/about";
    }

    @GetMapping("/anchorwear/contato")
    public String contact(Model model) {
        model.addAttribute("page", "contact");
        return "anchorwear/contact";
    }

    @GetMapping("/anchorwear/carrinho")
    public String cart(Model model) {
        model.addAttribute("page", "cart");
        return "anchorwear/cart";
    }
}
