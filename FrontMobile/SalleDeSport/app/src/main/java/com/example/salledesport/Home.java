package com.example.salledesport;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.salledesport.model.Member;
import com.example.salledesport.api.ApiService;
import com.example.salledesport.api.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends AppCompatActivity {

    private CardView premiumPlanCard, proPlanCard;
    private LinearLayout navHome, navPlanning, navHealth, navProfile;
    private TextView textUserName;
    private ApiService apiService;
    private Member currentMember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialiser l'API service
        apiService = RetrofitClient.getClient(getApplicationContext()).create(ApiService.class);

        // Initialiser les vues
        initViews();
        
        // Configurer les listeners
        setupListeners();
        
        // Charger les données du membre
        loadMemberData();
    }
    
    private void initViews() {
        // Cartes des plans d'abonnement
        premiumPlanCard = findViewById(R.id.premiumPlanCard);
        proPlanCard = findViewById(R.id.proPlanCard);
        
        // Navigation du bas
        navHome = findViewById(R.id.navHome);
        navPlanning = findViewById(R.id.navPlanning);
        navHealth = findViewById(R.id.navHealth);
        navProfile = findViewById(R.id.navProfile);
        
        // Informations utilisateur
        textUserName = findViewById(R.id.textUserName);
    }
    
    private void setupListeners() {
        // Listeners pour les plans d'abonnement
        premiumPlanCard.setOnClickListener(v -> openSubscriptionDetails("premium"));
        proPlanCard.setOnClickListener(v -> openSubscriptionDetails("pro"));
        
        // Listeners pour la navigation du bas
        navHome.setOnClickListener(v -> {
            // Déjà sur la page d'accueil
        });
        
        navPlanning.setOnClickListener(v -> {
            // Ouvrir la page de planning
            Toast.makeText(this, "Fonctionnalité Planning à venir", Toast.LENGTH_SHORT).show();
        });
        
        navHealth.setOnClickListener(v -> {
            // Ouvrir la page de santé/statistiques
            Toast.makeText(this, "Fonctionnalité Santé à venir", Toast.LENGTH_SHORT).show();
        });
        
        navProfile.setOnClickListener(v -> {
            // Ouvrir la page de profil
            Intent intent = new Intent(Home.this, MySubscriptionsActivity.class);
            startActivity(intent);
        });
    }
    
    private void loadMemberData() {
        // Simuler le chargement des données du membre
        // Dans une vraie application, vous récupéreriez ces données depuis l'API
        textUserName.setText("MEMBRE");
        
        // Exemple d'appel API (à décommenter et adapter selon votre API)
        /*
        Call<Member> call = apiService.getCurrentMember();
        call.enqueue(new Callback<Member>() {
            @Override
            public void onResponse(Call<Member> call, Response<Member> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentMember = response.body();
                    textUserName.setText(currentMember.getFirstName().toUpperCase());
                }
            }
            
            @Override
            public void onFailure(Call<Member> call, Throwable t) {
                Toast.makeText(Home.this, "Erreur de connexion", Toast.LENGTH_SHORT).show();
            }
        });
        */
    }
    
    private void openSubscriptionDetails(String planType) {
        Intent intent = new Intent(Home.this, Abonnement.class);
        startActivity(intent);
    }
}