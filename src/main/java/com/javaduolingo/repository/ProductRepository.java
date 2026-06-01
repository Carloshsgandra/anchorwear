package com.javaduolingo.repository;

import com.javaduolingo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByActiveTrue();
    List<Product> findByCategoryAndActiveTrue(String category);
    List<Product> findByBadgeAndActiveTrue(String badge);
    Optional<Product> findBySlug(String slug);
    boolean existsBySlug(String slug);
    long countByActive(boolean active);

    @Query("SELECT DISTINCT p.category FROM Product p WHERE p.active = true ORDER BY p.category")
    List<String> findDistinctCategories();

    @Query("SELECT p FROM Product p WHERE p.active = true AND " +
           "(LOWER(p.name) LIKE LOWER(CONCAT('%',:q,'%')) OR LOWER(p.category) LIKE LOWER(CONCAT('%',:q,'%')))")
    List<Product> searchByNameOrCategory(String q);
}
