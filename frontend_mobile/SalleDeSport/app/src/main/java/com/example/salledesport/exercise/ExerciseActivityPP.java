package com.example.salledesport.exercise;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.material.card.MaterialCardView;

import com.example.salledesport.R;
import com.example.salledesport.exercise.pertedepoids.ExercicesCardioActivity;
import com.example.salledesport.exercise.pertedepoids.ExercicesCircuitActivity;
import com.example.salledesport.exercise.pertedepoids.nutrition.NutritionActivityPP;
import com.example.salledesport.utils.BaseActivity;

public class ExerciseActivityPP extends BaseActivity {

    // Views selon le nouveau layout futuriste
    private FrameLayout backButton;
    private TextView titleText, motivationTitle, motivationSubtitle;
    private ImageView motivationIcon;
    private MaterialCardView cardExerciceCardio, cardExerciceCircuit, cardNutrition, progressButton;
    private String objectifChoisi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_pp);

        // Configurer le comportement des barres système (hérité de BaseActivity)
        setupSystemBarsBehavior();

        // Masquer les barres système pour un effet immersif
        hideSystemUI();

        // Récupérer l'objectif choisi
        objectifChoisi = getIntent().getStringExtra("objectif");
        if (objectifChoisi == null) {
            objectifChoisi = "perte_poids"; // Valeur par défaut
        }

        // Initialiser les vues
        initViews();

        // Configurer le contenu selon l'objectif
        setupContentForObjectif();

        // Configurer les animations
        setupAnimations();

        // Configurer les listeners
        setupListeners();
    }

    private void initViews() {
        // Views du nouveau layout futuriste
        backButton = findViewById(R.id.backButton);
        titleText = findViewById(R.id.titleText);
        motivationTitle = findViewById(R.id.motivationTitle);
        motivationSubtitle = findViewById(R.id.motivationSubtitle);
        motivationIcon = findViewById(R.id.motivationIcon);
        cardExerciceCardio = findViewById(R.id.cardExerciceCardio);
        cardExerciceCircuit = findViewById(R.id.cardExerciceCircuit);
        cardNutrition = findViewById(R.id.cardNutrition);
        progressButton = findViewById(R.id.progressButton);

        // Vérifications de sécurité avec logs informatifs
        if (backButton == null) {
            android.util.Log.e("ExerciseActivityPP", "backButton not found in layout");
        }
        if (progressButton == null) {
            android.util.Log.w("ExerciseActivityPP", "progressButton not found in layout");
        }
    }

    private void setupContentForObjectif() {
        // Configuration spécifique pour perte de poids
        if (titleText != null) titleText.setText("PERTE DE POIDS");
        if (motivationIcon != null) motivationIcon.setImageResource(R.drawable.scale_weight);
        if (motivationTitle != null) motivationTitle.setText("La perte de poids n'a jamais été aussi simple !");
        if (motivationSubtitle != null) motivationSubtitle.setText("Définissez vos exercices à votre convenance");
    }

    private void setupAnimations() {
        // Animations d'entrée futuristes
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in_slow);
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        Animation scaleIn = AnimationUtils.loadAnimation(this, R.anim.scale_in);

        // Masquer les cartes initialement
        if (cardExerciceCardio != null) cardExerciceCardio.setAlpha(0f);
        if (cardExerciceCircuit != null) cardExerciceCircuit.setAlpha(0f);
        if (cardNutrition != null) cardNutrition.setAlpha(0f);
        if (progressButton != null) progressButton.setAlpha(0f);

        // Animation séquentielle des cartes
        if (cardExerciceCardio != null) {
            cardExerciceCardio.postDelayed(() -> {
                cardExerciceCardio.animate()
                        .alpha(1f)
                        .translationY(0f)
                        .setDuration(600)
                        .start();
            }, 300);
        }

        if (cardExerciceCircuit != null) {
            cardExerciceCircuit.postDelayed(() -> {
                cardExerciceCircuit.animate()
                        .alpha(1f)
                        .translationY(0f)
                        .setDuration(600)
                        .start();
            }, 450);
        }

        if (cardNutrition != null) {
            cardNutrition.postDelayed(() -> {
                cardNutrition.animate()
                        .alpha(1f)
                        .translationY(0f)
                        .setDuration(600)
                        .start();
            }, 600);
        }

        if (progressButton != null) {
            progressButton.postDelayed(() -> {
                progressButton.animate()
                        .alpha(1f)
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(500)
                        .start();
            }, 800);
        }
    }

    private void setupListeners() {
        // Bouton de retour holographique
        if (backButton != null) {
            backButton.setOnClickListener(v -> {
                animateHolographicClick(backButton);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            });
        }

        // Carte exercices cardio
        if (cardExerciceCardio != null) {
            cardExerciceCardio.setOnClickListener(v -> {
                animateCardClick(cardExerciceCardio);
                Intent intent = new Intent(ExerciseActivityPP.this, ExercicesCardioActivity.class);
                intent.putExtra("objectif", objectifChoisi);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            });
        }

        // Carte exercices en circuit
        if (cardExerciceCircuit != null) {
            cardExerciceCircuit.setOnClickListener(v -> {
                animateCardClick(cardExerciceCircuit);
                Intent intent = new Intent(ExerciseActivityPP.this, ExercicesCircuitActivity.class);
                intent.putExtra("objectif", objectifChoisi);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            });
        }

        // Carte nutrition
        if (cardNutrition != null) {
            cardNutrition.setOnClickListener(v -> {
                animateCardClick(cardNutrition);
                Intent intent = new Intent(ExerciseActivityPP.this, NutritionActivityPP.class);
                intent.putExtra("objectif", objectifChoisi);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            });
        }

        // Bouton de progression
        if (progressButton != null) {
            progressButton.setOnClickListener(v -> {
                animateFuturisticButtonClick(progressButton);
                // TODO: Implémenter la navigation vers l'activité de progression
                // Intent intent = new Intent(ExerciseActivityPP.this, ProgressActivity.class);
                // startActivity(intent);
                android.util.Log.d("ExerciseActivityPP", "Progress button clicked");
            });
        }
    }

    private void animateHolographicClick(View button) {
        // Animation holographique sophistiquée
        if (button != null) {
            button.animate()
                    .scaleX(0.95f)
                    .scaleY(0.95f)
                    .setDuration(100)
                    .withEndAction(() -> {
                        button.animate()
                                .scaleX(1f)
                                .scaleY(1f)
                                .setDuration(100)
                                .start();
                    })
                    .start();
        }
    }

    private void animateCardClick(MaterialCardView card) {
        // Animation pour les cartes d'exercices
        if (card != null) {
            card.animate()
                    .scaleX(0.97f)
                    .scaleY(0.97f)
                    .setDuration(100)
                    .withEndAction(() -> {
                        card.animate()
                                .scaleX(1.02f)
                                .scaleY(1.02f)
                                .setDuration(100)
                                .withEndAction(() -> {
                                    card.animate()
                                            .scaleX(1f)
                                            .scaleY(1f)
                                            .setDuration(100)
                                            .start();
                                })
                                .start();
                    })
                    .start();

            // Effet de brillance
            try {
                Animation pulse = AnimationUtils.loadAnimation(this, R.anim.pulse);
                card.startAnimation(pulse);
            } catch (Exception e) {
                android.util.Log.w("ExerciseActivityPP", "Pulse animation not found");
            }
        }
    }

    private void animateFuturisticButtonClick(MaterialCardView button) {
        // Animation futuriste pour le bouton de progression
        if (button != null) {
            button.animate()
                    .scaleX(0.95f)
                    .scaleY(0.95f)
                    .setDuration(150)
                    .withEndAction(() -> {
                        button.animate()
                                .scaleX(1.05f)
                                .scaleY(1.05f)
                                .setDuration(200)
                                .withEndAction(() -> {
                                    button.animate()
                                            .scaleX(1f)
                                            .scaleY(1f)
                                            .setDuration(150)
                                            .start();
                                })
                                .start();
                    })
                    .start();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Réactiver le mode immersif au retour sur l'activité
        hideSystemUI();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            // Masquer les barres système quand l'activité regagne le focus
            hideSystemUI();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Nettoyer les animations en cours
        clearAnimations();
    }

    private void clearAnimations() {
        // Arrêter toutes les animations en cours
        if (cardExerciceCardio != null) cardExerciceCardio.clearAnimation();
        if (cardExerciceCircuit != null) cardExerciceCircuit.clearAnimation();
        if (cardNutrition != null) cardNutrition.clearAnimation();
        if (progressButton != null) progressButton.clearAnimation();
        if (backButton != null) backButton.clearAnimation();
    }
}