package com.example.salledesport;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salledesport.adapters.PaymentAdapter;
import com.example.salledesport.model.Payment;
import com.example.salledesport.model.PaginatedResponse;
import com.example.salledesport.api.ApiService;
import com.example.salledesport.api.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PaymentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_history);

        recyclerView = findViewById(R.id.paymentHistoryRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fetchPayments();
    }

    private void fetchPayments() {
        ApiService apiService = RetrofitClient.getClient(getApplicationContext()).create(ApiService.class);
        Call<PaginatedResponse<Payment>> call = apiService.getPayments();

        call.enqueue(new Callback<PaginatedResponse<Payment>>() {
            @Override
            public void onResponse(Call<PaginatedResponse<Payment>> call, Response<PaginatedResponse<Payment>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Payment> payments = response.body().getResults();
                    if (payments != null && !payments.isEmpty()) {
                        adapter = new PaymentAdapter(payments);
                        recyclerView.setAdapter(adapter);
                    } else {
                        Toast.makeText(PaymentHistoryActivity.this, "Aucun paiement trouvé", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(PaymentHistoryActivity.this, "Erreur serveur: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PaginatedResponse<Payment>> call, Throwable t) {
                Toast.makeText(PaymentHistoryActivity.this, "Erreur réseau : " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
