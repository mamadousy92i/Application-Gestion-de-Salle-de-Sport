package com.example.salledesport;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.salledesport.model.Payment;
import com.example.salledesport.model.Subscription;
import com.example.salledesport.api.ApiService;
import com.example.salledesport.api.RetrofitClient;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity {

    private TextView tvPlanName, tvAmount;
    private RadioGroup rgPaymentMethod;
    private Button btnConfirmPayment;
    private Subscription subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // Initialiser les vues
        tvPlanName = findViewById(R.id.tvPlanName);
        tvAmount = findViewById(R.id.tvAmount);
        rgPaymentMethod = findViewById(R.id.rgPaymentMethod);
        btnConfirmPayment = findViewById(R.id.btnConfirmPayment);

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
        btnConfirmPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processPayment();
            }
        });
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

        btnConfirmPayment.setEnabled(false);
        btnConfirmPayment.setText("Traitement en cours...");

        call.enqueue(new Callback<Payment>() {
            @Override
            public void onResponse(Call<Payment> call, Response<Payment> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(PaymentActivity.this, "Paiement effectué avec succès", Toast.LENGTH_LONG).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    btnConfirmPayment.setEnabled(true);
                    btnConfirmPayment.setText("Confirmer le paiement");
                    Toast.makeText(PaymentActivity.this, "Erreur lors du paiement: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Payment> call, Throwable t) {
                btnConfirmPayment.setEnabled(true);
                btnConfirmPayment.setText("Confirmer le paiement");
                Toast.makeText(PaymentActivity.this, "Échec de la connexion: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    // Convertir le texte affiché en code de méthode de paiement pour l'API
    private String getPaymentMethodCode(String displayText) {
        switch (displayText) {
            case "Carte bancaire":
                return "card";
            case "Espèces":
                return "cash";
            case "Virement bancaire":
                return "transfer";
            case "Chèque":
                return "check";
            default:
                return "card"; // Par défaut
        }
    }
}