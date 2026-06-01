package com.javaduolingo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Controller
@RequestMapping("/anchorwear")
public class AnchorWearController {

    private static final List<Map<String, Object>> PRODUCTS = new ArrayList<>();

    static {
        PRODUCTS.add(product(1, "Camiseta Anchor Classic", "Camisetas", 89.90,
                "Nossa peça mais icônica. Algodão 100% penteado, corte oversize.",
                List.of("P", "M", "G", "GG", "XG"), List.of("Preto", "Branco"), "bestseller", "#1a1a1a", "classic"));
        PRODUCTS.add(product(2, "Camiseta Urban Wave", "Camisetas", 94.90,
                "Estampa exclusiva da coleção Urban. Peso médio 200g/m².",
                List.of("P", "M", "G", "GG"), List.of("Cinza", "Preto"), "new", "#555", "wave"));
        PRODUCTS.add(product(3, "Camiseta Deep Sea", "Camisetas", 89.90,
                "Edição limitada Deep Sea. Tinta de alta durabilidade.",
                List.of("P", "M", "G", "GG", "XG"), List.of("Azul Marinho", "Preto"), "", "#0d2b5e", "sea"));
        PRODUCTS.add(product(4, "Moletom Anchor Premium", "Moletons", 189.90,
                "Moletom heavy weight com interior felpudo. Capuz duplo.",
                List.of("P", "M", "G", "GG"), List.of("Preto", "Cinza Chumbo"), "bestseller", "#111", "hoodie"));
        PRODUCTS.add(product(5, "Camiseta Street Code", "Camisetas", 89.90,
                "Para quem faz parte da cultura. Tecido dry touch.",
                List.of("P", "M", "G", "GG", "XG"), List.of("Branco", "Bege"), "new", "#f5f0e8", "street"));
        PRODUCTS.add(product(6, "Moletom Hood City", "Moletons", 199.90,
                "Coleção Hood City. Bolso canguru e punhos em ribana.",
                List.of("M", "G", "GG"), List.of("Cinza Mescla", "Preto"), "", "#888", "hood"));
        PRODUCTS.add(product(7, "Camiseta Origin", "Camisetas", 94.90,
                "A origem de tudo. Bordado em alto relevo no peito.",
                List.of("P", "M", "G", "GG"), List.of("Preto"), "new", "#1a1a1a", "origin"));
        PRODUCTS.add(product(8, "Boné Anchor Cap", "Acessórios", 69.90,
                "Boné estruturado 6 painéis. Aba curva com logo bordado.",
                List.of("Único"), List.of("Preto", "Bege"), "", "#1a1a1a", "cap"));
        PRODUCTS.add(product(9, "Camiseta Tide", "Camisetas", 99.90,
                "Estampa gráfica exclusiva. Algodão orgânico premium.",
                List.of("P", "M", "G", "GG", "XG"), List.of("Off-white", "Preto"), "new", "#f0ece0", "tide"));
        PRODUCTS.add(product(10, "Mochila Anchor", "Acessórios", 149.90,
                "Mochila impermeável 20L. Compartimento para notebook.",
                List.of("Único"), List.of("Preto"), "bestseller", "#222", "bag"));
    }

    private static Map<String, Object> product(int id, String name, String category, double price,
                                                String desc, List<String> sizes, List<String> colors,
                                                String badge, String color, String slug) {
        Map<String, Object> p = new LinkedHashMap<>();
        p.put("id", id);
        p.put("name", name);
        p.put("category", category);
        p.put("price", price);
        p.put("priceFormatted", String.format("R$ %.2f", price).replace('.', ','));
        p.put("description", desc);
        p.put("sizes", sizes);
        p.put("colors", colors);
        p.put("badge", badge);
        p.put("bgColor", color);
        p.put("slug", slug);
        return p;
    }

    @GetMapping({"", "/"})
    public String home(Model model) {
        List<Map<String, Object>> featured = PRODUCTS.subList(0, 4);
        List<Map<String, Object>> newArrivals = PRODUCTS.stream()
                .filter(p -> "new".equals(p.get("badge"))).toList();
        List<Map<String, Object>> bestsellers = PRODUCTS.stream()
                .filter(p -> "bestseller".equals(p.get("badge"))).toList();
        model.addAttribute("featured", featured);
        model.addAttribute("newArrivals", newArrivals);
        model.addAttribute("bestsellers", bestsellers);
        return "anchorwear/index";
    }

    @GetMapping("/shop")
    public String shop(Model model) {
        model.addAttribute("products", PRODUCTS);
        model.addAttribute("categories", List.of("Todos", "Camisetas", "Moletons", "Acessórios"));
        return "anchorwear/shop";
    }

    @GetMapping("/produto/{id}")
    public String product(@PathVariable int id, Model model) {
        Optional<Map<String, Object>> product = PRODUCTS.stream()
                .filter(p -> (int) p.get("id") == id).findFirst();
        if (product.isEmpty()) return "redirect:/anchorwear/shop";
        model.addAttribute("product", product.get());
        List<Map<String, Object>> related = PRODUCTS.stream()
                .filter(p -> (int) p.get("id") != id
                        && p.get("category").equals(product.get().get("category")))
                .limit(4).toList();
        model.addAttribute("related", related);
        return "anchorwear/product";
    }

    @GetMapping("/personalizar")
    public String personalize(Model model) {
        return "anchorwear/personalize";
    }

    @GetMapping("/sobre")
    public String about(Model model) {
        return "anchorwear/about";
    }

    @GetMapping("/contato")
    public String contact(Model model) {
        return "anchorwear/contact";
    }

    @GetMapping("/carrinho")
    public String cart(Model model) {
        return "anchorwear/cart";
    }
}
