package com.example.salledesport.exercise;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

import com.example.salledesport.R;
import com.example.salledesport.exercise.prisedemasse.ExercicesDeForceActivity;
import com.example.salledesport.exercise.prisedemasse.ExercicesMusculairesActivity;
import com.example.salledesport.exercise.prisedemasse.nutrition.NutritionActivityPM;

public class ExerciseActivityPM extends AppCompatActivity {

    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_pm);

        // Initialiser les vues
        initViews();

        // Configurer les listeners
        setupListeners();
    }

    private void initViews() {
        // Bouton de retour
        backButton = findViewById(R.id.backButton);
    }

    private void setupListeners() {
        // Listener pour le bouton retour
        backButton.setOnClickListener(v -> {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });

        // Listener pour les cartes d'exercices
        findViewById(R.id.cardExerciceForce).setOnClickListener(v -> {
            // Lancer l'activité des exercices de force
            Intent intent = new Intent(ExerciseActivityPM.this, ExercicesDeForceActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.cardExerciceMusculaire).setOnClickListener(v -> {
            // Lancer l'activité des exercices musculaires
            Intent intent = new Intent(ExerciseActivityPM.this, ExercicesMusculairesActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.cardNutrition).setOnClickListener(v -> {
            // Lancer l'activité des conseils nutritionnels
            Intent intent = new Intent(ExerciseActivityPM.this, NutritionActivityPM.class);
            startActivity(intent);
        });
    }
}
