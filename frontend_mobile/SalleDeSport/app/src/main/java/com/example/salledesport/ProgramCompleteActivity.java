package com.example.salledesport;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProgramCompleteActivity extends AppCompatActivity {

    private ImageView successIcon;
    private TextView programTypeText, frequencyText, durationText;
    private Button btnStartProgram, btnBackToHome;

    // Données du programme (reçues via Intent)
    private String programType;
    private String frequency;
    private String duration;
    private String difficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_complete);

        // Initialisation des vues
        initViews();

        // Récupération des données du programme
        getProgramData();

        // Configuration des données affichées
        setupProgramInfo();

        // Configuration des animations
        setupAnimations();

        // Configuration des listeners
        setupClickListeners();
    }

    private void initViews() {
        successIcon = findViewById(R.id.successIcon);
        programTypeText = findViewById(R.id.programTypeText);
        frequencyText = findViewById(R.id.frequencyText);
        durationText = findViewById(R.id.durationText);
        btnBackToHome = findViewById(R.id.btnBackToHome);
    }

    private void getProgramData() {
        Intent intent = getIntent();
        if (intent != null) {
            programType = intent.getStringExtra("program_type");
            frequency = intent.getStringExtra("frequency");
            duration = intent.getStringExtra("duration");
            difficulty = intent.getStringExtra("difficulty");
        }

        // Valeurs par défaut si pas de données
        if (programType == null) programType = "Programme Personnalisé";
        if (frequency == null) frequency = "3-4 fois par semaine";
        if (duration == null) duration = "45-60 minutes";
    }

    private void setupProgramInfo() {
        // Mise à jour des textes avec les vraies données
        programTypeText.setText("Programme: " + programType);
        frequencyText.setText("Fréquence: " + frequency);
        durationText.setText("Durée: " + duration + " par séance");
    }

    private void setupAnimations() {
        // Animation de rotation pour l'icône de succès
        Animation rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_scale);
        successIcon.startAnimation(rotateAnimation);

        // Animation d'apparition pour les boutons
        Animation slideUpAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        btnStartProgram.startAnimation(slideUpAnimation);

        // Animation décalée pour le deuxième bouton
        btnBackToHome.postDelayed(() -> {
            Animation slideUpAnimation2 = AnimationUtils.loadAnimation(this, R.anim.slide_up);
            btnBackToHome.startAnimation(slideUpAnimation2);
        }, 200);
    }

    private void setupClickListeners() {

        // Bouton retour à l'accueil
        btnBackToHome.setOnClickListener(v -> {
            // Animation du bouton
            animateButton(btnBackToHome);

            // Retour à MainActivity
            Intent intent = new Intent(ProgramCompleteActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

            // Animation de transition
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        });
    }

    private void animateButton(Button button) {
        // Animation de pression du bouton
        button.animate()
                .scaleX(0.95f)
                .scaleY(0.95f)
                .setDuration(100)
                .withEndAction(() -> {
                    button.animate()
                            .scaleX(1.0f)
                            .scaleY(1.0f)
                            .setDuration(100);
                });
    }

    @Override
    public void onBackPressed() {
        // Retour à l'accueil au lieu de l'activité précédente
        super.onBackPressed();
        Intent intent = new Intent(ProgramCompleteActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Relancer l'animation de l'icône si nécessaire
        if (successIcon != null) {
            Animation rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.pulse);
            successIcon.startAnimation(rotateAnimation);
        }
    }
}