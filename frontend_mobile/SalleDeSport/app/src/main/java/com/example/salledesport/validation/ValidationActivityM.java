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

import com.example.salledesport.exercise.ExerciseActivityM;
import com.example.salledesport.R;
import com.example.salledesport.utils.BaseActivity;

public class ValidationActivityM extends BaseActivity {

    private FrameLayout backButton;
    private ImageView objectifIcon;
    private TextView objectifTitle, encouragementMessage, benefit1, benefit2, benefit3;
    private LinearLayout continueButton;
    private View iconContainer;
    private String objectifChoisi = "maintien";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validationm);

        // Configuration des barres système héritée de BaseActivity
        setupSystemBarsBehavior();

        initViews();
        setupMaintienContent();
        setupAnimations();
        setupListeners();
    }

    private void initViews() {
        backButton = findViewById(R.id.backButton);
        objectifIcon = findViewById(R.id.objectifIcon);
        objectifTitle = findViewById(R.id.objectifTitle);
        encouragementMessage = findViewById(R.id.encouragementMessage);
        benefit1 = findViewById(R.id.benefit1);
        benefit2 = findViewById(R.id.benefit2);
        benefit3 = findViewById(R.id.benefit3);
        continueButton = findViewById(R.id.continueButton);
        iconContainer = findViewById(R.id.iconContainer);
    }

    private void setupMaintienContent() {
        objectifTitle.setText("MAINTENIR MA FORME");
        objectifIcon.setImageResource(R.drawable.group);

        encouragementMessage.setText("Continuez à cultiver votre équilibre et votre forme avec un programme conçu pour préserver vos acquis et booster votre bien-être au quotidien.");

        benefit1.setText("Programme personnalisé d'équilibre");
        benefit2.setText("Suivi intelligent de votre progression");
        benefit3.setText("Bien-être et résultats durables");
    }

    private void setupAnimations() {
        // Animation d'apparition de l'icône principale
        Animation scaleIn = AnimationUtils.loadAnimation(this, R.anim.scale_in);
        Animation fadeInSlow = AnimationUtils.loadAnimation(this, R.anim.fade_in_slow);
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);

        // Animation de l'icône avec effet de glow
        iconContainer.postDelayed(() -> {
            iconContainer.startAnimation(scaleIn);
            iconContainer.setVisibility(View.VISIBLE);
        }, 200);

        // Animation du titre
        objectifTitle.postDelayed(() -> {
            objectifTitle.startAnimation(fadeInSlow);
            objectifTitle.setVisibility(View.VISIBLE);
        }, 600);

        // Animation des bénéfices
        View benefitsContainer = findViewById(R.id.benefitsContainer);
        if (benefitsContainer != null) {
            benefitsContainer.postDelayed(() -> {
                benefitsContainer.startAnimation(slideUp);
                benefitsContainer.setVisibility(View.VISIBLE);
            }, 1000);
        }

        // Animation du bouton continuer
        continueButton.postDelayed(() -> {
            continueButton.startAnimation(slideUp);
            continueButton.setVisibility(View.VISIBLE);
        }, 1200);
    }

    private void setupListeners() {
        // Bouton retour avec effet holographique
        backButton.setOnClickListener(v -> {
            // Effet de pulsation sur le bouton
            Animation pulse = AnimationUtils.loadAnimation(this, R.anim.pulse);
            backButton.startAnimation(pulse);

            // Retour avec transition
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            finish();
        });

        // Bouton continuer avec effet futuriste
        continueButton.setOnClickListener(v -> {
            // Effet de pulsation avant la transition
            Animation pulse = AnimationUtils.loadAnimation(this, R.anim.pulse);
            continueButton.startAnimation(pulse);

            // Navigation vers l'activité suivante
            Intent intent = new Intent(ValidationActivityM.this, ExerciseActivityM.class);
            intent.putExtra("objectif", objectifChoisi);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
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
        // Optionnel : masquer les barres système quand l'utilisateur quitte l'app
        hideSystemUI();
    }
}