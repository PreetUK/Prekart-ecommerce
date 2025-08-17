package com.E_Commerce.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer productId; // Primary Key

    @NotBlank(message = "Name can not be empty")
    @Size(min = 3, max = 50, message = "Name should be at least 3 characters or less than 15 characters")
    private String name;

    @NotNull(message = "Price can not be empty")
    @Positive(message = "Price value should be positive")
    private Double price;

    @NotNull(message = "Quantity can not be empty")
    @Positive(message = "Quantity should be positive")
    private Integer quantity;

    @Size(min = 15, max = 300, message = "Description should be at least 15 characters or less than 300 characters")
    private String description;

    private String category;
    private String imageUrl;

    // Getters and Setters

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
