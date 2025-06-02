// SubscriptionService.java
package com.example.salledesport.service;

import com.example.salledesport.model.ApiResponse.SubscriptionPlanResponse;
import com.example.salledesport.model.SubscriptionPlan;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

// SubscriptionService.java
public interface SubscriptionService {
    @GET("api/subscriptions/plans/")
    Call<SubscriptionPlanResponse<SubscriptionPlan>> getPlans(); // Retirez le header Authorization
}