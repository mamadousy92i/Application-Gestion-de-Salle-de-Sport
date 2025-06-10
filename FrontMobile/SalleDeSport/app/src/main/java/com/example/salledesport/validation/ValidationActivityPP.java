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

import com.example.salledesport.exercise.ExerciseActivityPP;
import com.example.salledesport.R;

public class ValidationActivityPP extends AppCompatActivity {

    private ImageButton backButton;
    private ImageView objectifIcon;
    private TextView objectifTitle, encouragementMessage, benefit1, benefit2, benefit3;
    private LinearLayout continueButton, iconContainer;
    private String objectifChoisi = "perte_poids";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validationpp);

        initViews();
        setupPertePoidsContent();
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

    private void setupPertePoidsContent() {
        objectifTitle.setText("Perte de Poids");
        objectifIcon.setImageResource(R.drawable.calories);
        iconContainer.setBackgroundResource(R.drawable.circle_background_red);

        encouragementMessage.setText("Vous allez brûler les graisses efficacement et retrouver " +
                "votre silhouette idéale ! La motivation est la clé du succès.");

        benefit1.setText("Exercices cardio intensifs");
        benefit2.setText("Plan alimentaire équilibré");
        benefit3.setText("Suivi de perte de poids");
    }

    private void setupAnimations() {
        Animation scaleIn = AnimationUtils.loadAnimation(this, R.anim.scale_in);
        Animation fadeInSlow = AnimationUtils.loadAnimation(this, R.anim.fade_in_slow);
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);

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
        backButton.setOnClickListener(v -> {
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            finish();
        });

        continueButton.setOnClickListener(v -> {
            Animation pulse = AnimationUtils.loadAnimation(this, R.anim.pulse);
            continueButton.startAnimation(pulse);

            Intent intent = new Intent(ValidationActivityPP.this, ExerciseActivityPP.class);
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
