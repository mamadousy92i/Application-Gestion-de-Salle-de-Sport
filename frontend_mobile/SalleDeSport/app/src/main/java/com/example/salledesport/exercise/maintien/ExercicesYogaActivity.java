package com.example.salledesport.exercise.maintien;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;


import com.google.android.material.card.MaterialCardView;
import com.example.salledesport.R;
import com.example.salledesport.exercise.maintien.nutrition.NutritionActivityM;
import com.example.salledesport.utils.BaseActivity;

public class ExercicesYogaActivity extends BaseActivity {

    private FrameLayout backButton;
    private MaterialCardView cardSalutation, cardGuerrier, cardEnfant, cardCobra, cardArbre, cardTorsion;
    private MaterialCardView continueButton; // Utilisation de MaterialCardView pour le bouton "Continuer"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercices_yoga);

        // Configuration des barres système héritée de BaseActivity
        setupSystemBarsBehavior();

        // Initialiser les vues
        initViews();

        // Vérifier si le backButton n'est pas null
        if (backButton != null) {
            setupBackButtonListener();
        } else {
            Log.e("ExercicesYogaActivity", "backButton is null");
        }

        // Configurer les animations d'entrée
        setupAnimations();

        // Configurer les listeners pour les autres vues
        setupListeners();
    }

    private void initViews() {
        backButton = findViewById(R.id.btnBack);
        cardSalutation = findViewById(R.id.cardSalutation);
        cardGuerrier = findViewById(R.id.cardGuerrier);
        cardEnfant = findViewById(R.id.cardEnfant);
        cardCobra = findViewById(R.id.cardCobra);
        cardArbre = findViewById(R.id.cardArbre);
        cardTorsion = findViewById(R.id.cardTorsion);

        // Initialisation du bouton "Continuer" (maintenant MaterialCardView)
        continueButton = findViewById(R.id.continueButton);
    }

    private void setupBackButtonListener() {
        backButton.setOnClickListener(v -> {
            // Effet de pulsation avant la transition
            Animation pulse = AnimationUtils.loadAnimation(this, R.anim.pulse);
            backButton.startAnimation(pulse);

            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            finish();
        });
    }

    private void setupAnimations() {
        // Animation de fade in pour les cartes
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation slideFromLeft = AnimationUtils.loadAnimation(this, R.anim.slide_from_left);
        Animation slideFromRight = AnimationUtils.loadAnimation(this, R.anim.slide_from_right);

        // Appliquer les animations avec des délais progressifs
        if (cardSalutation != null) {
            cardSalutation.postDelayed(() -> {
                cardSalutation.startAnimation(slideFromLeft);
                cardSalutation.setVisibility(View.VISIBLE);
            }, 200);
        }

        if (cardGuerrier != null) {
            cardGuerrier.postDelayed(() -> {
                cardGuerrier.startAnimation(slideFromRight);
                cardGuerrier.setVisibility(View.VISIBLE);
            }, 300);
        }

        if (cardEnfant != null) {
            cardEnfant.postDelayed(() -> {
                cardEnfant.startAnimation(slideFromLeft);
                cardEnfant.setVisibility(View.VISIBLE);
            }, 400);
        }

        if (cardCobra != null) {
            cardCobra.postDelayed(() -> {
                cardCobra.startAnimation(slideFromRight);
                cardCobra.setVisibility(View.VISIBLE);
            }, 500);
        }

        if (cardArbre != null) {
            cardArbre.postDelayed(() -> {
                cardArbre.startAnimation(slideFromLeft);
                cardArbre.setVisibility(View.VISIBLE);
            }, 600);
        }

        if (cardTorsion != null) {
            cardTorsion.postDelayed(() -> {
                cardTorsion.startAnimation(slideFromRight);
                cardTorsion.setVisibility(View.VISIBLE);
            }, 700);
        }

        // Animation du bouton continuer
        if (continueButton != null) {
            continueButton.postDelayed(() -> {
                Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
                continueButton.startAnimation(slideUp);
                continueButton.setVisibility(View.VISIBLE);
            }, 900);
        }
    }

    private void setupListeners() {
        // Configurer le listener pour le bouton "Continuer"
        if (continueButton != null) {
            continueButton.setOnClickListener(v -> {
                // Animation de clic
                animateCardClick(continueButton);

                // Créer un Intent pour démarrer NutritionActivityM
                Intent intent = new Intent(ExercicesYogaActivity.this, NutritionActivityM.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            });
        }

        // Card Salutation au Soleil
        if (cardSalutation != null) {
            cardSalutation.setOnClickListener(v -> {
                animateCardClick(cardSalutation);
                // Intent vers les détails de l'exercice
                // Intent intent = new Intent(ExercicesYogaActivity.this, ExerciseDetailActivity.class);
                // intent.putExtra("exercise_name", "Salutation au Soleil");
                // startActivity(intent);
            });
        }

        // Card Posture du Guerrier
        if (cardGuerrier != null) {
            cardGuerrier.setOnClickListener(v -> {
                animateCardClick(cardGuerrier);
                // Intent vers les détails de l'exercice
            });
        }

        // Card Posture de l'Enfant
        if (cardEnfant != null) {
            cardEnfant.setOnClickListener(v -> {
                animateCardClick(cardEnfant);
                // Intent vers les détails de l'exercice
            });
        }

        // Card Posture du Cobra
        if (cardCobra != null) {
            cardCobra.setOnClickListener(v -> {
                animateCardClick(cardCobra);
                // Intent vers les détails de l'exercice
            });
        }

        // Card Posture de l'Arbre
        if (cardArbre != null) {
            cardArbre.setOnClickListener(v -> {
                animateCardClick(cardArbre);
                // Intent vers les détails de l'exercice
            });
        }

        // Card Torsion Spinale
        if (cardTorsion != null) {
            cardTorsion.setOnClickListener(v -> {
                animateCardClick(cardTorsion);
                // Intent vers les détails de l'exercice
            });
        }
    }

    private void animateCardClick(MaterialCardView card) {
        // Animation de "pulse" lors du clic
        Animation pulse = AnimationUtils.loadAnimation(this, R.anim.pulse);
        card.startAnimation(pulse);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Masquer les barres système lors de la reprise de l'activité
        hideSystemUI();
    }

    @Override
    public void onBackPressed() {
        // Animation personnalisée lors du retour
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        // Masquer les barres système quand l'utilisateur quitte l'app
        hideSystemUI();
    }
}