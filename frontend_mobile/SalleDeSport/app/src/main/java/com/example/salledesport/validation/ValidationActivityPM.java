package com.example.salledesport.validation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;

import com.example.salledesport.exercise.ExerciseActivityPM;
import com.example.salledesport.R;
import com.example.salledesport.utils.BaseActivity;

public class ValidationActivityPM extends BaseActivity {

    // IDs selon le layout modernisé
    private FrameLayout backButton;
    private ImageView objectifIcon;
    private TextView objectifTitle, encouragementMessage, benefit1, benefit2, benefit3;
    private LinearLayout continueButton;
    private MaterialCardView iconContainer;
    private Chip categoryChip;
    private String objectifChoisi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validationpm);

        // Configurer le comportement des barres système (hérité de BaseActivity)
        setupSystemBarsBehavior();

        // Masquer les barres système pour un effet immersif
        hideSystemUI();

        // Récupérer l'objectif choisi
        objectifChoisi = getIntent().getStringExtra("objectif");

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
        // IDs du layout modernisé
        backButton = findViewById(R.id.backButton);
        objectifIcon = findViewById(R.id.objectifIcon);
        objectifTitle = findViewById(R.id.objectifTitle);
        encouragementMessage = findViewById(R.id.encouragementMessage);
        benefit1 = findViewById(R.id.benefit1);
        benefit2 = findViewById(R.id.benefit2);
        benefit3 = findViewById(R.id.benefit3);
        continueButton = findViewById(R.id.continueButton);
        iconContainer = findViewById(R.id.iconContainer);

        // Chercher le chip de catégorie avec gestion d'erreur
        categoryChip = findViewById(R.id.categoryChip);

        // Si le chip n'est pas trouvé, créer un log pour debug
        if (categoryChip == null) {
            android.util.Log.w("ValidationActivityPM", "CategoryChip not found in layout");
        }
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
        objectifTitle.setText("PRISE DE MASSE");
        objectifIcon.setImageResource(R.drawable.ic_muscle_advanced);

        // Configurer les couleurs pour le thème orange (muscle)
        if (iconContainer != null) {
            iconContainer.setCardBackgroundColor(getColor(R.color.accent_orange));
        }

        // Configurer le chip de catégorie de manière sécurisée
        updateCategoryChip("● MUSCLE BUILDER", R.color.accent_orange);

        encouragementMessage.setText("Vous êtes sur le point de commencer un parcours incroyable vers votre corps de rêves ! " +
                "Préparez-vous à développer une masse musculaire impressionnante.");

        benefit1.setText("Programme personnalisé et adapté");
        benefit2.setText("Suivi de progression en temps réel");
        benefit3.setText("Résultats garantis avec coaching premium");
    }

    private void setupPertePoidsContent() {
        objectifTitle.setText("PERTE DE POIDS");
        objectifIcon.setImageResource(R.drawable.ic_flame_advanced);

        // Configurer les couleurs pour le thème rouge (fat burner)
        if (iconContainer != null) {
            iconContainer.setCardBackgroundColor(getColor(R.color.accent_red));
        }

        // Configurer le chip de catégorie de manière sécurisée
        updateCategoryChip("🔥 FAT BURNER", R.color.accent_red);

        encouragementMessage.setText("Vous allez brûler les graisses efficacement et retrouver " +
                "votre silhouette idéale ! La motivation est la clé du succès.");

        benefit1.setText("Exercices cardio intensifs optimisés");
        benefit2.setText("Plan nutritionnel pour la perte");
        benefit3.setText("Suivi de transformation corporelle");
    }

    private void setupMaintienContent() {
        objectifTitle.setText("MAINTIEN FORME");
        objectifIcon.setImageResource(R.drawable.ic_balance_advanced);

        // Configurer les couleurs pour le thème bleu (balance)
        if (iconContainer != null) {
            iconContainer.setCardBackgroundColor(getColor(R.color.accent_blue));
        }

        // Configurer le chip de catégorie de manière sécurisée
        updateCategoryChip("⚖️ BALANCE", R.color.accent_blue);

        encouragementMessage.setText("Parfait pour maintenir votre forme actuelle ! " +
                "Un équilibre parfait entre santé et bien-être vous attend.");

        benefit1.setText("Exercices d'entretien équilibrés");
        benefit2.setText("Maintien du poids idéal");
        benefit3.setText("Routine stable et durable");
    }

    // Méthode pour gérer le chip de catégorie de manière sécurisée
    private void updateCategoryChip(String text, int backgroundColorRes) {
        if (categoryChip != null) {
            categoryChip.setText(text);
            categoryChip.setChipBackgroundColorResource(backgroundColorRes);
        } else {
            android.util.Log.w("ValidationActivityPM", "Cannot update category chip - view not found");
        }
    }

    private void setupAnimations() {
        // Animations modernes et fluides
        Animation scaleIn = AnimationUtils.loadAnimation(this, R.anim.scale_in);
        Animation fadeInSlow = AnimationUtils.loadAnimation(this, R.anim.fade_in_slow);
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);

        // Masquer les éléments initialement
        if (iconContainer != null) iconContainer.setAlpha(0f);
        if (objectifTitle != null) objectifTitle.setAlpha(0f);
        if (categoryChip != null) categoryChip.setAlpha(0f);

        // Animation séquentielle moderne
        if (iconContainer != null) {
            iconContainer.postDelayed(() -> {
                iconContainer.animate()
                        .alpha(1f)
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(600)
                        .start();
                iconContainer.startAnimation(scaleIn);
            }, 200);
        }

        if (objectifTitle != null) {
            objectifTitle.postDelayed(() -> {
                objectifTitle.animate()
                        .alpha(1f)
                        .setDuration(500)
                        .start();
            }, 600);
        }

        if (categoryChip != null) {
            categoryChip.postDelayed(() -> {
                categoryChip.animate()
                        .alpha(1f)
                        .setDuration(400)
                        .start();
            }, 800);
        }

        LinearLayout benefitsContainer = findViewById(R.id.benefitsContainer);
        if (benefitsContainer != null) {
            benefitsContainer.postDelayed(() -> {
                benefitsContainer.startAnimation(slideUp);
                benefitsContainer.setVisibility(View.VISIBLE);
            }, 1000);
        }

        if (continueButton != null) {
            continueButton.postDelayed(() -> {
                continueButton.startAnimation(slideUp);
                continueButton.setVisibility(View.VISIBLE);
            }, 1200);
        }
    }

    private void setupListeners() {
        // Bouton de retour holographique
        if (backButton != null) {
            backButton.setOnClickListener(v -> {
                animateHolographicClick(backButton);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            });
        }

        // Bouton continuer futuriste
        if (continueButton != null) {
            continueButton.setOnClickListener(v -> {
                animateFuturisticButtonClick(continueButton);

                // Aller à l'activité suivante (choix des exercices/zones)
                Intent intent = new Intent(ValidationActivityPM.this, ExerciseActivityPM.class);
                intent.putExtra("objectif", objectifChoisi);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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

    private void animateFuturisticButtonClick(LinearLayout button) {
        // Animation futuriste pour le bouton principal
        if (button != null) {
            button.animate()
                    .scaleX(0.97f)
                    .scaleY(0.97f)
                    .setDuration(100)
                    .withEndAction(() -> {
                        button.animate()
                                .scaleX(1.02f)
                                .scaleY(1.02f)
                                .setDuration(150)
                                .withEndAction(() -> {
                                    button.animate()
                                            .scaleX(1f)
                                            .scaleY(1f)
                                            .setDuration(100)
                                            .start();
                                })
                                .start();
                    })
                    .start();

            // Effet de glow avec animation pulse
            try {
                Animation pulse = AnimationUtils.loadAnimation(this, R.anim.pulse);
                button.startAnimation(pulse);
            } catch (Exception e) {
                // Si l'animation n'existe pas, continuer sans erreur
                android.util.Log.w("ValidationActivityPM", "Pulse animation not found");
            }
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
        if (iconContainer != null) iconContainer.clearAnimation();
        if (objectifTitle != null) objectifTitle.clearAnimation();
        if (continueButton != null) continueButton.clearAnimation();
        if (backButton != null) backButton.clearAnimation();
        if (categoryChip != null) categoryChip.clearAnimation();
    }
}