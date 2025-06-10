package com.example.salledesport;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import com.example.salledesport.adapters.SlideshowAdapter;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 backgroundSlideshow;
    private Handler slideshowHandler;
    private Runnable slideshowRunnable;
    private int currentPage = 0;

    // Images du diaporama (ajoutez vos images dans res/drawable)
    private int[] slideshowImages = {
            R.drawable.slide1,  // Remplacez par vos vraies images
            R.drawable.slide2,
            R.drawable.slide3,
            R.drawable.slide4
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);  // Lien vers le fichier XML de mise en page

        // Initialiser le diaporama
        setupSlideshow();
        startAutoSlideshow();

        // Références aux éléments de l'interface
        LinearLayout startButton = findViewById(R.id.startButton);

        // Ajouter un clic sur le bouton pour démarrer l'application
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Passer à l'écran des informations personnelles
                Intent intent = new Intent(MainActivity.this, InfoActivity.class);
                startActivity(intent);  // Lance la deuxième activité
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