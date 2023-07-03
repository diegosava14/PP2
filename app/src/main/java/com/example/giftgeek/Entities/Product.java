package com.example.giftgeek.Entities;

public class Product {
    private String name;
    private String description;
    private double price;
    private String url;
    private String imageUrl;

    // Constructor
    public Product(String name, String description, double price, String url, String imageUrl) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.url = url;
        this.imageUrl = imageUrl;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUrl() {
        return this.url;
    }
}
