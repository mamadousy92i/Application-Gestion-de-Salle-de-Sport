package com.example.salledesport;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.salledesport.validation.ValidationActivityM;
import com.example.salledesport.validation.ValidationActivityPM;
import com.example.salledesport.validation.ValidationActivityPP;

public class InfoActivity extends AppCompatActivity {

    private ImageButton backButton;
    private CardView cardPriseMasse, cardPertePoids, cardMaintien;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        // Configurer la bottom navigation avec l'onglet Objectif actif
        BottomNavigationHandler navHandler = new BottomNavigationHandler(this);
        navHandler.setupNavigation(BottomNavigationHandler.ActiveTab.OBJECTIF);

        // Initialiser les vues
        initViews();

        // Configurer les animations d'entrée
        setupAnimations();

        // Configurer les listeners
        setupListeners();
    }

    private void initViews() {
        backButton = findViewById(R.id.backButton);
        cardPriseMasse = findViewById(R.id.cardPriseMasse);
        cardPertePoids = findViewById(R.id.cardPertePoids);
        cardMaintien = findViewById(R.id.cardMaintien);
    }

    private void setupAnimations() {
        // Animation de slide depuis la droite pour les cartes
        Animation slideFromRight = AnimationUtils.loadAnimation(this, R.anim.slide_from_right);
        Animation slideFromLeft = AnimationUtils.loadAnimation(this, R.anim.slide_from_left);
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        // Appliquer les animations avec des délais
        cardPriseMasse.postDelayed(() -> {
            cardPriseMasse.startAnimation(slideFromLeft);
            cardPriseMasse.setVisibility(View.VISIBLE);
        }, 200);

        cardPertePoids.postDelayed(() -> {
            cardPertePoids.startAnimation(fadeIn);
            cardPertePoids.setVisibility(View.VISIBLE);
        }, 400);

        cardMaintien.postDelayed(() -> {
            cardMaintien.startAnimation(slideFromRight);
            cardMaintien.setVisibility(View.VISIBLE);
        }, 600);
    }

    private void setupListeners() {
        // Bouton de retour
        backButton.setOnClickListener(v -> {
            // Animation de sortie
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            finish();
        });

        // Card Prise de Masse
        cardPriseMasse.setOnClickListener(v -> {
            animateCardClick(cardPriseMasse);
            // Démarrer l'activité de validation avec l'objectif "prise de masse"
            Intent intent = new Intent(InfoActivity.this, ValidationActivityPM.class);
            intent.putExtra("objectif", "prise_masse");
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        // Card Perte de Poids
        cardPertePoids.setOnClickListener(v -> {
            animateCardClick(cardPertePoids);
            // Démarrer l'activité de validation avec l'objectif "perte de poids"
            Intent intent = new Intent(InfoActivity.this, ValidationActivityPP.class);
            intent.putExtra("objectif", "perte_poids");
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        // Card Maintien
        cardMaintien.setOnClickListener(v -> {
            animateCardClick(cardMaintien);
            // Démarrer l'activité de validation avec l'objectif "maintien"
            Intent intent = new Intent(InfoActivity.this, ValidationActivityM.class);
            intent.putExtra("objectif", "maintien");
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
    }

    private void animateCardClick(CardView card) {
        // Animation de "pulse" lors du clic
        Animation pulse = AnimationUtils.loadAnimation(this, R.anim.pulse);
        card.startAnimation(pulse);
    }

    @Override
    public void onBackPressed() {
        // Animation personnalisée lors du retour
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}