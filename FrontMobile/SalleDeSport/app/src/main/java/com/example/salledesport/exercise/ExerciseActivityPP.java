package com.example.salledesport.exercise;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

import com.example.salledesport.R;
import com.example.salledesport.exercise.pertedepoids.ExercicesCardioActivity;
import com.example.salledesport.exercise.pertedepoids.ExercicesCircuitActivity;
import com.example.salledesport.exercise.pertedepoids.nutrition.NutritionActivityPP;

public class ExerciseActivityPP extends AppCompatActivity {

    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_pp);

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
        findViewById(R.id.cardExerciceCardio).setOnClickListener(v -> {
            // Lancer l'activité des exercices cardio
            Intent intent = new Intent(ExerciseActivityPP.this, ExercicesCardioActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.cardExerciceCircuit).setOnClickListener(v -> {
            // Lancer l'activité des exercices en circuit
            Intent intent = new Intent(ExerciseActivityPP.this, ExercicesCircuitActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.cardNutrition).setOnClickListener(v -> {
            // Lancer l'activité des conseils nutritionnels
            Intent intent = new Intent(ExerciseActivityPP.this, NutritionActivityPP.class);
            startActivity(intent);
        });
    }
}
