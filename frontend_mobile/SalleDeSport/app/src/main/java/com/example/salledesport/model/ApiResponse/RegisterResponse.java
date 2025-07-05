package com.example.salledesport.model.ApiResponse;

import com.example.salledesport.model.Utilisateur;

public class RegisterResponse {
    private Utilisateur user;

    public Utilisateur getUser() {
        return user;
    }

    public boolean isSuccess() {
        return user != null;
    }
}
