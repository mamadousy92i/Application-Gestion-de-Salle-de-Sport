package com.example.salledesport.utils;


import android.view.View;
import androidx.appcompat.app.AppCompatActivity;


import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Activer le mode Edge-to-Edge
        EdgeToEdge.enable(this);

        // Désactiver le fitting system windows selon l'API disponible
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // API 30+
            getWindow().setDecorFitsSystemWindows(false);
        } else {
            // API <30
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    protected void setupSystemBarsBehavior() {
        View mainView = findViewById(android.R.id.content); // Utiliser le content view par défaut
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                boolean systemBarsVisible = insets.isVisible(WindowInsetsCompat.Type.systemBars());
                if (!systemBarsVisible) {
                    // Si les barres sont cachées, programmer leur nouveau masquage
                    v.postDelayed(this::hideSystemUI, 2000);
                }
                return insets;
            });
        }
    }

    protected void hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsController controller = getWindow().getInsetsController();
            if (controller != null) {
                controller.hide(WindowInsets.Type.systemBars());
                controller.setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
            }
        } else {
            View decorView = getWindow().getDecorView();
            int flags = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;

            decorView.setSystemUiVisibility(flags);

            // Gestion du réaffichage pour les versions < API 30
            decorView.setOnSystemUiVisibilityChangeListener(visibility -> {
                if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    decorView.postDelayed(this::hideSystemUI, 2000);
                }
            });
        }
    }
}