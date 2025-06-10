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

public class ExercicesCardioActivity extends AppCompatActivity {

    private ImageButton backButton;
    private CardView cardCourseSurPlace, cardSautsEtoile, cardBurpees, cardMountainClimbers, cardJumpingJack, cardHighKnees;
    private LinearLayout continueButton; // Utilisation de LinearLayout pour le bouton "Continuer"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercices_cardio);

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
            Log.e("ExercisesCardioActivity", "backButton is null");
        }

        // Configurer les animations d'entrée
        setupAnimations();

        // Configurer les listeners pour les autres vues
        setupListeners();
    }

    private void initViews() {
        backButton = findViewById(R.id.btnBack);
        cardCourseSurPlace = findViewById(R.id.cardCourseSurPlace);
        cardSautsEtoile = findViewById(R.id.cardSautsEtoile);
        cardBurpees = findViewById(R.id.cardBurpees);
        cardMountainClimbers = findViewById(R.id.cardMountainClimbers);
        cardJumpingJack = findViewById(R.id.cardJumpingJack);
        cardHighKnees = findViewById(R.id.cardHighKnees);

        continueButton = findViewById(R.id.continueButton); // Utilisation de LinearLayout pour le bouton "Continuer"

    }

    private void setupAnimations() {
        // Animation de fade in pour les cartes
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation slideFromLeft = AnimationUtils.loadAnimation(this, R.anim.slide_from_left);
        Animation slideFromRight = AnimationUtils.loadAnimation(this, R.anim.slide_from_right);

        // Appliquer les animations avec des délais
        cardCourseSurPlace.postDelayed(() -> {
            cardCourseSurPlace.startAnimation(slideFromLeft);
            cardCourseSurPlace.setVisibility(View.VISIBLE);
        }, 200);

        cardSautsEtoile.postDelayed(() -> {
            cardSautsEtoile.startAnimation(slideFromRight);
            cardSautsEtoile.setVisibility(View.VISIBLE);
        }, 300);

        cardBurpees.postDelayed(() -> {
            cardBurpees.startAnimation(slideFromLeft);
            cardBurpees.setVisibility(View.VISIBLE);
        }, 400);

        cardMountainClimbers.postDelayed(() -> {
            cardMountainClimbers.startAnimation(slideFromRight);
            cardMountainClimbers.setVisibility(View.VISIBLE);
        }, 500);

        cardJumpingJack.postDelayed(() -> {
            cardJumpingJack.startAnimation(slideFromLeft);
            cardJumpingJack.setVisibility(View.VISIBLE);
        }, 600);

        cardHighKnees.postDelayed(() -> {
            cardHighKnees.startAnimation(slideFromRight);
            cardHighKnees.setVisibility(View.VISIBLE);
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
            Intent intent = new Intent(ExercicesCardioActivity.this, NutritionActivityPP.class);
            startActivity(intent);
        });


        // Card Course sur place
        cardCourseSurPlace.setOnClickListener(v -> {
            animateCardClick(cardCourseSurPlace);
            // Intent vers les détails de l'exercice
        });

        // Card Sauts en étoile
        cardSautsEtoile.setOnClickListener(v -> {
            animateCardClick(cardSautsEtoile);
            // Intent vers les détails de l'exercice
        });

        // Card Burpees
        cardBurpees.setOnClickListener(v -> {
            animateCardClick(cardBurpees);
            // Intent vers les détails de l'exercice
        });

        // Card Mountain climbers
        cardMountainClimbers.setOnClickListener(v -> {
            animateCardClick(cardMountainClimbers);
            // Intent vers les détails de l'exercice
        });

        // Card Jumping Jacks
        cardJumpingJack.setOnClickListener(v -> {
            animateCardClick(cardJumpingJack);
            // Intent vers les détails de l'exercice
        });

        // Card High Knees
        cardHighKnees.setOnClickListener(v -> {
            animateCardClick(cardHighKnees);
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
