package com.example.salledesport;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.example.salledesport.api.ApiService;
import com.example.salledesport.api.RetrofitClient;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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

    private void bindDataToUI() {
        planTitle.setText(plan.getName());
        planDescription.setText(plan.getDescription());
        planPrice.setText(plan.getPrice() + "€/mois");

        String options = "";
        options += "• " + plan.getMaxSessionsPerWeek() + " séances par semaine\n";
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
        btnSubscribe.setOnClickListener(v -> createSubscription());
    }

    private void createSubscription() {
        btnSubscribe.setEnabled(false);
        btnSubscribe.setText("Traitement en cours...");

        // Calculer la date de fin
        Calendar endCalendar = (Calendar) calendar.clone();
        endCalendar.add(Calendar.MONTH, plan.getDurationMonths());
        String endDate = dateFormat.format(endCalendar.getTime());

        // Créer l'objet Subscription
        Subscription subscription = new Subscription();
        subscription.setPlanId(plan.getId());
        subscription.setStartDate(startDate);
        subscription.setEndDate(endDate);
        subscription.setAutoRenewal(switchAutoRenewal.isChecked());
        subscription.setStatus("pending");
        subscription.setPaymentStatus(false);

        // Appeler l'API pour créer l'abonnement
        ApiService apiService = RetrofitClient.getClient(getApplicationContext()).create(ApiService.class);
        Call<Subscription> call = apiService.createSubscription(subscription);

        call.enqueue(new Callback<Subscription>() {
            @Override
            public void onResponse(Call<Subscription> call, Response<Subscription> response) {
                btnSubscribe.setEnabled(true);
                btnSubscribe.setText("S'abonner");

                if (response.isSuccessful() && response.body() != null) {
                    Subscription createdSubscription = response.body();
                    
                    // Passer à l'écran de paiement
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
