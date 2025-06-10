package com.example.salledesport;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.core.view.ViewCompat;


import com.example.salledesport.api.AuthService;
import com.example.salledesport.api.RetrofitClient;

import com.example.salledesport.model.ApiResponse.LoginResponse;
import com.example.salledesport.model.AuthUser.LoginRequest;
import com.example.salledesport.model.Utilisateur;
import com.example.salledesport.utils.BaseActivity;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends BaseActivity {

    private EditText emailTxt;
    private EditText passwordTxt;
    private Button connexionBtn;
    private TextView inscriptionLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Configurer la gestion des barres système
        setupSystemBarsBehavior();

        // Gestion des WindowInsets (pas de padding pour un look Edge-to-Edge)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            // Ne pas appliquer de padding pour un affichage Edge-to-Edge
            return insets;
        });
        SharedPreferences prefs = getSharedPreferences("auth", MODE_PRIVATE);
        int userId = prefs.getInt("id", -1);
        if (userId != -1) {
            startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
            finish();
        }

        hideSystemUI();
        initializedFields();

        connexionBtn.setOnClickListener(v -> {
            String email = emailTxt.getText().toString();
            String password = passwordTxt.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Veuillez renseigner tous les champs", Toast.LENGTH_LONG).show();
            } else {

                connexionBtn.setEnabled(false);

                LoginRequest dataUser = new LoginRequest(email, password);
                AuthService authService = RetrofitClient.getClient(getApplicationContext()).create(AuthService.class);
                Call<LoginResponse> call = authService.LoginUser(dataUser);

                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        try {
                            if (response.isSuccessful() && response.body() != null) {
                                if (response.body().isSuccess()) {
                                    Utilisateur utilisateur = response.body().getUser();
                                    int userId = utilisateur.getId();
                                    Gson gson = new Gson();
                                    String userJson = gson.toJson(utilisateur);

                                    getSharedPreferences("auth", MODE_PRIVATE)
                                            .edit()
                                            .putInt("id", userId)
                                            .putString("user", userJson)
                                            .apply();

                                    Toast.makeText(LoginActivity.this, "Connexion réussie avec succès", Toast.LENGTH_LONG).show();
                                    emailTxt.setText("");
                                    passwordTxt.setText("");

                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                } else {
                                    connexionBtn.setEnabled(true);

                                    Toast.makeText(LoginActivity.this, "Échec de la connexion", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                connexionBtn.setEnabled(true);

                                Toast.makeText(LoginActivity.this, "Erreur: réponse invalide du serveur", Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            connexionBtn.setEnabled(true);

                            Toast.makeText(LoginActivity.this, "Erreur lors du traitement de la réponse", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        connexionBtn.setEnabled(true);
                        Toast.makeText(LoginActivity.this, "Erreur réseau: " + t.getMessage(), Toast.LENGTH_LONG).show();
                        t.printStackTrace();
                    }
                });
            }
        });

        inscriptionLink.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
    }

    private void initializedFields() {
        emailTxt = findViewById(R.id.login_edt);
        passwordTxt = findViewById(R.id.mdp_edt);
        connexionBtn = findViewById(R.id.connexion_btn);
        inscriptionLink = findViewById(R.id.inscriptionBtn);
    }
}