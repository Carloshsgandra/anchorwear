package com.javaduolingo.dto;

import java.math.BigDecimal;

public class CartItemDto {
    private Long id;
    private String name;
    private BigDecimal price;
    private String size;
    private String color;
    private int qty;
    private String imageUrl;

    public CartItemDto() {}

    public BigDecimal getTotal() {
        if (price == null) return BigDecimal.ZERO;
        return price.multiply(BigDecimal.valueOf(qty));
    }

    public String getPriceFormatted() {
        if (price == null) return "R$ 0,00";
        return "R$ " + String.format("%.2f", price).replace('.', ',');
    }

    public String getTotalFormatted() {
        return "R$ " + String.format("%.2f", getTotal()).replace('.', ',');
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public int getQty() { return qty; }
    public void setQty(int qty) { this.qty = qty; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
