package com.example.salledesport;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.salledesport.model.Payment;
import com.example.salledesport.model.Subscription;
import com.example.salledesport.api.ApiService;
import com.example.salledesport.api.RetrofitClient;
import com.example.salledesport.utils.BaseActivity;
import com.google.android.material.card.MaterialCardView;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends BaseActivity {

    private TextView tvPlanName, tvAmount;
    private RadioGroup rgPaymentMethod;
    private TextView btnConfirmPayment;
    private Subscription subscription;
    private MaterialCardView backButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // Configurer le comportement des barres système
        setupSystemBarsBehavior();

        // Initialiser les vues
        tvPlanName = findViewById(R.id.tvPlanName);
        tvAmount = findViewById(R.id.tvAmount);
        rgPaymentMethod = findViewById(R.id.rgPaymentMethod);
        btnConfirmPayment = findViewById(R.id.btnConfirmPayment);
        backButton = findViewById(R.id.backButton);


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // Récupérer l'abonnement depuis l'intent
        String subscriptionJson = getIntent().getStringExtra("subscription");
        if (subscriptionJson != null) {
            subscription = new Gson().fromJson(subscriptionJson, Subscription.class);

            // Afficher les détails de l'abonnement
            tvPlanName.setText(subscription.getPlan().getName());
            tvAmount.setText(subscription.getPlan().getPrice() + " €");
        } else {
            Toast.makeText(this, "Erreur: Aucun abonnement trouvé", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Configurer le bouton de paiement
        View paymentCardButton = findViewById(R.id.paymentCardButton);
        paymentCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processPayment();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Masquer les barres système quand l'activité devient visible
        hideSystemUI();
    }

    private void processPayment() {
        // Vérifier qu'une méthode de paiement est sélectionnée
        int selectedId = rgPaymentMethod.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(this, "Veuillez sélectionner une méthode de paiement", Toast.LENGTH_SHORT).show();
            return;
        }

        // Récupérer la méthode de paiement sélectionnée
        RadioButton radioButton = findViewById(selectedId);
        String paymentMethod = getPaymentMethodCode(radioButton.getText().toString());

        // Créer l'objet Payment
        Payment payment = new Payment();
        payment.setSubscriptionId(subscription.getId());
        payment.setAmount(subscription.getPlan().getPrice());
        payment.setPaymentMethod(paymentMethod);
        payment.setStatus("completed");

        // Appeler l'API pour enregistrer le paiement
        ApiService apiService = RetrofitClient.getClient(getApplicationContext()).create(ApiService.class);
        Call<Payment> call = apiService.createPayment(payment);

        View paymentCardButton = findViewById(R.id.paymentCardButton);
        paymentCardButton.setClickable(false);
        btnConfirmPayment.setText("Traitement en cours...");

        call.enqueue(new Callback<Payment>() {
            @Override
            public void onResponse(Call<Payment> call, Response<Payment> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(PaymentActivity.this, "Paiement effectué avec succès", Toast.LENGTH_LONG).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    View paymentCardButton = findViewById(R.id.paymentCardButton);
                    paymentCardButton.setClickable(true);
                    btnConfirmPayment.setText("Confirmer le paiement");
                    Toast.makeText(PaymentActivity.this, "Erreur lors du paiement: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Payment> call, Throwable t) {
                View paymentCardButton = findViewById(R.id.paymentCardButton);
                paymentCardButton.setClickable(true);
                btnConfirmPayment.setText("Confirmer le paiement");
                Toast.makeText(PaymentActivity.this, "Échec de la connexion: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    // Convertir le texte affiché en code de méthode de paiement pour l'API
    private String getPaymentMethodCode(String displayText) {
        switch (displayText) {
            case "💳 Carte de crédit":
                return "card";
            case "🏦 Virement bancaire":
                return "transfer";
            case "💳 PayPal":
                return "paypal";
            case "Carte bancaire":
                return "card";
            case "Espèces":
                return "cash";
            case "Chèque":
                return "check";
            default:
                return "card"; // Par défaut
        }
    }
}