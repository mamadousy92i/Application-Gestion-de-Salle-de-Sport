package com.example.salledesport;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.salledesport.model.PaginatedResponse;
import com.example.salledesport.model.Subscription;
import com.example.salledesport.model.Utilisateur;
import com.example.salledesport.api.ApiService;
import com.example.salledesport.api.ProfileService;
import com.example.salledesport.api.RetrofitClient;
import com.example.salledesport.utils.BaseActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends BaseActivity {

    private CardView cardSubscriptions, cardProgram, currentSubscriptionCard, viewAllSubscriptionsCard;
    private LinearLayout subscriptionDetailsLayout;
    private TextView textUserName, subscriptionTitle, subscriptionDuration, subscriptionProgressText;
    private LinearLayout textNoSubscription;
    private ImageView profileImage, subscriptionImage;
    private ProgressBar subscriptionProgress;
    private ApiService apiService;
    private ProfileService profileService;
    private Utilisateur currentUser;
    private List<Subscription> userSubscriptions;
    private SimpleDateFormat apiDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Configurer le comportement des barres système (héritée de BaseActivity)
        setupSystemBarsBehavior();

        // Configurer la bottom navigation avec l'onglet Home actif
        BottomNavigationHandler navHandler = new BottomNavigationHandler(this);
        navHandler.setupNavigation(BottomNavigationHandler.ActiveTab.HOME);

        // Initialiser les services API
        apiService = RetrofitClient.getClient(getApplicationContext()).create(ApiService.class);
        profileService = RetrofitClient.getClient(getApplicationContext()).create(ProfileService.class);

        // Initialiser les vues
        initViews();

        // Configurer les listeners
        setupListeners();

        // Charger les données du profil et des abonnements
        loadUserProfile();
        loadUserSubscriptions();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recharger les abonnements à chaque retour sur l'écran d'accueil
        loadUserSubscriptions();
    }

    private void initViews() {
        // Cartes d'action
        cardSubscriptions = findViewById(R.id.cardSubscriptions);
        cardProgram = findViewById(R.id.cardProgram);
        // Informations utilisateur
        textUserName = findViewById(R.id.textUserName);
        profileImage = findViewById(R.id.profileImage);
        // Abonnement actuel
        currentSubscriptionCard = findViewById(R.id.currentSubscriptionCard);
        textNoSubscription = findViewById(R.id.textNoSubscription);
        subscriptionDetailsLayout = findViewById(R.id.subscriptionDetailsLayout);
        subscriptionTitle = findViewById(R.id.subscriptionTitle);
        subscriptionDuration = findViewById(R.id.subscriptionDuration);
        subscriptionProgress = findViewById(R.id.subscriptionProgress);
        subscriptionProgressText = findViewById(R.id.subscriptionProgressText);
        subscriptionImage = findViewById(R.id.subscriptionImage);
        // Abonnements disponibles
        viewAllSubscriptionsCard = findViewById(R.id.viewAllSubscriptionsCard);
    }

    private void setupListeners() {
        // Listeners pour les cartes d'action
        cardSubscriptions.setOnClickListener(v -> {
            // Ouvrir la liste des abonnements disponibles
            Intent intent = new Intent(Home.this, Abonnement.class);
            startActivity(intent);
        });

        cardProgram.setOnClickListener(v -> {
            // Fonctionnalité à venir
            Toast.makeText(this, "Fonctionnalité Programme à venir", Toast.LENGTH_SHORT).show();
        });

        // Listener pour la carte d'abonnement actuel
        currentSubscriptionCard.setOnClickListener(v -> {
            // Ouvrir la page des abonnements de l'utilisateur
            Intent intent = new Intent(Home.this, MySubscriptionsActivity.class);
            startActivity(intent);
        });

        // Listener pour voir tous les abonnements disponibles
        viewAllSubscriptionsCard.setOnClickListener(v -> {
            // Ouvrir la liste des abonnements disponibles
            Intent intent = new Intent(Home.this, Abonnement.class);
            startActivity(intent);
        });
    }

    private void loadUserProfile() {
        profileService.getProfile().enqueue(new Callback<Utilisateur>() {
            @Override
            public void onResponse(Call<Utilisateur> call, Response<Utilisateur> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentUser = response.body();
                    updateProfileUI();
                } else {
                    Toast.makeText(Home.this, "Erreur lors du chargement du profil", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Utilisateur> call, Throwable t) {
                Toast.makeText(Home.this, "Erreur de connexion: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateProfileUI() {
        if (currentUser != null) {
            // Mettre à jour le nom d'utilisateur
            textUserName.setText(currentUser.getFirst_name().toUpperCase());

            // Charger l'image de profil si disponible
            if (currentUser.getProfile_picture() != null && !currentUser.getProfile_picture().isEmpty()) {
                String imageUrl;
                if (currentUser.getProfile_picture().startsWith("http")) {
                    imageUrl = currentUser.getProfile_picture();
                } else {
                    imageUrl = RetrofitClient.getBaseUrl() + currentUser.getProfile_picture();
                }

                Glide.with(this)
                        .load(imageUrl)
                        .placeholder(R.drawable.ic_edit_profile)
                        .error(android.R.drawable.ic_menu_gallery)
                        .into(profileImage);
            }
        }
    }

    private void loadUserSubscriptions() {
        apiService.getMySubscriptions().enqueue(new Callback<PaginatedResponse<Subscription>>() {
            @Override
            public void onResponse(Call<PaginatedResponse<Subscription>> call, Response<PaginatedResponse<Subscription>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    userSubscriptions = response.body().getResults();
                    updateSubscriptionsUI();
                } else {
                    showNoSubscription();
                    Toast.makeText(Home.this, "Erreur lors du chargement des abonnements", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PaginatedResponse<Subscription>> call, Throwable t) {
                showNoSubscription();
                Toast.makeText(Home.this, "Erreur de connexion: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateSubscriptionsUI() {
        // Mettre à jour l'UI en fonction des abonnements de l'utilisateur
        if (userSubscriptions != null && !userSubscriptions.isEmpty()) {
            // Trouver l'abonnement actif
            Subscription activeSubscription = null;
            for (Subscription sub : userSubscriptions) {
                if ("active".equals(sub.getStatus())) {
                    activeSubscription = sub;
                    break;
                }
            }

            // Mettre à jour l'UI avec l'abonnement actif
            if (activeSubscription != null) {
                displayActiveSubscription(activeSubscription);
            } else {
                showNoSubscription();
            }
        } else {
            showNoSubscription();
        }
    }

    private void displayActiveSubscription(Subscription subscription) {
        // Afficher les détails de l'abonnement
        textNoSubscription.setVisibility(View.GONE);
        subscriptionDetailsLayout.setVisibility(View.VISIBLE);

        // Définir le titre de l'abonnement
        subscriptionTitle.setText(subscription.getPlan().getName().toUpperCase());

        // Calculer la durée et le pourcentage d'avancement
        try {
            Date startDate = apiDateFormat.parse(subscription.getStartDate());
            Date endDate = apiDateFormat.parse(subscription.getEndDate());
            Date currentDate = new Date();

            if (startDate != null && endDate != null) {
                // Calculer la durée totale en jours
                long totalDays = TimeUnit.MILLISECONDS.toDays(endDate.getTime() - startDate.getTime());

                // Calculer les jours écoulés
                long daysPassed = TimeUnit.MILLISECONDS.toDays(currentDate.getTime() - startDate.getTime());

                // Calculer le pourcentage d'avancement
                int progressPercentage = (int) ((daysPassed * 100) / totalDays);
                if (progressPercentage < 0) progressPercentage = 0;
                if (progressPercentage > 100) progressPercentage = 100;

                // Mettre à jour la barre de progression
                subscriptionProgress.setProgress(progressPercentage);
                subscriptionProgressText.setText(progressPercentage + "% terminé");

                // Formater la durée pour l'affichage
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(startDate);
                int weekNumber = calendar.get(Calendar.WEEK_OF_YEAR);

                subscriptionDuration.setText("Semaine " + weekNumber + " • " + subscription.getPlan().getDescription());
            }
        } catch (ParseException e) {
            subscriptionDuration.setText(subscription.getPlan().getDescription());
            subscriptionProgress.setProgress(0);
            subscriptionProgressText.setText("0% terminé");
        }

        // Charger l'image du plan d'abonnement
        if (subscription.getPlan().getImage() != null && !subscription.getPlan().getImage().isEmpty()) {
            String imageUrl;
            if (subscription.getPlan().getImage().startsWith("http")) {
                imageUrl = subscription.getPlan().getImage();
            } else {
                imageUrl = RetrofitClient.getBaseUrl() + subscription.getPlan().getImage();
            }

            Glide.with(this)
                    .load(imageUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .apply(new RequestOptions().centerCrop())
                    .error(R.drawable.gradient_background)
                    .into(subscriptionImage);
        }
    }

    private void showNoSubscription() {
        textNoSubscription.setVisibility(View.VISIBLE);
        subscriptionDetailsLayout.setVisibility(View.GONE);
    }
}