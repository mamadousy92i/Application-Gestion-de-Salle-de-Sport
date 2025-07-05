package com.example.salledesport.exercise.prisedemasse;

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
import com.example.salledesport.exercise.prisedemasse.nutrition.NutritionActivityPM;

public class ExercicesMusculairesActivity extends AppCompatActivity {

    private ImageButton backButton;
    private CardView cardDips, cardPresseACuisses, cardDeveloppeMilitaire, cardCurl, cardSquatsSautes, cardTirageHorizontal;

    private LinearLayout continueButton; // Utilisation de LinearLayout pour le bouton "Continuer"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercices_musculaires);

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
            Log.e("ExercicesMusculairesActivity", "backButton is null");
        }

        // Configurer les animations d'entrée
        setupAnimations();

        // Configurer les listeners pour les autres vues
        setupListeners();
    }

    private void initViews() {
        backButton = findViewById(R.id.btnBack);
        cardDips = findViewById(R.id.cardDips);
        cardPresseACuisses = findViewById(R.id.cardPresseACuisses);
        cardDeveloppeMilitaire = findViewById(R.id.cardDeveloppeMilitaire);
        cardCurl = findViewById(R.id.cardCurl);
        cardSquatsSautes = findViewById(R.id.cardSquatsSautes);
        cardTirageHorizontal = findViewById(R.id.cardTirageHorizontal);

        continueButton = findViewById(R.id.continueButton); // Utilisation de LinearLayout pour le bouton "Continuer"

    }

    private void setupAnimations() {
        // Animation de fade in pour les cartes
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation slideFromLeft = AnimationUtils.loadAnimation(this, R.anim.slide_from_left);
        Animation slideFromRight = AnimationUtils.loadAnimation(this, R.anim.slide_from_right);

        // Appliquer les animations avec des délais
        cardDips.postDelayed(() -> {
            cardDips.startAnimation(slideFromLeft);
            cardDips.setVisibility(View.VISIBLE);
        }, 200);

        cardPresseACuisses.postDelayed(() -> {
            cardPresseACuisses.startAnimation(slideFromRight);
            cardPresseACuisses.setVisibility(View.VISIBLE);
        }, 300);

        cardDeveloppeMilitaire.postDelayed(() -> {
            cardDeveloppeMilitaire.startAnimation(slideFromLeft);
            cardDeveloppeMilitaire.setVisibility(View.VISIBLE);
        }, 400);

        cardCurl.postDelayed(() -> {
            cardCurl.startAnimation(slideFromRight);
            cardCurl.setVisibility(View.VISIBLE);
        }, 500);

        cardSquatsSautes.postDelayed(() -> {
            cardSquatsSautes.startAnimation(slideFromLeft);
            cardSquatsSautes.setVisibility(View.VISIBLE);
        }, 600);

        cardTirageHorizontal.postDelayed(() -> {
            cardTirageHorizontal.startAnimation(slideFromRight);
            cardTirageHorizontal.setVisibility(View.VISIBLE);
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
            Intent intent = new Intent(ExercicesMusculairesActivity.this, NutritionActivityPM.class);
            startActivity(intent);
        });

        // Card Dips sur barres parallèles
        cardDips.setOnClickListener(v -> {
            animateCardClick(cardDips);
            // Intent vers les détails de l'exercice
        });

        // Card Presse à cuisses
        cardPresseACuisses.setOnClickListener(v -> {
            animateCardClick(cardPresseACuisses);
            // Intent vers les détails de l'exercice
        });

        // Card Développé militaire
        cardDeveloppeMilitaire.setOnClickListener(v -> {
            animateCardClick(cardDeveloppeMilitaire);
            // Intent vers les détails de l'exercice
        });

        // Card Curl
        cardCurl.setOnClickListener(v -> {
            animateCardClick(cardCurl);
            // Intent vers les détails de l'exercice
        });

        // Card Squats sautés
        cardSquatsSautes.setOnClickListener(v -> {
            animateCardClick(cardSquatsSautes);
            // Intent vers les détails de l'exercice
        });

        // Card Tirage horizontal
        cardTirageHorizontal.setOnClickListener(v -> {
            animateCardClick(cardTirageHorizontal);
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
