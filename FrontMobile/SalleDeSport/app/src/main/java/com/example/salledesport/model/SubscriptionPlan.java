package com.example.salledesport.model;

import com.google.gson.annotations.SerializedName;

public class SubscriptionPlan {

    private int id;
    private String name;
    private String description;
    private double price;

    @SerializedName("duration_months")
    private int durationMonths;

    @SerializedName("max_sessions_per_week")
    private int maxSessionsPerWeek;

    @SerializedName("includes_coach")
    private boolean includesCoach;

    @SerializedName("includes_group_classes")
    private boolean includesGroupClasses;

    @SerializedName("includes_pool")
    private boolean includesPool;
    
    private String image;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

    // Getters & Setters

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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getDurationMonths() {
        return durationMonths;
    }

    public void setDurationMonths(int durationMonths) {
        this.durationMonths = durationMonths;
    }

    public int getMaxSessionsPerWeek() {
        return maxSessionsPerWeek;
    }

    public void setMaxSessionsPerWeek(int maxSessionsPerWeek) {
        this.maxSessionsPerWeek = maxSessionsPerWeek;
    }

    public boolean isIncludesCoach() {
        return includesCoach;
    }

    public void setIncludesCoach(boolean includesCoach) {
        this.includesCoach = includesCoach;
    }

    public boolean isIncludesGroupClasses() {
        return includesGroupClasses;
    }

    public void setIncludesGroupClasses(boolean includesGroupClasses) {
        this.includesGroupClasses = includesGroupClasses;
    }

    public boolean isIncludesPool() {
        return includesPool;
    }

    public void setIncludesPool(boolean includesPool) {
        this.includesPool = includesPool;
    }
    
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
