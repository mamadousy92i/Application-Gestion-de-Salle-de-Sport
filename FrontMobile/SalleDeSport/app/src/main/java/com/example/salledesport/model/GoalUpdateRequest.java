package com.example.salledesport.model;

public class GoalUpdateRequest {
    private String name;

    public GoalUpdateRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}