package com.example.salledesport;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.salledesport.model.Subscription;
import com.example.salledesport.model.SubscriptionPlan;
import com.example.salledesport.model.User;
import com.example.salledesport.api.ApiService;
import com.example.salledesport.api.RetrofitClient;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlanDetailActivity extends AppCompatActivity {

    private ImageView planImage;
    private TextView planTitle, planDescription, planPrice, planOptions;
    private TextView tvStartDate;
    private Button btnSubscribe;
    private Switch switchAutoRenewal;
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
                } else {
                    Toast.makeText(this, "Paiement annulé ou échoué", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_detail);

        // Initialiser les vues
        planImage = findViewById(R.id.planImage);
        planTitle = findViewById(R.id.planTitle);
        planDescription = findViewById(R.id.planDescription);
        planPrice = findViewById(R.id.planPrice);
        planOptions = findViewById(R.id.planOptions);
        btnSubscribe = findViewById(R.id.btnSubscribe);
        tvStartDate = findViewById(R.id.tvStartDate);
        switchAutoRenewal = findViewById(R.id.switchAutoRenewal);

        // Initialiser le calendrier et format
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        startDate = dateFormat.format(calendar.getTime());
        tvStartDate.setText("Date de début: " + startDate);

        // Récupérer les données du plan
        String planJson = getIntent().getStringExtra("plan");
        plan = new Gson().fromJson(planJson, SubscriptionPlan.class);

        bindDataToUI();
        setupListeners();
    }

    private void bindDataToUI() {
        planTitle.setText(plan.getName());
        planDescription.setText(plan.getDescription());
        planPrice.setText(plan.getPrice() + "€/mois");

        String options = "• " + plan.getMaxSessionsPerWeek() + " séances par semaine\n";
        options += plan.isIncludesCoach() ? "✔ Coach inclus\n" : "❌ Coach non inclus\n";
        options += plan.isIncludesGroupClasses() ? "✔ Cours collectifs\n" : "❌ Pas de cours collectifs\n";
        options += plan.isIncludesPool() ? "✔ Piscine" : "❌ Pas de piscine";

        planOptions.setText(options);

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

        btnSubscribe.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = getSharedPreferences("auth", MODE_PRIVATE);
            String userJson = sharedPreferences.getString("user", "");
            if (userJson.isEmpty()) {
                Toast.makeText(this, "Vous devez être connecté pour souscrire à un abonnement", Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, LoginActivity.class));
                return;
            }

            if (calendar.getTime().before(new Date())) {
                Toast.makeText(this, "La date de début ne peut pas être antérieure à aujourd'hui.", Toast.LENGTH_LONG).show();
                return;
            }

            createSubscription(userJson);
        });
    }

    private void createSubscription(String userJson) {
        btnSubscribe.setEnabled(false);
        btnSubscribe.setText("Traitement en cours...");

        Calendar endCalendar = (Calendar) calendar.clone();
        endCalendar.add(Calendar.MONTH, plan.getDurationMonths());
        String endDate = dateFormat.format(endCalendar.getTime());

        // Récupération du membre à partir du JSON
        User user = new Gson().fromJson(userJson, User.class);
        int memberId = user.getId();

        if (memberId <= 0) {
            Toast.makeText(this, "Erreur: utilisateur invalide", Toast.LENGTH_LONG).show();
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

        Log.d("API", "createSubscription: " + new Gson().toJson(subscription));

        ApiService apiService = RetrofitClient.getClient(getApplicationContext()).create(ApiService.class);
        Call<Subscription> call = apiService.createSubscription(subscription);

        call.enqueue(new Callback<Subscription>() {
            @Override
            public void onResponse(Call<Subscription> call, Response<Subscription> response) {
                btnSubscribe.setEnabled(true);
                btnSubscribe.setText("S'abonner");

                if (response.isSuccessful() && response.body() != null) {
                    Subscription created = response.body();
                    Intent intent = new Intent(PlanDetailActivity.this, PaymentActivity.class);
                    intent.putExtra("subscription", new Gson().toJson(created));
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
