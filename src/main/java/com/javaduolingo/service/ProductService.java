package com.javaduolingo.service;

import com.javaduolingo.dto.ProductForm;
import com.javaduolingo.model.Product;
import com.javaduolingo.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findAll() { return productRepository.findByActiveTrue(); }
    public List<Product> findAllIncludingInactive() { return productRepository.findAll(); }
    public Optional<Product> findById(Long id) { return productRepository.findById(id); }
    public Optional<Product> findBySlug(String slug) { return productRepository.findBySlug(slug); }

    public List<Product> findByCategory(String category) {
        return productRepository.findByCategoryAndActiveTrue(category);
    }

    public List<Product> findFeatured() {
        List<Product> all = productRepository.findByActiveTrue();
        return all.size() > 4 ? all.subList(0, 4) : all;
    }

    public List<Product> findNew() { return productRepository.findByBadgeAndActiveTrue("new"); }
    public List<Product> findBestsellers() { return productRepository.findByBadgeAndActiveTrue("bestseller"); }

    public List<Product> findRelated(Product product) {
        return productRepository.findByCategoryAndActiveTrue(product.getCategory())
                .stream()
                .filter(p -> !p.getId().equals(product.getId()))
                .limit(4)
                .collect(Collectors.toList());
    }

    public List<String> findCategories() {
        List<String> cats = productRepository.findDistinctCategories();
        List<String> result = new ArrayList<>();
        result.add("Todos");
        result.addAll(cats);
        return result;
    }

    public List<Product> search(String q) { return productRepository.searchByNameOrCategory(q); }

    @Transactional
    public Product create(ProductForm form) {
        if (productRepository.existsBySlug(form.getSlug())) {
            throw new IllegalArgumentException("Slug já existe.");
        }
        Product p = new Product();
        applyForm(p, form);
        return productRepository.save(p);
    }

    @Transactional
    public Product update(Long id, ProductForm form) {
        Product p = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado."));
        if (!p.getSlug().equals(form.getSlug()) && productRepository.existsBySlug(form.getSlug())) {
            throw new IllegalArgumentException("Slug já existe.");
        }
        applyForm(p, form);
        return productRepository.save(p);
    }

    @Transactional
    public void delete(Long id) {
        productRepository.findById(id).ifPresent(p -> {
            p.setActive(false);
            productRepository.save(p);
        });
    }

    private void applyForm(Product p, ProductForm form) {
        p.setName(form.getName());
        p.setSlug(form.getSlug());
        p.setCategory(form.getCategory());
        p.setPrice(form.getPrice());
        p.setDescription(form.getDescription());
        p.setStock(form.getStock());
        p.setImageUrl(form.getImageUrl());
        p.setBadge(form.getBadge() != null ? form.getBadge() : "");
        p.setBgColor(form.getBgColor() != null ? form.getBgColor() : "#1a1a1a");
        p.setActive(form.isActive());
        if (form.getSizesRaw() != null && !form.getSizesRaw().isBlank()) {
            p.setSizes(Arrays.stream(form.getSizesRaw().split(","))
                    .map(String::trim).filter(s -> !s.isEmpty()).collect(Collectors.toList()));
        }
        if (form.getColorsRaw() != null && !form.getColorsRaw().isBlank()) {
            p.setColors(Arrays.stream(form.getColorsRaw().split(","))
                    .map(String::trim).filter(s -> !s.isEmpty()).collect(Collectors.toList()));
        }
    }

    public long countActive() { return productRepository.countByActive(true); }

    public ProductForm toForm(Product p) {
        ProductForm f = new ProductForm();
        f.setName(p.getName());
        f.setSlug(p.getSlug());
        f.setCategory(p.getCategory());
        f.setPrice(p.getPrice());
        f.setDescription(p.getDescription());
        f.setStock(p.getStock());
        f.setImageUrl(p.getImageUrl());
        f.setBadge(p.getBadge());
        f.setBgColor(p.getBgColor());
        f.setActive(p.isActive());
        f.setSizesRaw(String.join(", ", p.getSizes()));
        f.setColorsRaw(String.join(", ", p.getColors()));
        return f;
    }
}
