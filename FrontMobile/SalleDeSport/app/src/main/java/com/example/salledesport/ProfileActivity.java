package com.example.salledesport;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.core.view.ViewCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.salledesport.api.ProfileService;
import com.example.salledesport.api.RetrofitClient;
import com.example.salledesport.model.Utilisateur;
import com.example.salledesport.utils.BaseActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class ProfileActivity extends BaseActivity {
    private ImageView profile;
    private TextView firstName;
    private TextView lastName;
    private TextView age;
    private TextView taille;
    private TextView poids;
    private TextView objectif;
    private MaterialButton editButton;
    private MaterialCardView logoutButton;

    private ActivityResultLauncher<Intent> editProfileLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Configurer la gestion des barres système
        setupSystemBarsBehavior();

        // Gestion des WindowInsets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            // Ne pas appliquer de padding
            return insets;
        });

        hideSystemUI();
        initialized();
        loadProfileDetail();

        editProfileLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        // Recharge les données du profil après modification
                        loadProfileDetail();
                    }
                }
        );

        editButton.setOnClickListener(v -> editProfileLauncher.launch(new Intent(ProfileActivity.this, EditProfileActivity.class)));

        logoutButton.setOnClickListener(v -> logoutConfirmationDialog());
    }

    private void initialized() {
        profile = findViewById(R.id.profile_image);
        firstName = findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name);
        age = findViewById(R.id.age);
        taille = findViewById(R.id.height);
        poids = findViewById(R.id.weight);
        objectif = findViewById(R.id.goal);
        editButton = findViewById(R.id.edit_button);
        logoutButton = findViewById(R.id.logout_button);

    }

    private void loadProfileDetail() {
        ProfileService profileService = RetrofitClient.getClient(getApplicationContext()).create(ProfileService.class);
        Call<Utilisateur> call = profileService.getProfile();

        call.enqueue(new Callback<Utilisateur>() {
            @Override
            public void onResponse(@NonNull Call<Utilisateur> call, @NonNull Response<Utilisateur> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String userJson = getSharedPreferences("auth", MODE_PRIVATE).getString("user", null);
                    if (userJson != null) {
                        Utilisateur user = response.body();
                        if (displayProfile(user)) {
                            Toast.makeText(ProfileActivity.this,
                                    "Données profil chargées avec succès",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Utilisateur> call, @NonNull Throwable t) {
                Toast.makeText(ProfileActivity.this,
                        "Erreur de chargement des données profile",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean displayProfile(Utilisateur user) {
        firstName.setText(user.getFirst_name());
        lastName.setText(user.getLast_name());
        age.setText(String.format("%s ans", user.getAge()));
        taille.setText(String.format("%s m", user.getTaille()));
        poids.setText(String.format("%s Kg", user.getPoids()));
        if (user.getGoals() != null && !user.getGoals().isEmpty()) {
            objectif.setText(user.getGoals().get(0).getName());
        } else {
            objectif.setText("Vous n'avez pas d'objectif");
        }

        if (user.getProfile_picture() != null && !user.getProfile_picture().isEmpty()) {
            String image = RetrofitClient.getBaseUrl() + user.getProfile_picture();
            Glide.with(this)
                    .load(image)
                    .error(R.drawable.ic_edit_profile)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                    @NonNull Target<Drawable> target, boolean isFirstResource) {
                            Log.e("GLIDE", "Erreur de chargement", e);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(@NonNull Drawable resource, @NonNull Object model,
                                                       Target<Drawable> target, @NonNull DataSource dataSource,
                                                       boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(profile);
        }
        return true;
    }

    private void logout() {
        getSharedPreferences("auth", MODE_PRIVATE).edit().clear().apply();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // ⛔ évite de revenir en arrière

        startActivity(intent);
        Toast.makeText(this, "Déconnexion réussie", Toast.LENGTH_SHORT).show();
        finish();
    }


    private void logoutConfirmationDialog() {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Confirmation")
                .setContentText("Voulez-vous vraiment vous déconnecter ?")
                .setConfirmText("Oui")
                .setConfirmClickListener(sDialog -> {
                    sDialog.dismiss();
                    logout();
                })
                .setCancelText("Annuler")
                .setCancelClickListener(SweetAlertDialog::dismiss)
                .show();
    }
}