package com.example.salledesport;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.salledesport.LoginActivity;
import com.example.salledesport.PaymentActivity;
import com.example.salledesport.R;
import com.example.salledesport.model.Subscription;
import com.example.salledesport.model.SubscriptionPlan;
import com.example.salledesport.api.ApiService;
import com.example.salledesport.api.RetrofitClient;
import com.example.salledesport.utils.BaseActivity;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlanDetailActivity extends BaseActivity {

    private ImageView planImage;
    private TextView planTitle, planDescription, planPrice;
    private LinearLayout planOptions;
    private TextView tvStartDate;
    private Button btnSubscribe;
    private SwitchMaterial switchAutoRenewal;
    private SubscriptionPlan plan;
    private String startDate;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;

    private ActivityResultLauncher<Intent> paymentLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Toast.makeText(this, "Abonnement activé avec succès!", Toast.LENGTH_LONG).show();
                    finish();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // IMPORTANT: Appeler super.onCreate() de BaseActivity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_detail);

        // NOUVEAU: Activer le comportement de masquage des barres système
        setupSystemBarsBehavior();

        // NOUVEAU: Masquer immédiatement les barres système
        hideSystemUI();

        // Initialiser les vues
        planImage = findViewById(R.id.planImage);
        planTitle = findViewById(R.id.planTitle);
        planDescription = findViewById(R.id.planDescription);
        planPrice = findViewById(R.id.planPrice);
        planOptions = findViewById(R.id.planOptions);
        btnSubscribe = findViewById(R.id.btnSubscribe);
        tvStartDate = findViewById(R.id.tvStartDate);
        switchAutoRenewal = findViewById(R.id.switchAutoRenewal);

        // Vérifier si l'utilisateur est connecté
        SharedPreferences sharedPreferences = getSharedPreferences("auth", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("id", -1);
        String userJson = sharedPreferences.getString("user", "");

        // Initialiser le calendrier
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        startDate = dateFormat.format(calendar.getTime());
        tvStartDate.setText("Date de début: " + startDate);

        // Récupère les données via Intent (JSON string)
        String planJson = getIntent().getStringExtra("plan");
        plan = new Gson().fromJson(planJson, SubscriptionPlan.class);

        bindDataToUI();
        setupListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // NOUVEAU: Re-masquer les barres à chaque fois que l'activité redevient visible
        hideSystemUI();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        // NOUVEAU: Masquer les barres quand la fenêtre reprend le focus
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void bindDataToUI() {
        planTitle.setText(plan.getName());
        planDescription.setText(plan.getDescription());
        planPrice.setText(plan.getPrice() + "€/mois");

        if (planOptions != null) {
            planOptions.removeAllViews();

            String[] options = {
                    "• " + plan.getMaxSessionsPerWeek() + " séances par semaine",
                    plan.isIncludesCoach() ? "✔ Coach inclus" : "❌ Coach non inclus",
                    plan.isIncludesGroupClasses() ? "✔ Cours collectifs" : "❌ Pas de cours collectifs",
                    plan.isIncludesPool() ? "✔ Piscine" : "❌ Pas de piscine"
            };

            for (String option : options) {
                TextView optionView = new TextView(this);
                optionView.setText(option);
                optionView.setTextSize(14);
                optionView.setTextColor(ContextCompat.getColor(this, R.color.white));

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, 8, 0, 8);
                optionView.setLayoutParams(params);

                optionView.setPadding(16, 8, 16, 8);
                planOptions.addView(optionView);
            }
        }

        if (plan.getImage() != null && !plan.getImage().isEmpty()) {
            Glide.with(this)
                    .load(plan.getImage())
                    .placeholder(R.drawable.placeholder)
                    .into(planImage);
        } else {
            planImage.setImageResource(R.drawable.placeholder);
        }
    }

    private void setupListeners() {
        // Configurer le sélecteur de date
        tvStartDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (view, year, month, dayOfMonth) -> {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        startDate = dateFormat.format(calendar.getTime());
                        tvStartDate.setText("Date de début: " + startDate);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });

        // Configurer le bouton d'abonnement
        btnSubscribe.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = getSharedPreferences("auth", MODE_PRIVATE);
            int userId = sharedPreferences.getInt("id", -1);
            String userJson = sharedPreferences.getString("user", "");

            if (userId <= 0 || userJson.isEmpty()) {
                Toast.makeText(this, "Vous devez être connecté pour souscrire à un abonnement", Toast.LENGTH_LONG).show();
                Intent loginIntent = new Intent(PlanDetailActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                return;
            }

            createSubscription();
        });

        // Bouton retour
        View backButton = findViewById(R.id.back_button);
        if (backButton != null) {
            backButton.setOnClickListener(v -> finish());
        }
    }

    private void createSubscription() {
        btnSubscribe.setEnabled(false);
        btnSubscribe.setText("Traitement en cours...");

        Calendar endCalendar = (Calendar) calendar.clone();
        endCalendar.add(Calendar.MONTH, plan.getDurationMonths());
        String endDate = dateFormat.format(endCalendar.getTime());

        SharedPreferences sharedPreferences = getSharedPreferences("auth", MODE_PRIVATE);
        int memberId = sharedPreferences.getInt("member_id", -1);

        if (memberId <= 0) {
            Toast.makeText(this, "Erreur: Vous devez être connecté pour souscrire à un abonnement", Toast.LENGTH_LONG).show();
            btnSubscribe.setEnabled(true);
            btnSubscribe.setText("S'abonner");
            return;
        }

        Subscription subscription = new Subscription();
        subscription.setPlanId(plan.getId());
        subscription.setMemberId(memberId);
        subscription.setStartDate(startDate);
        subscription.setEndDate(endDate);
        subscription.setAutoRenewal(switchAutoRenewal.isChecked());
        subscription.setStatus("pending");
        subscription.setPaymentStatus(false);

        ApiService apiService = RetrofitClient.getClient(getApplicationContext()).create(ApiService.class);
        Call<Subscription> call = apiService.createSubscription(subscription);

        call.enqueue(new Callback<Subscription>() {
            @Override
            public void onResponse(Call<Subscription> call, Response<Subscription> response) {
                btnSubscribe.setEnabled(true);
                btnSubscribe.setText("S'abonner");

                if (response.isSuccessful() && response.body() != null) {
                    Subscription createdSubscription = response.body();

                    Intent intent = new Intent(PlanDetailActivity.this, PaymentActivity.class);
                    intent.putExtra("subscription", new Gson().toJson(createdSubscription));
                    paymentLauncher.launch(intent);
                } else {
                    Toast.makeText(PlanDetailActivity.this,
                            "Erreur lors de la création de l'abonnement: " + response.code(),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Subscription> call, Throwable t) {
                btnSubscribe.setEnabled(true);
                btnSubscribe.setText("S'abonner");
                Toast.makeText(PlanDetailActivity.this,
                        "Erreur de connexion: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}