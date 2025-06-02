package com.example.salledesport.model.ApiResponse;

import com.example.salledesport.model.Utilisateur;

public class LoginResponse {
    private Utilisateur user;
    private String message;

    public Utilisateur getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return user != null;
    }
}

