package com.javaduolingo.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ProductForm {

    @NotBlank(message = "Nome é obrigatório")
    private String name;

    @NotBlank(message = "Slug é obrigatório")
    private String slug;

    @NotBlank(message = "Categoria é obrigatória")
    private String category;

    @NotNull(message = "Preço é obrigatório")
    @DecimalMin(value = "0.01", message = "Preço deve ser maior que zero")
    private BigDecimal price;

    private String description;
    private int stock = 0;
    private String imageUrl;
    private String badge = "";
    private String bgColor = "#1a1a1a";
    private String sizesRaw;
    private String colorsRaw;
    private boolean active = true;

    public ProductForm() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getBadge() { return badge; }
    public void setBadge(String badge) { this.badge = badge; }
    public String getBgColor() { return bgColor; }
    public void setBgColor(String bgColor) { this.bgColor = bgColor; }
    public String getSizesRaw() { return sizesRaw; }
    public void setSizesRaw(String sizesRaw) { this.sizesRaw = sizesRaw; }
    public String getColorsRaw() { return colorsRaw; }
    public void setColorsRaw(String colorsRaw) { this.colorsRaw = colorsRaw; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
