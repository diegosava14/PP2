package com.example.giftgeek.Entities;

public class Gift {
    private int id;
    private int wishlistId;
    private String productUrl;
    private String priority;
    private boolean booked;

    private String name;

    private String description;

    private Double price;

    private String imageUrl;

    public Gift(int id, int wishlistId, String productUrl, String priority, boolean booked) {
        this.id = id;
        this.wishlistId = wishlistId;
        this.productUrl = productUrl;
        this.priority = priority;
        this.booked = booked;
    }

    public int getId() {
        return id;
    }

    public int getWishlistId() {
        return wishlistId;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public String getPriority() {
        return priority;
    }

    public boolean isBooked() {
        return booked;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

