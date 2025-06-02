package com.example.salledesport;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.salledesport.api.ApiService;
import com.example.salledesport.api.RetrofitClient;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminDashboardActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private CardView cardTotalRevenue, cardActiveSubscriptions, cardNewMembers;
    private TextView tvTotalRevenue, tvActiveSubscriptions, tvNewMembers;
    private BarChart chartMonthlyRevenue;
    private Spinner spinnerYear;
    private ApiService apiService;
    private int selectedYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        // Initialiser les vues
        progressBar = findViewById(R.id.progressBar);
        cardTotalRevenue = findViewById(R.id.cardTotalRevenue);
        cardActiveSubscriptions = findViewById(R.id.cardActiveSubscriptions);
        cardNewMembers = findViewById(R.id.cardNewMembers);
        tvTotalRevenue = findViewById(R.id.tvTotalRevenue);
        tvActiveSubscriptions = findViewById(R.id.tvActiveSubscriptions);
        tvNewMembers = findViewById(R.id.tvNewMembers);
        chartMonthlyRevenue = findViewById(R.id.chartMonthlyRevenue);
        spinnerYear = findViewById(R.id.spinnerYear);

        // Initialiser l'API service
        apiService = RetrofitClient.getClient(getApplicationContext()).create(ApiService.class);

        // Configurer le spinner des années
        setupYearSpinner();

        // Configurer le graphique
        setupChart();

        // Charger les données
        loadDashboardData();
    }

    private void setupYearSpinner() {
        // Obtenir l'année actuelle
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        selectedYear = currentYear;

        // Créer une liste d'années (de l'année actuelle à 3 ans en arrière)
        List<Integer> years = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            years.add(currentYear - i);
        }

        // Configurer l'adaptateur
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, years);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYear.setAdapter(adapter);

        // Configurer le listener
        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedYear = (int) parent.getItemAtPosition(position);
                loadMonthlyRevenue(selectedYear);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Ne rien faire
            }
        });
    }

    private void setupChart() {
        // Configurer le graphique
        chartMonthlyRevenue.getDescription().setEnabled(false);
        chartMonthlyRevenue.setDrawGridBackground(false);
        chartMonthlyRevenue.setDrawBarShadow(false);
        chartMonthlyRevenue.setHighlightFullBarEnabled(false);
        chartMonthlyRevenue.setMaxVisibleValueCount(12);
        chartMonthlyRevenue.setPinchZoom(false);
        chartMonthlyRevenue.setDrawValueAboveBar(true);
        chartMonthlyRevenue.getAxisRight().setEnabled(false);

        // Configurer l'axe X
        XAxis xAxis = chartMonthlyRevenue.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(getMonthNames()));
    }

    private String[] getMonthNames() {
        return new String[]{"Jan", "Fév", "Mar", "Avr", "Mai", "Juin", "Juil", "Août", "Sep", "Oct", "Nov", "Déc"};
    }

    private void loadDashboardData() {
        progressBar.setVisibility(View.VISIBLE);
        cardTotalRevenue.setVisibility(View.GONE);
        cardActiveSubscriptions.setVisibility(View.GONE);
        cardNewMembers.setVisibility(View.GONE);
        chartMonthlyRevenue.setVisibility(View.GONE);

        // Charger les statistiques générales
        loadStatistics();

        // Charger les revenus mensuels pour l'année sélectionnée
        loadMonthlyRevenue(selectedYear);
    }

    private void loadStatistics() {
        Call<Map<String, Object>> call = apiService.getStatistics();

        call.enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                progressBar.setVisibility(View.GONE);
                
                if (response.isSuccessful() && response.body() != null) {
                    Map<String, Object> stats = response.body();
                    
                    // Afficher les statistiques
                    tvTotalRevenue.setText(stats.get("total_revenue") + " €");
                    tvActiveSubscriptions.setText(stats.get("active_subscriptions") + "");
                    tvNewMembers.setText(stats.get("new_members") + "");
                    
                    cardTotalRevenue.setVisibility(View.VISIBLE);
                    cardActiveSubscriptions.setVisibility(View.VISIBLE);
                    cardNewMembers.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(AdminDashboardActivity.this, 
                        "Erreur lors du chargement des statistiques: " + response.code(), 
                        Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(AdminDashboardActivity.this, 
                    "Échec de la connexion: " + t.getMessage(), 
                    Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadMonthlyRevenue(int year) {
        Call<Map<String, Double>> call = apiService.getMonthlyRevenue(year);

        call.enqueue(new Callback<Map<String, Double>>() {
            @Override
            public void onResponse(Call<Map<String, Double>> call, Response<Map<String, Double>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Map<String, Double> monthlyRevenue = response.body();
                    updateChart(monthlyRevenue);
                    chartMonthlyRevenue.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(AdminDashboardActivity.this, 
                        "Erreur lors du chargement des revenus mensuels: " + response.code(), 
                        Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Map<String, Double>> call, Throwable t) {
                Toast.makeText(AdminDashboardActivity.this, 
                    "Échec de la connexion: " + t.getMessage(), 
                    Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateChart(Map<String, Double> monthlyRevenue) {
        List<BarEntry> entries = new ArrayList<>();
        
        // Convertir les données pour le graphique
        for (int i = 1; i <= 12; i++) {
            String monthKey = String.format(Locale.getDefault(), "%02d", i);
            float value = 0f;
            if (monthlyRevenue.containsKey(monthKey)) {
                value = monthlyRevenue.get(monthKey).floatValue();
            }
            entries.add(new BarEntry(i - 1, value));
        }
        
        // Créer le dataset
        BarDataSet dataSet = new BarDataSet(entries, "Revenus mensuels (€)");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        
        // Créer les données
        BarData data = new BarData(dataSet);
        data.setBarWidth(0.7f);
        
        // Mettre à jour le graphique
        chartMonthlyRevenue.setData(data);
        chartMonthlyRevenue.invalidate();
    }
}
