package com.example.salledesport.exercise;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.example.salledesport.R;
import com.example.salledesport.exercise.maintien.ExercicesEtirementsActivity;
import com.example.salledesport.exercise.maintien.ExercicesYogaActivity;
import com.example.salledesport.exercise.maintien.nutrition.NutritionActivityM;
import com.example.salledesport.utils.BaseActivity;

public class ExerciseActivityM extends BaseActivity {

    private FrameLayout backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_m);

        // Configuration des barres système héritée de BaseActivity
        setupSystemBarsBehavior();

        // Initialiser les vues
        initViews();

        // Configurer les listeners
        setupListeners();

        // Ajouter des animations d'entrée
        setupAnimations();
    }

    private void initViews() {
        // Bouton de retour (maintenant FrameLayout avec effet holographique)
        backButton = findViewById(R.id.backButton);
    }

    private void setupListeners() {
        // Listener pour le bouton retour avec effet de pulsation
        backButton.setOnClickListener(v -> {
            // Effet de pulsation avant la transition
            Animation pulse = AnimationUtils.loadAnimation(this, R.anim.pulse);
            backButton.startAnimation(pulse);

            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });

        // Listener pour les cartes d'exercices avec animations
        findViewById(R.id.cardExerciceYoga).setOnClickListener(v -> {
            // Animation de clic
            animateCardClick(v);

            // Lancer l'activité des exercices de yoga
            Intent intent = new Intent(ExerciseActivityM.this, ExercicesYogaActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        findViewById(R.id.cardExerciceStretching).setOnClickListener(v -> {
            // Animation de clic
            animateCardClick(v);

            // Lancer l'activité des exercices d'étirement
            Intent intent = new Intent(ExerciseActivityM.this, ExercicesEtirementsActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        findViewById(R.id.cardNutrition).setOnClickListener(v -> {
            // Animation de clic
            animateCardClick(v);

            // Lancer l'activité des conseils nutritionnels
            Intent intent = new Intent(ExerciseActivityM.this, NutritionActivityM.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        // Listener pour le bouton de progression
        View progressButton = findViewById(R.id.progressButton);
        if (progressButton != null) {
            progressButton.setOnClickListener(v -> {
                // Animation de clic
                animateCardClick(v);

                // Ici vous pouvez ajouter la navigation vers une activité de progression
                // Intent intent = new Intent(ExerciseActivityM.this, ProgressActivityM.class);
                // startActivity(intent);
            });
        }
    }

    private void setupAnimations() {
        // Animation d'apparition séquentielle des éléments
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in_slow);
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);

        // Animation du header
        View headerCard = findViewById(R.id.headerCard);
        if (headerCard != null) {
            headerCard.postDelayed(() -> {
                headerCard.startAnimation(fadeIn);
                headerCard.setVisibility(View.VISIBLE);
            }, 100);
        }

        // Animation de la card de motivation
        View motivationCard = findViewById(R.id.motivationCard);
        if (motivationCard != null) {
            motivationCard.postDelayed(() -> {
                motivationCard.startAnimation(slideUp);
                motivationCard.setVisibility(View.VISIBLE);
            }, 300);
        }

        // Animation des cards d'exercices
        View cardYoga = findViewById(R.id.cardExerciceYoga);
        View cardStretching = findViewById(R.id.cardExerciceStretching);
        View cardNutrition = findViewById(R.id.cardNutrition);

        if (cardYoga != null) {
            cardYoga.postDelayed(() -> {
                cardYoga.startAnimation(slideUp);
                cardYoga.setVisibility(View.VISIBLE);
            }, 500);
        }

        if (cardStretching != null) {
            cardStretching.postDelayed(() -> {
                cardStretching.startAnimation(slideUp);
                cardStretching.setVisibility(View.VISIBLE);
            }, 700);
        }

        if (cardNutrition != null) {
            cardNutrition.postDelayed(() -> {
                cardNutrition.startAnimation(slideUp);
                cardNutrition.setVisibility(View.VISIBLE);
            }, 900);
        }
    }

    private void animateCardClick(View view) {
        // Animation de pulsation pour les cartes cliquées
        Animation pulse = AnimationUtils.loadAnimation(this, R.anim.pulse);
        view.startAnimation(pulse);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Masquer les barres système lors de la reprise de l'activité
        hideSystemUI();
    }

    @Override
    public void onBackPressed() {
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