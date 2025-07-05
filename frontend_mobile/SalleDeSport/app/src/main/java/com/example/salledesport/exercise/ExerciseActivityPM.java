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
import com.example.salledesport.exercise.prisedemasse.ExercicesDeForceActivity;
import com.example.salledesport.exercise.prisedemasse.ExercicesMusculairesActivity;
import com.example.salledesport.exercise.prisedemasse.nutrition.NutritionActivityPM;
import com.example.salledesport.utils.BaseActivity;

public class ExerciseActivityPM extends BaseActivity {

    // Views selon le nouveau layout
    private FrameLayout backButton;
    private TextView titleText, motivationTitle, motivationSubtitle;
    private ImageView motivationIcon;
    private MaterialCardView cardExerciceForce, cardExerciceMusculaire, cardNutrition, progressButton;
    private String objectifChoisi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_pm);

        // Configurer le comportement des barres système (hérité de BaseActivity)
        setupSystemBarsBehavior();

        // Masquer les barres système pour un effet immersif
        hideSystemUI();

        // Récupérer l'objectif choisi
        objectifChoisi = getIntent().getStringExtra("objectif");
        if (objectifChoisi == null) {
            objectifChoisi = "prise_masse"; // Valeur par défaut
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
        cardExerciceForce = findViewById(R.id.cardExerciceForce);
        cardExerciceMusculaire = findViewById(R.id.cardExerciceMusculaire);
        cardNutrition = findViewById(R.id.cardNutrition);
        progressButton = findViewById(R.id.progressButton);
    }

    private void setupContentForObjectif() {
        switch (objectifChoisi) {
            case "prise_masse":
                setupPriseMasseContent();
                break;
            case "perte_poids":
                setupPertePoidsContent();
                break;
            case "maintien":
                setupMaintienContent();
                break;
            default:
                setupPriseMasseContent(); // Par défaut
                break;
        }
    }

    private void setupPriseMasseContent() {
        // Configuration pour prise de masse
        if (titleText != null) titleText.setText("PRISE DE MASSE");
        if (motivationIcon != null) motivationIcon.setImageResource(R.drawable.powerlifting);
        if (motivationTitle != null) motivationTitle.setText("Renforcez votre masse musculaire");
        if (motivationSubtitle != null) motivationSubtitle.setText("Augmentez votre force avec ces exercices ciblés");
    }

    private void setupPertePoidsContent() {
        // Configuration pour perte de poids
        if (titleText != null) titleText.setText("PERTE DE POIDS");
        if (motivationIcon != null) motivationIcon.setImageResource(R.drawable.ic_flame_advanced);
        if (motivationTitle != null) motivationTitle.setText("Brûlez les graisses efficacement");
        if (motivationSubtitle != null) motivationSubtitle.setText("Cardio intensif et exercices métaboliques");
    }

    private void setupMaintienContent() {
        // Configuration pour maintien
        if (titleText != null) titleText.setText("MAINTIEN FORME");
        if (motivationIcon != null) motivationIcon.setImageResource(R.drawable.ic_balance_advanced);
        if (motivationTitle != null) motivationTitle.setText("Maintenez votre forme actuelle");
        if (motivationSubtitle != null) motivationSubtitle.setText("Équilibre parfait entre santé et bien-être");
    }

    private void setupAnimations() {
        // Animations d'entrée futuristes
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in_slow);
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        Animation scaleIn = AnimationUtils.loadAnimation(this, R.anim.scale_in);

        // Masquer les cartes initialement
        if (cardExerciceForce != null) cardExerciceForce.setAlpha(0f);
        if (cardExerciceMusculaire != null) cardExerciceMusculaire.setAlpha(0f);
        if (cardNutrition != null) cardNutrition.setAlpha(0f);
        if (progressButton != null) progressButton.setAlpha(0f);

        // Animation séquentielle des cartes
        if (cardExerciceForce != null) {
            cardExerciceForce.postDelayed(() -> {
                cardExerciceForce.animate()
                        .alpha(1f)
                        .translationY(0f)
                        .setDuration(600)
                        .start();
            }, 300);
        }

        if (cardExerciceMusculaire != null) {
            cardExerciceMusculaire.postDelayed(() -> {
                cardExerciceMusculaire.animate()
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

        // Carte exercices de force
        if (cardExerciceForce != null) {
            cardExerciceForce.setOnClickListener(v -> {
                animateCardClick(cardExerciceForce);
                Intent intent = new Intent(ExerciseActivityPM.this, ExercicesDeForceActivity.class);
                intent.putExtra("objectif", objectifChoisi);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            });
        }

        // Carte exercices musculaires
        if (cardExerciceMusculaire != null) {
            cardExerciceMusculaire.setOnClickListener(v -> {
                animateCardClick(cardExerciceMusculaire);
                Intent intent = new Intent(ExerciseActivityPM.this, ExercicesMusculairesActivity.class);
                intent.putExtra("objectif", objectifChoisi);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            });
        }

        // Carte nutrition
        if (cardNutrition != null) {
            cardNutrition.setOnClickListener(v -> {
                animateCardClick(cardNutrition);
                Intent intent = new Intent(ExerciseActivityPM.this, NutritionActivityPM.class);
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
                // Intent intent = new Intent(ExerciseActivityPM.this, ProgressActivity.class);
                // startActivity(intent);
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
                // Si l'animation n'existe pas, continuer sans erreur
                android.util.Log.w("ExerciseActivityPM", "Pulse animation not found");
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
        if (cardExerciceForce != null) cardExerciceForce.clearAnimation();
        if (cardExerciceMusculaire != null) cardExerciceMusculaire.clearAnimation();
        if (cardNutrition != null) cardNutrition.clearAnimation();
        if (progressButton != null) progressButton.clearAnimation();
        if (backButton != null) backButton.clearAnimation();
    }
}