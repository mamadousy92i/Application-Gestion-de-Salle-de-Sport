package com.example.salledesport.api;


import com.example.salledesport.model.ApiResponse.SlideResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface SplashScreen {

    @GET("api/slider/slides")
    Call<SlideResponse> getSlides();
}

