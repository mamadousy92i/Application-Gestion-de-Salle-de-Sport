package com.example.salledesport;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.salledesport.adapters.SubscriptionAdapter;
import com.example.salledesport.model.PaginatedResponse;
import com.example.salledesport.model.Subscription;
import com.example.salledesport.api.RetrofitClient;
import com.example.salledesport.api.ApiService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MySubscriptionsActivity extends AppCompatActivity implements SubscriptionAdapter.OnSubscriptionActionListener {

    private RecyclerView recyclerView;
    private SubscriptionAdapter adapter;
    private ProgressBar progressBar;
    private TextView tvNoSubscriptions;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_subscriptions);

        // Configurer la bottom navigation avec l'onglet Abonnement actif
        BottomNavigationHandler navHandler = new BottomNavigationHandler(this);
        navHandler.setupNavigation(BottomNavigationHandler.ActiveTab.ABONNEMENT);

        // Initialiser les vues
        recyclerView = findViewById(R.id.subscriptionsRecycler);
        progressBar = findViewById(R.id.progressBar);
        tvNoSubscriptions = findViewById(R.id.tvNoSubscriptions);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String userJson = getSharedPreferences("auth", MODE_PRIVATE).getString("user", null);
        String authToken = getSharedPreferences("prefs", MODE_PRIVATE).getString("auth_token", null);
        if (userJson == null || authToken == null) {
            Toast.makeText(this, "Erreur : utilisateur non connecté", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            // Initialiser l'API service
            apiService = RetrofitClient.getClient(getApplicationContext()).create(ApiService.class);

            // Configurer le swipe refresh
            swipeRefreshLayout.setOnRefreshListener(this::fetchSubscriptions);

            // Charger les abonnements
            fetchSubscriptions();
        }
    }

    private void fetchSubscriptions() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        tvNoSubscriptions.setVisibility(View.GONE);

        Call<PaginatedResponse<Subscription>> call = apiService.getMySubscriptions();

        call.enqueue(new Callback<PaginatedResponse<Subscription>>() {
            @Override
            public void onResponse(Call<PaginatedResponse<Subscription>> call, Response<PaginatedResponse<Subscription>> response) {
                progressBar.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);

                if (response.isSuccessful() && response.body() != null) {
                    List<Subscription> subscriptions = response.body().getResults();
                    if (subscriptions == null || subscriptions.isEmpty()) {
                        tvNoSubscriptions.setVisibility(View.VISIBLE);
                        tvNoSubscriptions.setText("Aucun abonnement trouvé");
                    } else {
                        // Filtrer les doublons (même plan, mêmes dates, même statut)
                        List<Subscription> filteredSubscriptions = new ArrayList<>();
                        Set<String> uniqueSubscriptions = new HashSet<>();

                        for (Subscription subscription : subscriptions) {
                            String key = subscription.getPlan().getName() + "_" +
                                    subscription.getStartDate() + "_" +
                                    subscription.getEndDate() + "_" +
                                    subscription.getStatus();
                            if (uniqueSubscriptions.add(key)) {
                                filteredSubscriptions.add(subscription);
                            }
                        }

                        if (filteredSubscriptions.isEmpty()) {
                            tvNoSubscriptions.setVisibility(View.VISIBLE);
                            tvNoSubscriptions.setText("Aucun abonnement trouvé");
                        } else {
                            recyclerView.setVisibility(View.VISIBLE);
                            adapter = new SubscriptionAdapter(filteredSubscriptions, MySubscriptionsActivity.this);
                            recyclerView.setAdapter(adapter);
                        }
                    }
                } else {
                    tvNoSubscriptions.setVisibility(View.VISIBLE);
                    tvNoSubscriptions.setText("Erreur serveur : " + response.code());
                    Toast.makeText(MySubscriptionsActivity.this, "Erreur serveur : " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<PaginatedResponse<Subscription>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                tvNoSubscriptions.setVisibility(View.VISIBLE);
                tvNoSubscriptions.setText("Erreur réseau : " + t.getMessage());
                Toast.makeText(MySubscriptionsActivity.this, "Échec : " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onRenewClick(Subscription subscription) {
        new AlertDialog.Builder(this)
                .setTitle("Renouveler l'abonnement")
                .setMessage("Voulez-vous renouveler votre abonnement " + subscription.getPlan().getName() + " ?")
                .setPositiveButton("Oui", (dialog, which) -> renewSubscription(subscription))
                .setNegativeButton("Non", null)
                .show();
    }

    @Override
    public void onCancelClick(Subscription subscription) {
        new AlertDialog.Builder(this)
                .setTitle("Annuler l'abonnement")
                .setMessage("Êtes-vous sûr de vouloir annuler votre abonnement " + subscription.getPlan().getName() + " ?")
                .setPositiveButton("Oui", (dialog, which) -> cancelSubscription(subscription))
                .setNegativeButton("Non", null)
                .show();
    }

    private void renewSubscription(Subscription subscription) {
        Call<Subscription> call = apiService.renewSubscription(subscription.getId());

        call.enqueue(new Callback<Subscription>() {
            @Override
            public void onResponse(Call<Subscription> call, Response<Subscription> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(MySubscriptionsActivity.this, "Abonnement renouvelé avec succès", Toast.LENGTH_LONG).show();
                    fetchSubscriptions(); // Rafraîchir la liste
                } else {
                    Toast.makeText(MySubscriptionsActivity.this, "Erreur lors du renouvellement : " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Subscription> call, Throwable t) {
                Toast.makeText(MySubscriptionsActivity.this, "Échec de la connexion : " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void cancelSubscription(Subscription subscription) {
        Call<Map<String, String>> call = apiService.cancelSubscription(subscription.getId());

        call.enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MySubscriptionsActivity.this, "Abonnement annulé avec succès", Toast.LENGTH_LONG).show();
                    fetchSubscriptions(); // Rafraîchir la liste
                } else {
                    Toast.makeText(MySubscriptionsActivity.this, "Erreur lors de l'annulation : " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                Toast.makeText(MySubscriptionsActivity.this, "Échec de la connexion : " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}