package com.example.salledesport.validation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.salledesport.exercise.ExerciseActivityPM;
import com.example.salledesport.R;

public class ValidationActivityPM extends AppCompatActivity {

    private ImageButton backButton;
    private ImageView objectifIcon;
    private TextView objectifTitle, encouragementMessage, benefit1, benefit2, benefit3;
    private LinearLayout continueButton, iconContainer;
    private String objectifChoisi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validationpm);

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
        backButton = findViewById(R.id.backButton);
        objectifIcon = findViewById(R.id.objectifIcon);
        objectifTitle = findViewById(R.id.objectifTitle);
        encouragementMessage = findViewById(R.id.encouragementMessage);
        benefit1 = findViewById(R.id.benefit1);
        benefit2 = findViewById(R.id.benefit2);
        benefit3 = findViewById(R.id.benefit3);
        continueButton = findViewById(R.id.continueButton);
        iconContainer = findViewById(R.id.iconContainer);  // ← Ajouté ici !

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
        objectifTitle.setText("Prise de Masse");
        objectifIcon.setImageResource(R.drawable.muscles);
        iconContainer.setBackgroundResource(R.drawable.circle_background_green);

        encouragementMessage.setText("Préparez-vous à développer une masse musculaire impressionnante ! " +
                "Votre transformation commence maintenant.");

        benefit1.setText("Exercices de force optimisés");
        benefit2.setText("Plan nutritionnel pour la masse");
        benefit3.setText("Progression muscle par muscle");
    }

    private void setupPertePoidsContent() {
        objectifTitle.setText("Perte de Poids");
        objectifIcon.setImageResource(R.drawable.ic_weight_loss);
        iconContainer.setBackgroundResource(R.drawable.circle_background_red);

        encouragementMessage.setText("Vous allez brûler les graisses efficacement et retrouver " +
                "votre silhouette idéale ! La motivation est la clé du succès.");

        benefit1.setText("Exercices cardio intensifs");
        benefit2.setText("Plan alimentaire équilibré");
        benefit3.setText("Suivi de perte de poids");
    }

    private void setupMaintienContent() {
        objectifTitle.setText("Maintien");
        objectifIcon.setImageResource(R.drawable.ic_balance);
        iconContainer.setBackgroundResource(R.drawable.circle_background_blue);

        encouragementMessage.setText("Parfait pour maintenir votre forme actuelle ! " +
                "Un équilibre parfait entre santé et bien-être vous attend.");

        benefit1.setText("Exercices d'entretien");
        benefit2.setText("Maintien du poids idéal");
        benefit3.setText("Routine stable et durable");
    }

    private void setupAnimations() {
        // Animation d'entrée pour l'icône
        Animation scaleIn = AnimationUtils.loadAnimation(this, R.anim.scale_in);
        Animation fadeInSlow = AnimationUtils.loadAnimation(this, R.anim.fade_in_slow);
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);

        // Appliquer les animations avec des délais
        iconContainer.postDelayed(() -> {
            iconContainer.startAnimation(scaleIn);
            iconContainer.setVisibility(View.VISIBLE);
        }, 200);

        objectifTitle.postDelayed(() -> {
            objectifTitle.startAnimation(fadeInSlow);
            objectifTitle.setVisibility(View.VISIBLE);
        }, 600);

        findViewById(R.id.benefitsContainer).postDelayed(() -> {
            findViewById(R.id.benefitsContainer).startAnimation(slideUp);
            findViewById(R.id.benefitsContainer).setVisibility(View.VISIBLE);
        }, 1000);

        continueButton.postDelayed(() -> {
            continueButton.startAnimation(slideUp);
            continueButton.setVisibility(View.VISIBLE);
        }, 1200);
    }

    private void setupListeners() {
        // Bouton de retour
        backButton.setOnClickListener(v -> {
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            finish();
        });

        // Bouton continuer
        continueButton.setOnClickListener(v -> {
            // Animation de clic
            Animation pulse = AnimationUtils.loadAnimation(this, R.anim.pulse);
            continueButton.startAnimation(pulse);

            // Aller à l'activité suivante (choix des exercices/zones)
            Intent intent = new Intent(ValidationActivityPM.this, ExerciseActivityPM.class);
            intent.putExtra("objectif", objectifChoisi);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}