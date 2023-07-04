package com.example.giftgeek.Entities;

import org.json.JSONException;
import org.json.JSONObject;

public class Product {
    private int id;
    private String name;
    private String description;
    private String url;

    private String imageUrl;
    private double price;

    private int is_active;
    private String categories;

    // Constructor
    public Product(int id, String name, String description, String url, String imageUrl, double price, int is_active, String categories) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.url = url;
        this.imageUrl = imageUrl;
        this.price = price;
        this.is_active = is_active;
        this.categories = categories;
    }

    public static Product getProductFromJson(JSONObject o) throws JSONException {
        int id = o.getInt("id");
        String name = o.getString("name");
        String description = o.getString("description");
        String url = o.getString("link");
        String imageUrl = o.getString("photo");
        double price = o.getDouble("price");
        int is_active = o.getInt("is_active");
        //String categories = o.getString("categoryIds");
        String categories = new String();

        return new Product(id, name, description, url, imageUrl, price, is_active, categories);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getIs_active() {
        return is_active;
    }

    public void setIs_active(int is_active) {
        this.is_active = is_active;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }
}
