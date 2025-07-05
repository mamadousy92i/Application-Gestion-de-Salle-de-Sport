package com.example.salledesport;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import com.example.salledesport.adapters.SlideshowAdapter;
import com.example.salledesport.utils.BaseActivity;
import androidx.viewpager2.widget.ViewPager2;

public class MainActivity extends BaseActivity {

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

        // Configurer le comportement des barres système (hérité de BaseActivity)
        setupSystemBarsBehavior();

        // Masquer les barres système pour un effet immersif
        hideSystemUI();

        // Initialiser le diaporama
        setupSlideshow();
        startAutoSlideshow();

        // Références aux éléments de l'interface
        LinearLayout startButton = findViewById(R.id.startButton);

        // Ajouter un clic sur le bouton pour démarrer l'application
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Animation de clic
                animateButtonClick(startButton);

                // Passer à l'écran des informations personnelles
                Intent intent = new Intent(MainActivity.this, InfoActivity.class);
                startActivity(intent);  // Lance la deuxième activité

                // Animation de transition
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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

    private void animateButtonClick(View button) {
        // Animation simple de clic
        button.animate()
                .scaleX(0.95f)
                .scaleY(0.95f)
                .setDuration(100)
                .withEndAction(() -> {
                    button.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(100)
                            .start();
                })
                .start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Réactiver le mode immersif au retour sur l'activité
        hideSystemUI();

        // Redémarrer le slideshow si nécessaire
        if (slideshowHandler != null && slideshowRunnable != null) {
            startAutoSlideshow();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Arrêter le diaporama en pause pour économiser les ressources
        if (slideshowHandler != null && slideshowRunnable != null) {
            slideshowHandler.removeCallbacks(slideshowRunnable);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            // Masquer les barres système quand l'activité regagne le focus
            hideSystemUI();
        }
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