package com.example.salledesport.api;

import com.example.salledesport.model.Goal;
import com.example.salledesport.model.PaginatedResponse;
import com.example.salledesport.model.SubscriptionPlan;
import com.example.salledesport.model.Subscription;
import com.example.salledesport.model.Payment;

import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.POST;
import retrofit2.http.Body;
import com.example.salledesport.model.Measurement;

public interface ApiService {

    // Récupérer les plans d'abonnement
    @GET("api/subscriptions/plans/")
    Call<PaginatedResponse<SubscriptionPlan>> getSubscriptionPlans();

    // Créer un abonnement
    @POST("api/subscriptions/subscriptions/")
    Call<Subscription> createSubscription(@Body Subscription subscription);

    // Créer un paiement
    @POST("api/subscriptions/payments/")
    Call<Payment> createPayment(@Body Payment payment);

    // Récupérer les abonnements de l'utilisateur connecté
    @GET("api/subscriptions/subscriptions/")
    Call<PaginatedResponse<Subscription>> getMySubscriptions();

    // Récupérer les paiements de l'utilisateur
    //@GET("api/subscriptions/payments/")
    //Call<List<Payment>> getMyPayments();

    // Récupérer tous les payments
    @GET("api/subscriptions/payments/")
    Call<PaginatedResponse<Payment>> getPayments();

    // Renouveler un abonnement
    @POST("api/subscriptions/subscriptions/{id}/renew/")
    Call<Subscription> renewSubscription(@Path("id") int subscriptionId);

    // Annuler un abonnement
    @POST("api/subscriptions/subscriptions/{id}/cancel/")
    Call<Map<String, String>> cancelSubscription(@Path("id") int subscriptionId);

    // Obtenir les revenus mensuels (pour les administrateurs)
    @GET("api/subscriptions/payments/monthly_revenue/{year}/")
    Call<Map<String, Double>> getMonthlyRevenue(@Path("year") int year);

    // Obtenir les statistiques globales (pour les administrateurs)
    @GET("api/subscriptions/statistics/")
    Call<Map<String, Object>> getStatistics();


    // Récupérer les objectifs de l'utilisateur connecté
    @GET("api/programs/goals/")
    Call<PaginatedResponse<Goal>> getGoals();


    // =========================
    // ==== MESURES UTILISATEUR
    // =========================

    // Récupérer les mesures de l'utilisateur connecté
    @GET("api/accounts/measurements/")
    Call<List<Measurement>> getMeasurements(@Header("Authorization") String token);

    // Ajouter une mesure
    @POST("api/accounts/measurements/")
    Call<Measurement> addMeasurement(@Header("Authorization") String token, @Body Measurement measurement);
}
