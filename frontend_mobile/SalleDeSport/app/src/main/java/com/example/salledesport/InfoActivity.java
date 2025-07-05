package com.example.salledesport;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import com.google.android.material.card.MaterialCardView;

import com.example.salledesport.utils.BaseActivity;
import com.example.salledesport.validation.ValidationActivityM;
import com.example.salledesport.validation.ValidationActivityPM;
import com.example.salledesport.validation.ValidationActivityPP;

public class InfoActivity extends BaseActivity {

    // IDs corrects selon le layout
    private FrameLayout holoBackButton;
    private MaterialCardView megaCardMuscle, megaCardBurn, megaCardBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        // Configurer le comportement des barres système (hérité de BaseActivity)
        setupSystemBarsBehavior();

        // Masquer les barres système pour un effet immersif
        hideSystemUI();

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
        // Bouton retour holographique
        holoBackButton = findViewById(R.id.holoBackButton);

        // Cartes avec les bons IDs du layout
        megaCardMuscle = findViewById(R.id.megaCardMuscle);   // Prise de masse
        megaCardBurn = findViewById(R.id.megaCardBurn);       // Perte de poids
        megaCardBalance = findViewById(R.id.megaCardBalance); // Maintien
    }

    private void setupAnimations() {
        // Animations simples qui fonctionnent
        Animation slideFromRight = AnimationUtils.loadAnimation(this, R.anim.slide_from_right);
        Animation slideFromLeft = AnimationUtils.loadAnimation(this, R.anim.slide_from_left);
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        // Masquer les cartes initialement
        megaCardMuscle.setAlpha(0f);
        megaCardBurn.setAlpha(0f);
        megaCardBalance.setAlpha(0f);

        // Animation séquentielle
        megaCardMuscle.postDelayed(() -> {
            megaCardMuscle.animate()
                    .alpha(1f)
                    .setDuration(600)
                    .start();
            megaCardMuscle.startAnimation(slideFromLeft);
        }, 200);

        megaCardBurn.postDelayed(() -> {
            megaCardBurn.animate()
                    .alpha(1f)
                    .setDuration(600)
                    .start();
            megaCardBurn.startAnimation(fadeIn);
        }, 500);

        megaCardBalance.postDelayed(() -> {
            megaCardBalance.animate()
                    .alpha(1f)
                    .setDuration(600)
                    .start();
            megaCardBalance.startAnimation(slideFromRight);
        }, 800);
    }

    private void setupListeners() {
        // Bouton retour holographique
        holoBackButton.setOnClickListener(v -> {
            animateClick(holoBackButton);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            finish();
        });

        // Card Prise de Masse (Muscle Builder)
        megaCardMuscle.setOnClickListener(v -> {
            animateCardClick(megaCardMuscle);
            Intent intent = new Intent(InfoActivity.this, ValidationActivityPM.class);
            intent.putExtra("objectif", "prise_masse");
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        // Card Perte de Poids (Fat Burner)
        megaCardBurn.setOnClickListener(v -> {
            animateCardClick(megaCardBurn);
            Intent intent = new Intent(InfoActivity.this, ValidationActivityPP.class);
            intent.putExtra("objectif", "perte_poids");
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        // Card Maintien (Balance)
        megaCardBalance.setOnClickListener(v -> {
            animateCardClick(megaCardBalance);
            Intent intent = new Intent(InfoActivity.this, ValidationActivityM.class);
            intent.putExtra("objectif", "maintien");
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
    }

    private void animateClick(View view) {
        // Animation simple de clic
        view.animate()
                .scaleX(0.95f)
                .scaleY(0.95f)
                .setDuration(100)
                .withEndAction(() -> {
                    view.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(100)
                            .start();
                })
                .start();
    }

    private void animateCardClick(MaterialCardView card) {
        // Animation de clic pour les cartes
        card.animate()
                .scaleX(0.98f)
                .scaleY(0.98f)
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

        // Animation pulse simple
        try {
            Animation pulse = AnimationUtils.loadAnimation(this, R.anim.pulse);
            card.startAnimation(pulse);
        } catch (Exception e) {
            // Si l'animation pulse n'existe pas, on continue sans erreur
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
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
    protected void onPause() {
        super.onPause();
        // Nettoyer les animations
        if (megaCardMuscle != null) megaCardMuscle.clearAnimation();
        if (megaCardBurn != null) megaCardBurn.clearAnimation();
        if (megaCardBalance != null) megaCardBalance.clearAnimation();
        if (holoBackButton != null) holoBackButton.clearAnimation();
    }
}