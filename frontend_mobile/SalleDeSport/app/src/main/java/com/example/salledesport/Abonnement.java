package com.example.salledesport;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salledesport.adapters.SubscriptionPlanAdapter;
import com.example.salledesport.model.SubscriptionPlan;
import com.example.salledesport.api.ApiService;
import com.example.salledesport.api.RetrofitClient;
import com.example.salledesport.utils.BaseActivity;
import com.google.android.material.card.MaterialCardView;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Abonnement extends BaseActivity implements SubscriptionPlanAdapter.OnPlanClickListener {

    private RecyclerView recyclerView;
    private SubscriptionPlanAdapter adapter;
    private ProgressBar progressBar;
    private TextView tvNoPlans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abonnement);

        // Configurer le comportement des barres système
        setupSystemBarsBehavior();

        // Initialiser les vues
        recyclerView = findViewById(R.id.recyclerViewPlans);
        progressBar = findViewById(R.id.progressBar);
        tvNoPlans = findViewById(R.id.tvNoPlans);
        MaterialCardView backButton = findViewById(R.id.back_button);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Charger les plans d'abonnement
        loadSubscriptionPlans();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        // Masquer les barres système quand l'activité devient visible
        hideSystemUI();

    }

    private void loadSubscriptionPlans() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        tvNoPlans.setVisibility(View.GONE);

        ApiService apiService = RetrofitClient.getClient(getApplicationContext()).create(ApiService.class);
        Call<com.example.salledesport.model.PaginatedResponse<SubscriptionPlan>> call = apiService.getSubscriptionPlans();

        call.enqueue(new Callback<com.example.salledesport.model.PaginatedResponse<SubscriptionPlan>>() {
            @Override
            public void onResponse(Call<com.example.salledesport.model.PaginatedResponse<SubscriptionPlan>> call, Response<com.example.salledesport.model.PaginatedResponse<SubscriptionPlan>> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    List<SubscriptionPlan> plans = response.body().getResults();

                    if (plans != null && !plans.isEmpty()) {
                        recyclerView.setVisibility(View.VISIBLE);
                        adapter = new SubscriptionPlanAdapter(plans, Abonnement.this);
                        recyclerView.setAdapter(adapter);
                    } else {
                        tvNoPlans.setVisibility(View.VISIBLE);
                        tvNoPlans.setText("Aucun plan d'abonnement trouvé.");
                    }
                } else {
                    tvNoPlans.setVisibility(View.VISIBLE);
                    tvNoPlans.setText("Erreur lors du chargement des plans: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<com.example.salledesport.model.PaginatedResponse<SubscriptionPlan>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                tvNoPlans.setVisibility(View.VISIBLE);
                tvNoPlans.setText("Erreur réseau: " + t.getMessage());
                Toast.makeText(Abonnement.this, "Erreur de connexion: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onPlanClick(SubscriptionPlan plan) {
        // Ouvrir l'activité de détail du plan
        Intent intent = new Intent(this, PlanDetailActivity.class);
        intent.putExtra("plan", new Gson().toJson(plan));
        startActivity(intent);
    }
}