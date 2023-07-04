package com.example.giftgeek.Entities;

public class Gift {
    private int id;
    private int wishlistId;
    private String productUrl;
    private String priority;
    private int priorityInt;
    private boolean booked;

    public Gift(int id, int wishlistId, String productUrl, int priorityInt, boolean booked) {
        this.id = id;
        this.wishlistId = wishlistId;
        this.productUrl = productUrl;
        this.priority = getPriorityString(priorityInt);
        this.priorityInt = priorityInt;
        this.booked = booked;
    }

    private String getPriorityString(int priorityInt) {
        if (priorityInt >= 0 && priorityInt < 4) {
            return "LOW";
        } else {
            if (priorityInt >= 4 && priorityInt < 7) {
                return "MEDIUM";
            } else {
                if (priorityInt >= 7 && priorityInt <= 10) {
                    return "HIGH";
                } else {
                    return "LOW";
                }
            }
        }
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

    public void setId(int id) {
        this.id = id;
    }

    public void setWishlistId(int wishlistId) {
        this.wishlistId = wishlistId;
    }

    public int getPriorityInt() {
        return priorityInt;
    }
}

