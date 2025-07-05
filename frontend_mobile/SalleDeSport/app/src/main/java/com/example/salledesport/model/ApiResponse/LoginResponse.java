package com.example.salledesport.model.ApiResponse;

import com.example.salledesport.model.Utilisateur;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    private Utilisateur user;
    private String message;

    @SerializedName("member_id")
    private int memberId;

    public Utilisateur getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }

    public int getMemberId() {
        return memberId;
    }

    public boolean isSuccess() {
        return user != null;
    }
}
