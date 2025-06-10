package com.example.salledesport.exercise.prisedemasse;

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
import com.example.salledesport.exercise.prisedemasse.nutrition.NutritionActivityPM;

public class ExercicesDeForceActivity extends AppCompatActivity {

    private ImageButton backButton;
    private CardView cardFlexion, cardPompes, cardSquat, cardSouleveDeTerre, cardTraction;
    private LinearLayout continueButton; // Utilisation de LinearLayout pour le bouton "Continuer"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercices_de_force);

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
            Log.e("ExercicesDeForceActivity", "backButton is null");
        }

        // Configurer les animations d'entrée
        setupAnimations();

        // Configurer les listeners pour les autres vues
        setupListeners();
    }

    private void initViews() {
        backButton = findViewById(R.id.btnBack);
        cardFlexion = findViewById(R.id.cardDeveloppeCouche);
        cardPompes = findViewById(R.id.cardPompes);
        cardSquat = findViewById(R.id.cardSquat);
        cardSouleveDeTerre = findViewById(R.id.cardSouleveDeTerre);
        cardTraction = findViewById(R.id.cardTraction);

        continueButton = findViewById(R.id.continueButton); // Utilisation de LinearLayout pour le bouton "Continuer"

    }

    private void setupAnimations() {
        // Animation de fade in pour les cartes
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation slideFromLeft = AnimationUtils.loadAnimation(this, R.anim.slide_from_left);
        Animation slideFromRight = AnimationUtils.loadAnimation(this, R.anim.slide_from_right);

        // Appliquer les animations avec des délais
        cardFlexion.postDelayed(() -> {
            cardFlexion.startAnimation(slideFromLeft);
            cardFlexion.setVisibility(View.VISIBLE);
        }, 200);

        cardPompes.postDelayed(() -> {
            cardPompes.startAnimation(slideFromRight);
            cardPompes.setVisibility(View.VISIBLE);
        }, 300);

        cardSquat.postDelayed(() -> {
            cardSquat.startAnimation(slideFromLeft);
            cardSquat.setVisibility(View.VISIBLE);
        }, 400);

        cardSouleveDeTerre.postDelayed(() -> {
            cardSouleveDeTerre.startAnimation(slideFromRight);
            cardSouleveDeTerre.setVisibility(View.VISIBLE);
        }, 500);

        cardTraction.postDelayed(() -> {
            cardTraction.startAnimation(slideFromLeft);
            cardTraction.setVisibility(View.VISIBLE);
        }, 600);
    }

    private void setupListeners() {
        // Bouton de retour
        backButton.setOnClickListener(v -> {
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            finish();
        });

        // Configurer le listener pour le LinearLayout "Continuer"
        continueButton.setOnClickListener(v -> {
            // Créer un Intent pour démarrer NutritionActivity
            Intent intent = new Intent(ExercicesDeForceActivity.this, NutritionActivityPM.class);
            startActivity(intent);
        });

        // Card Flexion
        cardFlexion.setOnClickListener(v -> {
            animateCardClick(cardFlexion);
            // Intent vers les détails de l'exercice
        });

        // Card Pompes
        cardPompes.setOnClickListener(v -> {
            animateCardClick(cardPompes);
            // Intent vers les détails de l'exercice
        });

        // Card Squat
        cardSquat.setOnClickListener(v -> {
            animateCardClick(cardSquat);
            // Intent vers les détails de l'exercice
        });

        // Card Soulevé de Terre
        cardSouleveDeTerre.setOnClickListener(v -> {
            animateCardClick(cardSouleveDeTerre);
            // Intent vers les détails de l'exercice
        });

        // Card Traction
        cardTraction.setOnClickListener(v -> {
            animateCardClick(cardTraction);
            // Intent vers les détails de l'exercice
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
