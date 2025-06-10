package com.example.salledesport.exercise.maintien;

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
import com.example.salledesport.exercise.maintien.nutrition.NutritionActivityM;

public class ExercicesYogaActivity extends AppCompatActivity {

    private ImageButton backButton;
    private CardView cardSalutation, cardGuerrier, cardEnfant, cardCobra, cardArbre, cardTorsion;
    private LinearLayout continueButton; // Utilisation de LinearLayout pour le bouton "Continuer"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercices_yoga);

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
            Log.e("ExercicesYogaActivity", "backButton is null");
        }

        // Configurer les animations d'entrée
        setupAnimations();

        // Configurer les listeners pour les autres vues
        setupListeners();
    }


    private void initViews() {
        backButton = findViewById(R.id.btnBack);
        cardSalutation = findViewById(R.id.cardSalutation);
        cardGuerrier = findViewById(R.id.cardGuerrier);
        cardEnfant = findViewById(R.id.cardEnfant);
        cardCobra = findViewById(R.id.cardCobra);
        cardArbre = findViewById(R.id.cardArbre);
        cardTorsion = findViewById(R.id.cardTorsion);

        // Initialisation du bouton "Continuer"
        continueButton = findViewById(R.id.continueButton); // Utilisation de LinearLayout pour le bouton "Continuer"

    }

    private void setupAnimations() {
        // Animation de fade in pour les cartes
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation slideFromLeft = AnimationUtils.loadAnimation(this, R.anim.slide_from_left);
        Animation slideFromRight = AnimationUtils.loadAnimation(this, R.anim.slide_from_right);

        // Appliquer les animations avec des délais
        cardSalutation.postDelayed(() -> {
            cardSalutation.startAnimation(slideFromLeft);
            cardSalutation.setVisibility(View.VISIBLE);
        }, 200);

        cardGuerrier.postDelayed(() -> {
            cardGuerrier.startAnimation(slideFromRight);
            cardGuerrier.setVisibility(View.VISIBLE);
        }, 300);

        cardEnfant.postDelayed(() -> {
            cardEnfant.startAnimation(slideFromLeft);
            cardEnfant.setVisibility(View.VISIBLE);
        }, 400);

        cardCobra.postDelayed(() -> {
            cardCobra.startAnimation(slideFromRight);
            cardCobra.setVisibility(View.VISIBLE);
        }, 500);

        cardArbre.postDelayed(() -> {
            cardArbre.startAnimation(slideFromLeft);
            cardArbre.setVisibility(View.VISIBLE);
        }, 600);

        cardTorsion.postDelayed(() -> {
            cardTorsion.startAnimation(slideFromRight);
            cardTorsion.setVisibility(View.VISIBLE);
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
            Intent intent = new Intent(ExercicesYogaActivity.this, NutritionActivityM.class);
            startActivity(intent);
        });

        // Card Salutation au Soleil
        cardSalutation.setOnClickListener(v -> {
            animateCardClick(cardSalutation);
            // Intent vers les détails de l'exercice
            // Intent intent = new Intent(ExercisesYogaActivity.this, ExerciseDetailActivity.class);
            // intent.putExtra("exercise_name", "Salutation au Soleil");
            // startActivity(intent);
        });

        // Card Posture du Guerrier
        cardGuerrier.setOnClickListener(v -> {
            animateCardClick(cardGuerrier);
            // Intent vers les détails de l'exercice
        });

        // Card Posture de l'Enfant
        cardEnfant.setOnClickListener(v -> {
            animateCardClick(cardEnfant);
            // Intent vers les détails de l'exercice
        });

        // Card Posture du Cobra
        cardCobra.setOnClickListener(v -> {
            animateCardClick(cardCobra);
            // Intent vers les détails de l'exercice
        });

        // Card Posture de l'Arbre
        cardArbre.setOnClickListener(v -> {
            animateCardClick(cardArbre);
            // Intent vers les détails de l'exercice
        });

        // Card Torsion Spinale
        cardTorsion.setOnClickListener(v -> {
            animateCardClick(cardTorsion);
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