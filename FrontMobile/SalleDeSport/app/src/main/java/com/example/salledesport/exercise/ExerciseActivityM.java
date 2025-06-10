package com.example.salledesport.exercise;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

import com.example.salledesport.R;
import com.example.salledesport.exercise.maintien.ExercicesEtirementsActivity;
import com.example.salledesport.exercise.maintien.ExercicesYogaActivity;
import com.example.salledesport.exercise.maintien.nutrition.NutritionActivityM;

public class ExerciseActivityM extends AppCompatActivity {

    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_m);

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
        findViewById(R.id.cardExerciceYoga).setOnClickListener(v -> {
            // Lancer l'activité des exercices de yoga
            Intent intent = new Intent(ExerciseActivityM.this, ExercicesYogaActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.cardExerciceStretching).setOnClickListener(v -> {
            // Lancer l'activité des exercices d'étirement
            Intent intent = new Intent(ExerciseActivityM.this, ExercicesEtirementsActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.cardNutrition).setOnClickListener(v -> {
            // Lancer l'activité des conseils nutritionnels
            Intent intent = new Intent(ExerciseActivityM.this, NutritionActivityM.class);
            startActivity(intent);
        });
    }
}
