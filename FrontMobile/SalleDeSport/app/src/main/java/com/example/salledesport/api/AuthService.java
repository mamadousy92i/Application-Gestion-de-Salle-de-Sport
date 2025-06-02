package com.example.salledesport.api;

import com.example.salledesport.model.ApiResponse.LoginResponse;
import com.example.salledesport.model.ApiResponse.RegisterResponse;
import com.example.salledesport.model.AuthUser.LoginRequest;
import com.example.salledesport.model.Utilisateur;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {

    @POST("api/accounts/login/")
    Call<LoginResponse> LoginUser(@Body LoginRequest loginRequest);

    @POST("api/accounts/users/")
    Call<RegisterResponse>RegisterUser(@Body Utilisateur utilisateur);
}
