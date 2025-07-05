package com.example.salledesport;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.salledesport.adapters.PaymentAdapter;
import com.example.salledesport.api.ApiService;
import com.example.salledesport.api.RetrofitClient;
import com.example.salledesport.model.PaginatedResponse;
import com.example.salledesport.model.Payment;
import com.example.salledesport.utils.BaseActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentHistoryActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private PaymentAdapter adapter;
    private ProgressBar progressBar;
    private TextView tvNoPayments;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_history);

        // Configurer le comportement des barres système
        setupSystemBarsBehavior();

        // Initialiser les vues
        recyclerView = findViewById(R.id.paymentsRecycler);
        progressBar = findViewById(R.id.progressBar);
        tvNoPayments = findViewById(R.id.tvNoPayments);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Configurer le bouton retour
        findViewById(R.id.back_button).setOnClickListener(v -> finish());

        String authToken = getSharedPreferences("prefs", MODE_PRIVATE).getString("auth_token", null);
        if (authToken == null) {
            Toast.makeText(this, "Erreur : utilisateur non connecté", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            // Initialiser l'API service
            apiService = RetrofitClient.getClient(getApplicationContext()).create(ApiService.class);

            // Configurer le swipe refresh
            swipeRefreshLayout.setOnRefreshListener(this::fetchPayments);

            // Charger l'historique des paiements
            fetchPayments();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Masquer les barres système quand l'activité devient visible
        hideSystemUI();
    }

    private void fetchPayments() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        tvNoPayments.setVisibility(View.GONE);

        Call<PaginatedResponse<Payment>> call = apiService.getPayments();

        call.enqueue(new Callback<PaginatedResponse<Payment>>() {
            @Override
            public void onResponse(Call<PaginatedResponse<Payment>> call, Response<PaginatedResponse<Payment>> response) {
                progressBar.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);

                if (response.isSuccessful() && response.body() != null) {
                    List<Payment> payments = response.body().getResults();
                    if (payments == null || payments.isEmpty()) {
                        tvNoPayments.setVisibility(View.VISIBLE);
                        tvNoPayments.setText("Aucun paiement trouvé");
                    } else {
                        recyclerView.setVisibility(View.VISIBLE);
                        adapter = new PaymentAdapter(payments);
                        recyclerView.setAdapter(adapter);
                    }
                } else {
                    tvNoPayments.setVisibility(View.VISIBLE);
                    tvNoPayments.setText("Erreur serveur : " + response.code());
                    Toast.makeText(PaymentHistoryActivity.this, "Erreur serveur : " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<PaginatedResponse<Payment>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                tvNoPayments.setVisibility(View.VISIBLE);
                tvNoPayments.setText("Erreur réseau : " + t.getMessage());
                Toast.makeText(PaymentHistoryActivity.this, "Échec : " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}