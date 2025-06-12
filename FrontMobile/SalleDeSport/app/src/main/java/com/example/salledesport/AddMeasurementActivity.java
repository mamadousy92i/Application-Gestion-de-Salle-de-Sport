package com.example.salledesport;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.salledesport.api.ProfileService;
import com.example.salledesport.api.RetrofitClient;
import com.example.salledesport.model.UserUpdateRequest;
import com.example.salledesport.model.Utilisateur;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddMeasurementActivity extends AppCompatActivity {
    private EditText editWeight, editHeight;
    private Button btnAdd;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_measurement);

        editWeight = findViewById(R.id.editWeight);
        editHeight = findViewById(R.id.editHeight);
        btnAdd = findViewById(R.id.btnCalculate);
        btnBack = findViewById(R.id.back_button);

        btnAdd.setOnClickListener(v -> {
            String weightStr = editWeight.getText().toString();
            String heightStr = editHeight.getText().toString();

            if (weightStr.isEmpty() || heightStr.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double weight = Double.parseDouble(weightStr);
                double height = Double.parseDouble(heightStr);

                UserUpdateRequest userUpdate = new UserUpdateRequest();
                userUpdate.setPoids(weight);
                userUpdate.setTaille(height);

                ProfileService apiService = RetrofitClient.getApiService(this);
                Call<Utilisateur> call = apiService.updateProfile(userUpdate);

                call.enqueue(new Callback<Utilisateur>() {
                    @Override
                    public void onResponse(@NonNull Call<Utilisateur> call, @NonNull Response<Utilisateur> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(AddMeasurementActivity.this, "Taille et poids mis à jour !", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AddMeasurementActivity.this, ProfileActivity.class));
                        } else {
                            Toast.makeText(AddMeasurementActivity.this, "Erreur : " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Utilisateur> call, @NonNull Throwable t) {
                        Toast.makeText(AddMeasurementActivity.this, "Erreur réseau : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Entrez des valeurs numériques valides", Toast.LENGTH_SHORT).show();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}