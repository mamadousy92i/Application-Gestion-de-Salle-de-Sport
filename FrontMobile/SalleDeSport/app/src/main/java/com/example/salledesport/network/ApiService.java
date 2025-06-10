package com.example.salledesport.network;

import com.example.salledesport.model.Goal;  // Assure-toi d'importer le modèle Goal

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    // Cette méthode récupère la liste des objectifs depuis l'API
    @GET("goals/")  // Remplace par l'URL complète de l'API si nécessaire
    Call<List<Goal>> getGoals();
}
