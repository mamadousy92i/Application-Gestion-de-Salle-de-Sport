package com.example.salledesport.exercise.pertedepoids.nutrition;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.salledesport.R;
import com.example.salledesport.adapters.SlideshowAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

public class NutritionActivityPP extends AppCompatActivity {

    private ViewPager2 backgroundSlideshow;
    private Handler slideshowHandler;
    private Runnable slideshowRunnable;
    private int currentPage = 0;

    // Images du diaporama pour la nutrition (ajoutez vos images dans res/drawable)
    private int[] slideshowImages = {
            R.drawable.slide7,  // Images liées à la nutrition
            R.drawable.slide9,
            R.drawable.slide10,
            R.drawable.slide6
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition_pp);  // Lien vers le fichier XML de mise en page

        // Initialiser le diaporama
        setupSlideshow();
        startAutoSlideshow();

        // Références aux éléments de l'interface
        LinearLayout startButton = findViewById(R.id.startButton);

        // Ajouter un clic sur le bouton pour voir les détails du plan nutritionnel
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Passer à l'écran des détails du plan nutritionnel
                Intent intent = new Intent(NutritionActivityPP.this, NutritionDetailsPP.class);
                startActivity(intent);  // Lance l'activité des détails nutrition
            }
        });

        // Ajouter la gestion du bouton retour dans la toolbar
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Appelle la méthode onBackPressed pour revenir à l'activité précédente
            }
        });
    }

    private void setupSlideshow() {
        backgroundSlideshow = findViewById(R.id.backgroundSlideshow);
        SlideshowAdapter adapter = new SlideshowAdapter(slideshowImages);
        backgroundSlideshow.setAdapter(adapter);
    }

    private void startAutoSlideshow() {
        slideshowHandler = new Handler();
        slideshowRunnable = new Runnable() {
            @Override
            public void run() {
                if (currentPage == slideshowImages.length) {
                    currentPage = 0;
                }
                backgroundSlideshow.setCurrentItem(currentPage++, true);
                slideshowHandler.postDelayed(this, 2000); // Change toutes les 2 secondes
            }
        };
        slideshowHandler.postDelayed(slideshowRunnable, 2000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Arrêter le diaporama quand l'activité est détruite
        if (slideshowHandler != null && slideshowRunnable != null) {
            slideshowHandler.removeCallbacks(slideshowRunnable);
        }
    }
}
