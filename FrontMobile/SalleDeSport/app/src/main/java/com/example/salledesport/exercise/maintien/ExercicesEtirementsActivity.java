package com.example.salledesport.exercise.maintien;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.salledesport.R;
import com.example.salledesport.exercise.maintien.nutrition.NutritionActivityM;

public class ExercicesEtirementsActivity extends AppCompatActivity {

    private ImageButton backButton;
    private CardView cardEtirementCou, cardEtirementDos, cardEtirementJambes, cardEtirementBras;
    private LinearLayout continueButton; // LinearLayout pour le bouton "Continuer"
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercices_etirements);

        // Initialiser les vues
        initViews();

        // Vérifier si le backButton n'est pas null
        backButton = findViewById(R.id.btnBack);
        if (backButton != null) {
            backButton.setOnClickListener(v -> {
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            });
        } else {
            Log.e("ExercicesEtirementsActivity", "backButton is null");
        }

        // Configurer les animations d'entrée
        setupAnimations();

        // Configurer les listeners pour les autres vues
        setupListeners();
    }

    private void initViews() {
        // Initialisation des vues des cartes d'étirement
        backButton = findViewById(R.id.btnBack);
        cardEtirementCou = findViewById(R.id.cardEtirementCou);
        cardEtirementDos = findViewById(R.id.cardEtirementDos);
        cardEtirementJambes = findViewById(R.id.cardEtirementJambes);
        cardEtirementBras = findViewById(R.id.cardEtirementBras);

        // Initialisation du bouton "Continuer"
        continueButton = findViewById(R.id.continueButton);
    }

    private void setupAnimations() {
        // Animation de fade in pour les cartes
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation slideFromLeft = AnimationUtils.loadAnimation(this, R.anim.slide_from_left);
        Animation slideFromRight = AnimationUtils.loadAnimation(this, R.anim.slide_from_right);

        // Appliquer les animations avec des délais
        cardEtirementCou.postDelayed(() -> {
            cardEtirementCou.startAnimation(slideFromLeft);
            cardEtirementCou.setVisibility(View.VISIBLE);
        }, 200);

        cardEtirementDos.postDelayed(() -> {
            cardEtirementDos.startAnimation(slideFromRight);
            cardEtirementDos.setVisibility(View.VISIBLE);
        }, 300);

        cardEtirementJambes.postDelayed(() -> {
            cardEtirementJambes.startAnimation(slideFromLeft);
            cardEtirementJambes.setVisibility(View.VISIBLE);
        }, 400);

        cardEtirementBras.postDelayed(() -> {
            cardEtirementBras.startAnimation(slideFromRight);
            cardEtirementBras.setVisibility(View.VISIBLE);
        }, 500);
    }

    private void setupListeners() {
        // Configurer le listener pour le bouton "Continuer"
        continueButton.setOnClickListener(v -> {
            // Créer un Intent pour démarrer NutritionActivity
            Intent intent = new Intent(ExercicesEtirementsActivity.this, NutritionActivityM.class);
            startActivity(intent);
        });
// Bouton de retour
        backButton.setOnClickListener(v -> {
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            finish();
        });

        // Configurer les autres actions pour les cartes d'étirement
        cardEtirementCou.setOnClickListener(v -> animateCardClick(cardEtirementCou));
        cardEtirementDos.setOnClickListener(v -> animateCardClick(cardEtirementDos));
        cardEtirementJambes.setOnClickListener(v -> animateCardClick(cardEtirementJambes));
        cardEtirementBras.setOnClickListener(v -> animateCardClick(cardEtirementBras));
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
