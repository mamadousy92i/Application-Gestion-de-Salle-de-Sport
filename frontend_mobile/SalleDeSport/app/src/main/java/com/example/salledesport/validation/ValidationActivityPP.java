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

import com.example.salledesport.exercise.ExerciseActivityPP;
import com.example.salledesport.R;
import com.example.salledesport.utils.BaseActivity;

public class ValidationActivityPP extends BaseActivity {

    // Views selon le nouveau layout modernisé
    private FrameLayout backButton;
    private ImageView objectifIcon;
    private TextView objectifTitle, encouragementMessage, benefit1, benefit2, benefit3;
    private MaterialCardView iconContainer; // MaterialCardView dans le nouveau layout
    private LinearLayout continueButton;    // LinearLayout à l'intérieur de la MaterialCardView
    private Chip categoryChip;
    private String objectifChoisi = "perte_poids";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validationpp);

        // Configurer le comportement des barres système (hérité de BaseActivity)
        setupSystemBarsBehavior();

        // Masquer les barres système pour un effet immersif
        hideSystemUI();

        // Récupérer l'objectif choisi
        String objectifFromIntent = getIntent().getStringExtra("objectif");
        if (objectifFromIntent != null) {
            objectifChoisi = objectifFromIntent;
        }

        // Initialiser les vues
        initViews();

        // Configurer le contenu pour perte de poids
        setupPertePoidsContent();

        // Configurer les animations
        setupAnimations();

        // Configurer les listeners
        setupListeners();
    }

    private void initViews() {
        // Views selon le nouveau layout modernisé
        backButton = findViewById(R.id.backButton);
        objectifIcon = findViewById(R.id.objectifIcon);
        objectifTitle = findViewById(R.id.objectifTitle);
        encouragementMessage = findViewById(R.id.encouragementMessage);
        benefit1 = findViewById(R.id.benefit1);
        benefit2 = findViewById(R.id.benefit2);
        benefit3 = findViewById(R.id.benefit3);
        continueButton = findViewById(R.id.continueButton); // LinearLayout à l'intérieur de la MaterialCardView
        iconContainer = findViewById(R.id.iconContainer);   // MaterialCardView dans le nouveau layout
        categoryChip = findViewById(R.id.categoryChip);

        // Vérifications de sécurité avec logs informatifs
        if (backButton == null) {
            android.util.Log.e("ValidationActivityPP", "backButton not found in layout");
        }
        if (continueButton == null) {
            android.util.Log.e("ValidationActivityPP", "continueButton not found in layout");
        }
        if (iconContainer == null) {
            android.util.Log.e("ValidationActivityPP", "iconContainer not found in layout");
        }
        if (categoryChip == null) {
            android.util.Log.w("ValidationActivityPP", "categoryChip not found in layout");
        }
    }

    private void setupPertePoidsContent() {
        // Configuration spécifique pour perte de poids
        if (objectifTitle != null) {
            objectifTitle.setText("PERTE DE POIDS");
        }

        if (objectifIcon != null) {
            objectifIcon.setImageResource(R.drawable.calories);
        }

        // Configurer les couleurs pour le thème rouge (fat burner)
        if (iconContainer != null) {
            iconContainer.setCardBackgroundColor(getColor(R.color.accent_red));
        }

        // Configurer le chip de catégorie
        if (categoryChip != null) {
            categoryChip.setText("🔥 FAT BURNER");
            categoryChip.setChipBackgroundColorResource(R.color.accent_red);
        }

        if (encouragementMessage != null) {
            encouragementMessage.setText("Préparez-vous à transformer votre silhouette et à retrouver confiance en vous !");
        }

        // Bénéfices spécifiques à la perte de poids
        if (benefit1 != null) {
            benefit1.setText("Exercices cardio intensifs optimisés");
        }
        if (benefit2 != null) {
            benefit2.setText("Plan nutritionnel pour la perte");
        }
        if (benefit3 != null) {
            benefit3.setText("Suivi de transformation corporelle");
        }
    }

    private void setupAnimations() {
        // Animations futuristes modernes
        Animation scaleIn = AnimationUtils.loadAnimation(this, R.anim.scale_in);
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in_slow);
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
                try {
                    iconContainer.startAnimation(scaleIn);
                } catch (Exception e) {
                    android.util.Log.w("ValidationActivityPP", "Scale animation not found");
                }
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
                try {
                    benefitsContainer.startAnimation(slideUp);
                } catch (Exception e) {
                    android.util.Log.w("ValidationActivityPP", "Slide up animation not found");
                }
                benefitsContainer.setVisibility(View.VISIBLE);
            }, 1000);
        }

        if (continueButton != null) {
            continueButton.postDelayed(() -> {
                try {
                    continueButton.startAnimation(slideUp);
                } catch (Exception e) {
                    android.util.Log.w("ValidationActivityPP", "Slide up animation not found");
                }
                continueButton.setVisibility(View.VISIBLE);
            }, 1200);
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

                // Navigation vers ExerciseActivity avec objectif
                Intent intent = new Intent(ValidationActivityPP.this, ExerciseActivityPP.class);
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
                android.util.Log.w("ValidationActivityPP", "Pulse animation not found");
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