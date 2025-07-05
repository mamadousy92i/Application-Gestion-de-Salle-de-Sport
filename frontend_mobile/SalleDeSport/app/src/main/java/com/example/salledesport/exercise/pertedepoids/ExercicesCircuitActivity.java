package com.example.salledesport.exercise.pertedepoids;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.salledesport.R;
import com.example.salledesport.exercise.pertedepoids.nutrition.NutritionActivityPP;

public class ExercicesCircuitActivity extends AppCompatActivity {

    private ImageButton backButton;
    private CardView cardFentes, cardPlanche, cardFenteArriere, cardFlexionsLaterales, cardSautGenoux, cardAbdos;
    private LinearLayout continueButton; // Utilisation de LinearLayout pour le bouton "Continuer"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercices_circuit);

        // Initialiser les vues
        initViews();

        // Vérifier si le backButton n'est pas null
        backButton = findViewById(R.id.btnBack);
        if (backButton != null) {
            backButton.setOnClickListener(v -> {
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            });
        } else {
            Log.e("ExercicesCircuitActivity", "backButton is null");
        }

        // Configurer les animations d'entrée
        setupAnimations();

        // Configurer les listeners pour les autres vues
        setupListeners();
    }

    private void initViews() {
        backButton = findViewById(R.id.btnBack);
        cardFentes = findViewById(R.id.cardFentes);
        cardPlanche = findViewById(R.id.cardPlanche);
        cardFenteArriere = findViewById(R.id.cardFenteArriere);
        cardFlexionsLaterales = findViewById(R.id.cardFlexionsLaterales);
        cardSautGenoux = findViewById(R.id.cardSautGenoux);
        cardAbdos = findViewById(R.id.cardAbdos);


        continueButton = findViewById(R.id.continueButton); // Utilisation de LinearLayout pour le bouton "Continuer"

    }

    private void setupAnimations() {
        // Animation de fade in pour les cartes
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation slideFromLeft = AnimationUtils.loadAnimation(this, R.anim.slide_from_left);
        Animation slideFromRight = AnimationUtils.loadAnimation(this, R.anim.slide_from_right);

        // Appliquer les animations avec des délais
        cardFentes.postDelayed(() -> {
            cardFentes.startAnimation(slideFromLeft);
            cardFentes.setVisibility(View.VISIBLE);
        }, 200);

        cardPlanche.postDelayed(() -> {
            cardPlanche.startAnimation(slideFromRight);
            cardPlanche.setVisibility(View.VISIBLE);
        }, 300);

        cardFenteArriere.postDelayed(() -> {
            cardFenteArriere.startAnimation(slideFromLeft);
            cardFenteArriere.setVisibility(View.VISIBLE);
        }, 400);

        cardFlexionsLaterales.postDelayed(() -> {
            cardFlexionsLaterales.startAnimation(slideFromRight);
            cardFlexionsLaterales.setVisibility(View.VISIBLE);
        }, 500);

        cardSautGenoux.postDelayed(() -> {
            cardSautGenoux.startAnimation(slideFromLeft);
            cardSautGenoux.setVisibility(View.VISIBLE);
        }, 600);

        cardAbdos.postDelayed(() -> {
            cardAbdos.startAnimation(slideFromRight);
            cardAbdos.setVisibility(View.VISIBLE);
        }, 700);
    }

    private void setupListeners() {
        // Bouton de retour
        backButton.setOnClickListener(v -> {
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            finish();
        });


        // Configurer le listener pour le LinearLayout "Continuer"
        continueButton.setOnClickListener(v -> {
            // Créer un Intent pour démarrer NutritionActivity
            Intent intent = new Intent(ExercicesCircuitActivity.this, NutritionActivityPP.class);
            startActivity(intent);
        });


        // Card Fentes
        cardFentes.setOnClickListener(v -> {
            animateCardClick(cardFentes);
            // Intent vers les détails de l'exercice
        });

        // Card Planche
        cardPlanche.setOnClickListener(v -> {
            animateCardClick(cardPlanche);
            // Intent vers les détails de l'exercice
        });

        // Card Fente Arrière
        cardFenteArriere.setOnClickListener(v -> {
            animateCardClick(cardFenteArriere);
            // Intent vers les détails de l'exercice
        });

        // Card Flexions Latérales
        cardFlexionsLaterales.setOnClickListener(v -> {
            animateCardClick(cardFlexionsLaterales);
            // Intent vers les détails de l'exercice
        });

        // Card Saut en Genoux
        cardSautGenoux.setOnClickListener(v -> {
            animateCardClick(cardSautGenoux);
            // Intent vers les détails de l'exercice
        });

        // Card Abdos
        cardAbdos.setOnClickListener(v -> {
            animateCardClick(cardAbdos);
            // Intent vers les détails de l'exercice
        });
    }

    private void animateCardClick(CardView card) {
        // Animation de "pulse" lors du clic
        Animation pulse = AnimationUtils.loadAnimation(this, R.anim.pulse);
        card.startAnimation(pulse);
    }

    @Override
    public void onBackPressed() {
        // Animation personnalisée lors du retour
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
