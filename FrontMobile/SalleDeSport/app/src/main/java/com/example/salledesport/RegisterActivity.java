package com.example.salledesport;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.salledesport.api.AuthService;
import com.example.salledesport.api.RetrofitClient;
import com.example.salledesport.model.ApiResponse.RegisterResponse;
import com.example.salledesport.model.Utilisateur;
import com.example.salledesport.utils.BaseActivity;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends BaseActivity {
    private EditText nomTxt;
    private EditText prenomTxt;
    private EditText emailTxt;
    private EditText telephoneTxt;
    private EditText usernameTxt;
    private EditText passwordTxt;
    private EditText confirmPasswordTxt;

    private EditText dateNaissanceTxt;
    private EditText adresseTxt;

    private Button inscriptionBtn;
    private Button nextBtn;
    private Button backBtn;
    private TextView inscriptionLink;
    String[] genders = new String[]{"Homme", "Femme", "Autre"};
    private AutoCompleteTextView genderDropdown;




    private LinearLayout personalInfoLayout;
    private LinearLayout loginInfoLayout;
    private LinearLayout loginLinkLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        // Configurer la gestion des barres système
        setupSystemBarsBehavior();

        // Gestion des WindowInsets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            // Ne pas appliquer de padding
            return insets;
            // Initialisation du dropdown

        });
        hideSystemUI();
        initializedFields();




        // Date picker
        dateNaissanceTxt.setOnClickListener(a -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    RegisterActivity.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        String monthFormatted = (selectedMonth + 1) < 10 ? "0" + (selectedMonth + 1) : String.valueOf(selectedMonth + 1);
                        String dayFormatted = selectedDay < 10 ? "0" + selectedDay : String.valueOf(selectedDay);
                        String date = selectedYear + "-" + monthFormatted + "-" + dayFormatted;
                        dateNaissanceTxt.setText(date);
                    },
                    year, month, day
            );
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialog.show();
        });

        // Next button click - show login info
        nextBtn.setOnClickListener(v -> {
            if (validatePersonalInfo()) {
                personalInfoLayout.setVisibility(View.GONE);
                loginInfoLayout.setVisibility(View.VISIBLE);
                loginLinkLayout.setVisibility(View.GONE);
            }
        });

        // Back button click - show personal info
        backBtn.setOnClickListener(v -> {
            personalInfoLayout.setVisibility(View.VISIBLE);
            loginInfoLayout.setVisibility(View.GONE);
            loginLinkLayout.setVisibility(View.VISIBLE);
        });

        // Register button click
        inscriptionBtn.setOnClickListener(v -> {
            if (!validateAllFields()) {
                return;
            }
            registerUser();
        });

        // Login link click
        inscriptionLink.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        });
    }

    public void initializedFields(){
        nomTxt = findViewById(R.id.first_name_edt);
        prenomTxt = findViewById(R.id.last_name_edt);
        emailTxt = findViewById(R.id.email_edt);
        telephoneTxt = findViewById(R.id.phone_edt);
        adresseTxt = findViewById(R.id.address_edt);
        dateNaissanceTxt = findViewById(R.id.dateNaissance_edt);
        passwordTxt = findViewById(R.id.password_edt);
        confirmPasswordTxt = findViewById(R.id.confirm_password_edt);
        usernameTxt = findViewById(R.id.username_edt);
        genderDropdown = findViewById(R.id.gender_edt);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.dropdown_item,
                genders
        );
        genderDropdown.setAdapter(adapter);




        nextBtn = findViewById(R.id.next_btn);
        backBtn = findViewById(R.id.back_btn);
        inscriptionBtn = findViewById(R.id.signup_btn);
        inscriptionLink = findViewById(R.id.login_btn);

        personalInfoLayout = findViewById(R.id.personal_info_layout);
        loginInfoLayout = findViewById(R.id.login_info_layout);
        loginLinkLayout = findViewById(R.id.login_link_layout);
    }

    private boolean validatePersonalInfo() {
        String nom = nomTxt.getText().toString();
        String prenom = prenomTxt.getText().toString();
        String telephone = telephoneTxt.getText().toString();
        String dateNaissance = dateNaissanceTxt.getText().toString();
        String adresse = adresseTxt.getText().toString();
        String gender = genderDropdown.getText().toString();


        if (prenom.isEmpty() || nom.isEmpty()  ||
                telephone.isEmpty() || dateNaissance.isEmpty()  || adresse.isEmpty() || gender.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private boolean validateAllFields() {
        if (!validatePersonalInfo()) {
            return false;
        }
        String email = emailTxt.getText().toString();
        String username = usernameTxt.getText().toString();
        String password = passwordTxt.getText().toString();
        String confirmPassword = confirmPasswordTxt.getText().toString();
        String gender = genderDropdown.getText().toString();


        if (username.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Veuillez saisir un nom d'utilisateur", Toast.LENGTH_LONG).show();
            return false;
        }
        if (email.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Veuillez saisir une adresse e-mail correcte", Toast.LENGTH_LONG).show();
            return false;
        }

        if (gender.isEmpty()) {
            Toast.makeText(this, "Veuillez sélectionner votre sexe", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.length() < 8) {
            Toast.makeText(RegisterActivity.this, "Le mot de passe doit contenir 8 caractères", Toast.LENGTH_LONG).show();
            return false;
        }

        if (!confirmPassword.equals(password)) {
            Toast.makeText(RegisterActivity.this, "Les mots de passe ne correspondent pas", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private void registerUser() {
        String nom = nomTxt.getText().toString();
        String prenom = prenomTxt.getText().toString();
        String email = emailTxt.getText().toString();
        String telephone = telephoneTxt.getText().toString();
        String username = usernameTxt.getText().toString();
        String password = passwordTxt.getText().toString();
        String dateNaissance = dateNaissanceTxt.getText().toString();
        String adresse = adresseTxt.getText().toString();
        String gender = genderDropdown.getText().toString();


        Utilisateur newUser = new Utilisateur(
                email,
                username,
                prenom,
                nom,
                 telephone, adresse,
                dateNaissance, password, null,
                gender
        );

        AuthService authService = RetrofitClient.getClient(getApplicationContext()).create(AuthService.class);
        Call<RegisterResponse> call = authService.RegisterUser(newUser);

        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        if (response.body().isSuccess()) {
                            Toast.makeText(RegisterActivity.this, "Inscription réussie avec succès", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, "Échec de l'inscription", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "Erreur: réponse invalide du serveur", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(RegisterActivity.this, "Erreur lors du traitement de la réponse", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Erreur réseau", Toast.LENGTH_LONG).show();
            }
        });
    }
}