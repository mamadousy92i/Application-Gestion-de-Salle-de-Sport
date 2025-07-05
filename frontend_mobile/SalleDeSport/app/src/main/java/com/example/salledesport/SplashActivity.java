package com.example.salledesport;

import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;
import com.bumptech.glide.Glide;
import com.example.salledesport.api.RetrofitClient;
import com.example.salledesport.api.SplashScreen;
import com.example.salledesport.model.ApiResponse.SlideResponse;
import com.example.salledesport.utils.BaseActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends BaseActivity {
private ViewFlipper imageFlipper;

private TextView registerLink;
private Button getStartedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setupSystemBarsBehavior();

        initialized();
        // Masquer la barre de statut et la barre de navigation
        hideSystemUI();
        setupDiaporama();

        SharedPreferences appPrefs = getSharedPreferences("auth", MODE_PRIVATE);

        boolean isFirstLaunch = appPrefs.getBoolean("first_launch", true);
            if(!isFirstLaunch) {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
                return;
            }

        getStartedButton.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("auth", MODE_PRIVATE);
            appPrefs.edit().putBoolean("first_launch", false).apply();
            int userId = prefs.getInt("id", -1);
            if (userId != -1) {
                startActivity(new Intent(SplashActivity.this, Home.class));
            } else {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }
            finish();
        });

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SplashActivity.this, RegisterActivity.class));
            }
        });

    }

    private void setupDiaporama() {
        SplashScreen splashScreen = RetrofitClient.getClient(getApplicationContext()).create(SplashScreen.class);
        Call<SlideResponse>call =splashScreen.getSlides();

        call.enqueue(new Callback<SlideResponse>() {
            @Override
            public void onResponse(Call<SlideResponse> call, Response<SlideResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    SlideResponse slideResponse = response.body();
                    for (int i = 0; i < slideResponse.getResults().size(); i++) {
                        String image = slideResponse.getResults().get(i).getImage_url();
                        ImageView imageView = new ImageView(SplashActivity.this);
                        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        if (!isDestroyed() && !isFinishing()) {
                            Glide.with(SplashActivity.this)
                                    .load(image)
                                    .into(imageView);
                            imageFlipper.addView(imageView);
                        }

                    }

                }
            }

            @Override
            public void onFailure(Call<SlideResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void initialized(){
        imageFlipper = findViewById(R.id.imageFlipper);
        registerLink = findViewById(R.id.registerLink);
        getStartedButton = findViewById(R.id.getStartedButton);
    }



}