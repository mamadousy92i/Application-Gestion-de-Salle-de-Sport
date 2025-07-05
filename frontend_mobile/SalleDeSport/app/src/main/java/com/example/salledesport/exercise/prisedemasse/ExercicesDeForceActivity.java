package com.example.salledesport.exercise.prisedemasse;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import com.google.android.material.card.MaterialCardView;

import com.example.salledesport.R;
import com.example.salledesport.exercise.prisedemasse.nutrition.NutritionActivityPM;
import com.example.salledesport.utils.BaseActivity;

public class ExercicesDeForceActivity extends BaseActivity {

    // Views selon le nouveau layout futuriste
    private FrameLayout backButton;
    private MaterialCardView cardDeveloppeCouche, cardPompes, cardSquat, cardSouleveDeTerre, cardTraction;
    private MaterialCardView continueButton;
    private String objectifChoisi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercices_de_force);

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

        // Configurer les animations d'entrée
        setupAnimations();

        // Configurer les listeners
        setupListeners();
    }

    private void initViews() {
        // Views du nouveau layout futuriste
        backButton = findViewById(R.id.btnBack);
        cardDeveloppeCouche = findViewById(R.id.cardDeveloppeCouche);
        cardPompes = findViewById(R.id.cardPompes);
        cardSquat = findViewById(R.id.cardSquat);
        cardSouleveDeTerre = findViewById(R.id.cardSouleveDeTerre);
        cardTraction = findViewById(R.id.cardTraction);
        continueButton = findViewById(R.id.continueButton);

        // Vérifications de sécurité avec logs informatifs
        if (backButton == null) {
            Log.e("ExercicesDeForceActivity", "backButton (btnBack) not found in layout");
        }
        if (continueButton == null) {
            Log.e("ExercicesDeForceActivity", "continueButton not found in layout");
        }
    }

    private void setupAnimations() {
        // Animations futuristes modernes
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        Animation scaleIn = AnimationUtils.loadAnimation(this, R.anim.scale_in);

        // Masquer les cartes initialement
        if (cardDeveloppeCouche != null) cardDeveloppeCouche.setAlpha(0f);
        if (cardPompes != null) cardPompes.setAlpha(0f);
        if (cardSquat != null) cardSquat.setAlpha(0f);
        if (cardSouleveDeTerre != null) cardSouleveDeTerre.setAlpha(0f);
        if (cardTraction != null) cardTraction.setAlpha(0f);
        if (continueButton != null) continueButton.setAlpha(0f);

        // Animation séquentielle moderne avec fade et translation
        if (cardDeveloppeCouche != null) {
            cardDeveloppeCouche.postDelayed(() -> {
                cardDeveloppeCouche.animate()
                        .alpha(1f)
                        .translationY(0f)
                        .setDuration(600)
                        .start();
            }, 200);
        }

        if (cardPompes != null) {
            cardPompes.postDelayed(() -> {
                cardPompes.animate()
                        .alpha(1f)
                        .translationY(0f)
                        .setDuration(600)
                        .start();
            }, 300);
        }

        if (cardSquat != null) {
            cardSquat.postDelayed(() -> {
                cardSquat.animate()
                        .alpha(1f)
                        .translationY(0f)
                        .setDuration(600)
                        .start();
            }, 400);
        }

        if (cardSouleveDeTerre != null) {
            cardSouleveDeTerre.postDelayed(() -> {
                cardSouleveDeTerre.animate()
                        .alpha(1f)
                        .translationY(0f)
                        .setDuration(600)
                        .start();
            }, 500);
        }

        if (cardTraction != null) {
            cardTraction.postDelayed(() -> {
                cardTraction.animate()
                        .alpha(1f)
                        .translationY(0f)
                        .setDuration(600)
                        .start();
            }, 600);
        }

        if (continueButton != null) {
            continueButton.postDelayed(() -> {
                continueButton.animate()
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

        // Bouton continuer futuriste
        if (continueButton != null) {
            continueButton.setOnClickListener(v -> {
                animateFuturisticButtonClick(continueButton);

                // Navigation vers NutritionActivity avec objectif
                Intent intent = new Intent(ExercicesDeForceActivity.this, NutritionActivityPM.class);
                intent.putExtra("objectif", objectifChoisi);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            });
        }

        // Card Développé Couché
        if (cardDeveloppeCouche != null) {
            cardDeveloppeCouche.setOnClickListener(v -> {
                animateCardClick(cardDeveloppeCouche);
                // TODO: Intent vers les détails de l'exercice Développé Couché
                // Intent intent = new Intent(ExercicesDeForceActivity.this, DeveloppeCoucheDetailActivity.class);
                // intent.putExtra("objectif", objectifChoisi);
                // startActivity(intent);
                Log.d("ExercicesDeForceActivity", "Développé Couché clicked");
            });
        }

        // Card Pompes
        if (cardPompes != null) {
            cardPompes.setOnClickListener(v -> {
                animateCardClick(cardPompes);
                // TODO: Intent vers les détails de l'exercice Pompes
                Log.d("ExercicesDeForceActivity", "Pompes clicked");
            });
        }

        // Card Squat
        if (cardSquat != null) {
            cardSquat.setOnClickListener(v -> {
                animateCardClick(cardSquat);
                // TODO: Intent vers les détails de l'exercice Squat
                Log.d("ExercicesDeForceActivity", "Squat clicked");
            });
        }

        // Card Soulevé de Terre
        if (cardSouleveDeTerre != null) {
            cardSouleveDeTerre.setOnClickListener(v -> {
                animateCardClick(cardSouleveDeTerre);
                // TODO: Intent vers les détails de l'exercice Soulevé de Terre
                Log.d("ExercicesDeForceActivity", "Soulevé de Terre clicked");
            });
        }

        // Card Traction
        if (cardTraction != null) {
            cardTraction.setOnClickListener(v -> {
                animateCardClick(cardTraction);
                // TODO: Intent vers les détails de l'exercice Traction
                Log.d("ExercicesDeForceActivity", "Traction clicked");
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
                                .setDuration(150)
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

            // Effet de brillance avec animation pulse
            try {
                Animation pulse = AnimationUtils.loadAnimation(this, R.anim.pulse);
                card.startAnimation(pulse);
            } catch (Exception e) {
                Log.w("ExercicesDeForceActivity", "Pulse animation not found");
            }
        }
    }

    private void animateFuturisticButtonClick(MaterialCardView button) {
        // Animation futuriste pour le bouton continuer
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
        if (cardDeveloppeCouche != null) cardDeveloppeCouche.clearAnimation();
        if (cardPompes != null) cardPompes.clearAnimation();
        if (cardSquat != null) cardSquat.clearAnimation();
        if (cardSouleveDeTerre != null) cardSouleveDeTerre.clearAnimation();
        if (cardTraction != null) cardTraction.clearAnimation();
        if (continueButton != null) continueButton.clearAnimation();
        if (backButton != null) backButton.clearAnimation();
    }
}