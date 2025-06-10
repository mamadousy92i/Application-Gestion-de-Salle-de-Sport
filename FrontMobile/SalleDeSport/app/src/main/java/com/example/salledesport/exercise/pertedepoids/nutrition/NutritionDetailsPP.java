package com.example.salledesport.exercise.pertedepoids.nutrition;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salledesport.ProgramCompleteActivity;
import com.example.salledesport.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NutritionDetailsPP extends AppCompatActivity {

    // Vues
    private LinearLayout headerGradient;
    private MaterialCardView nutritionCountersCard, waterReminderCard, stepsCard;
    private LinearLayout remindersContainer;
    private RecyclerView rvMeals;
    private TextView tvCaloriesCount, tvProteinCount, tvFiberCount;
    private TextView tvWaterCount, tvStepsCount;
    private TextView waterIcon, stepsIcon;
    private CircularProgressIndicator progressCalories;
    private LinearProgressIndicator progressWater, progressSteps;
    private MaterialButton btnAddMeal;
    private FloatingActionButton btnWaterReminder;

    // Variables
    private MealsAdapter mealsAdapter;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable waterReminderRunnable;
    private int stepsCount = 0;
    private int currentWaterCount = 0;
    private List<Meal> mealsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition_details_pp);

        // Initialiser les vues
        initializeMealsData();
        initializeViews();
        setupUI();
        setupMealsRecyclerView();
        startWaterReminder();
        animateEntry();

        // Trouver le bouton CTA
        LinearLayout startButton = findViewById(R.id.startButton);

        // Ajouter un listener sur le bouton
        startButton.setOnClickListener(v -> {
            // Créer un Intent pour naviguer vers ActivityProgramComplete
            Intent intent = new Intent(NutritionDetailsPP.this, ProgramCompleteActivity.class);
            startActivity(intent);
        });
    }

    private void initializeMealsData() {
        mealsList = new ArrayList<>();

        mealsList.add(new Meal("Petit-déjeuner", "07:00",
                Arrays.asList("Omelette aux blancs d'œufs", "Épinards frais", "1 toast complet", "Thé vert"),
                320, "🥚",
                "Démarrez avec des protéines ! Elles boostent le métabolisme et la satiété"));

        mealsList.add(new Meal("Collation matin", "10:00",
                Arrays.asList("Pomme verte", "10 amandes", "Infusion détox"),
                150, "🍏",
                "Les fibres de la pomme + les bons gras = coupe-faim naturel !"));

        mealsList.add(new Meal("Déjeuner", "13:00",
                Arrays.asList("Salade de quinoa", "100g blanc de poulet", "Légumes croquants", "Vinaigrette citron"),
                380, "🥗",
                "Repas roi ! Protéines + fibres = sensation de satiété prolongée"));

        mealsList.add(new Meal("Collation après-midi", "16:00",
                Arrays.asList("Yaourt grec 0%", "Myrtilles", "1 c.à.s. graines de chia"),
                120, "🫐",
                "Antioxydants + protéines pour tenir jusqu'au dîner sans craquer !"));

        mealsList.add(new Meal("Dîner", "19:00",
                Arrays.asList("Saumon grillé", "Courgetti", "Brocolis vapeur", "Huile d'olive"),
                420, "🐟",
                "Oméga-3 pour la récupération + légumes peu caloriques mais rassasiants"));

        mealsList.add(new Meal("En-cas soir", "21:30",
                Arrays.asList("Tisane drainante", "2 carrés chocolat noir 85%"),
                80, "🍵",
                "Le chocolat noir satisfait les envies sucrées avec peu de calories !"));
    }

    private void initializeViews() {
        headerGradient = findViewById(R.id.headerGradient);
        nutritionCountersCard = findViewById(R.id.nutritionCountersCard);
        waterReminderCard = findViewById(R.id.waterReminderCard);
        stepsCard = findViewById(R.id.stepsCard);
        remindersContainer = findViewById(R.id.remindersContainer);
        rvMeals = findViewById(R.id.rvMeals);

        tvCaloriesCount = findViewById(R.id.tvCaloriesCount);
        tvProteinCount = findViewById(R.id.tvProteinCount);
        tvFiberCount = findViewById(R.id.tvFiberCount);
        tvWaterCount = findViewById(R.id.tvWaterCount);
        tvStepsCount = findViewById(R.id.tvStepsCount);

        waterIcon = findViewById(R.id.waterIcon);
        stepsIcon = findViewById(R.id.stepsIcon);

        progressCalories = findViewById(R.id.progressCalories);
        progressWater = findViewById(R.id.progressWater);
        progressSteps = findViewById(R.id.progressSteps);

        btnAddMeal = findViewById(R.id.btnAddMeal);
        btnWaterReminder = findViewById(R.id.btnWaterReminder);
    }

    private void setupUI() {
        // Configuration du header avec gradient
        headerGradient.setBackground(ContextCompat.getDrawable(this, R.drawable.gradient_nutrition_loss_header));

        // Configuration des compteurs
        updateNutritionCounters();

        // Configuration des cartes de rappel
        setupReminderCards();

        // Boutons d'action
        btnAddMeal.setOnClickListener(v -> addCustomMeal());
        btnWaterReminder.setOnClickListener(v -> drinkWater());
    }

    private void setupMealsRecyclerView() {
        mealsAdapter = new MealsAdapter(mealsList, this::animateMealCompletion);
        rvMeals.setLayoutManager(new LinearLayoutManager(this));
        rvMeals.setAdapter(mealsAdapter);
        rvMeals.setNestedScrollingEnabled(false);
    }

    private void updateNutritionCounters() {
        int totalCalories = 0;
        for (Meal meal : mealsList) {
            totalCalories += meal.getCalories();
        }

        // Pour la perte de poids : plus de protéines (30%) et fibres importantes
        int proteinGrams = (int) (totalCalories * 0.30 / 4);
        int fiberGrams = (int) (totalCalories / 100); // Approximation fibres

        animateCounter(tvCaloriesCount, totalCalories);
        animateCounter(tvProteinCount, proteinGrams);
        animateCounter(tvFiberCount, fiberGrams);

        // Animation de la barre de progression des calories (objectif 1500 kcal)
        int progressTarget = (int) ((totalCalories / 1500.0f) * 100);
        animateProgress(progressCalories, Math.min(progressTarget, 100));
    }

    private void setupReminderCards() {
        // Configuration de la carte hydratation
        waterReminderCard.setOnClickListener(v -> {
            drinkWater();
            pulseAnimation(waterReminderCard);
        });

        // Configuration de la carte pas
        stepsCard.setOnClickListener(v -> {
            addSteps(1000); // Ajouter 1000 pas à chaque clic
            pulseAnimation(stepsCard);
        });

        updateCounters();
    }

    private void startWaterReminder() {
        waterReminderRunnable = new Runnable() {
            @Override
            public void run() {
                showWaterReminder();
                handler.postDelayed(this, 60 * 60 * 1000); // Toutes les 60 minutes pour la perte de poids
            }
        };
        handler.postDelayed(waterReminderRunnable, 60 * 60 * 1000);
    }

    private void showWaterReminder() {
        // Animation de pulsation pour rappel d'eau
        ObjectAnimator animator = ObjectAnimator.ofFloat(waterReminderCard, "alpha", 1f, 0.3f, 1f);
        animator.setDuration(1000);
        animator.setRepeatCount(3);

        // Changement de couleur temporaire
        waterReminderCard.setCardBackgroundColor(ContextCompat.getColor(this, R.color.accent_blue));
        handler.postDelayed(() -> {
            waterReminderCard.setCardBackgroundColor(ContextCompat.getColor(this, R.color.card_background));
        }, 3000);

        animator.start();
        showEncouragementMessage("💧 Pensez à boire ! L'hydratation booste le métabolisme");
    }

    private void drinkWater() {
        currentWaterCount++;
        updateCounters();
        successPulse(waterIcon);

        // Messages d'encouragement selon la progression
        switch (currentWaterCount) {
            case 3:
                showEncouragementMessage("💪 Excellent ! Continuez à bien vous hydrater");
                break;
            case 6:
                showEncouragementMessage("🔥 À mi-parcours ! Votre métabolisme vous remercie");
                break;
            case 10:
                showEncouragementMessage("🏆 Objectif atteint ! Hydratation parfaite !");
                break;
        }
    }

    private void addSteps(int steps) {
        stepsCount += steps;
        updateCounters();
        successPulse(stepsIcon);

        // Messages motivants pour les pas
        if (stepsCount >= 5000 && stepsCount < 7500) {
            showEncouragementMessage("🚶 À mi-chemin ! Chaque pas brûle des calories");
        } else if (stepsCount >= 10000) {
            showEncouragementMessage("🎯 10 000 pas ! Bravo, vous brûlez efficacement !");
        }
    }

    private void updateCounters() {
        tvWaterCount.setText(currentWaterCount + "/10");
        tvStepsCount.setText(stepsCount + "/10000");

        // Mise à jour des barres de progression
        int waterProgress = (int) (currentWaterCount / 10.0f * 100);
        int stepsProgress = (int) (stepsCount / 10000.0f * 100);

        animateProgress(progressWater, Math.min(waterProgress, 100));
        animateProgress(progressSteps, Math.min(stepsProgress, 100));
    }

    private void animateMealCompletion(Meal meal) {
        // Animation visuelle pour repas terminé
        int position = mealsList.indexOf(meal);
        RecyclerView.ViewHolder holder = rvMeals.findViewHolderForAdapterPosition(position);

        if (holder != null && holder.itemView != null) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(holder.itemView, "scaleX", 1f, 1.05f, 1f);
            animator.setDuration(300);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.start();

            // Changement de couleur temporaire
            MaterialCardView cardView = holder.itemView.findViewById(R.id.mealCard);
            if (cardView != null) {
                cardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.success_green));
                handler.postDelayed(() -> {
                    cardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.card_background));
                }, 1000);
            }
        }

        // Messages motivants selon le repas
        if (meal.isCompleted()) {
            showEncouragementMessage("✅ " + meal.getName() + " validé ! Vous tenez le bon rythme");
        }
    }

    private void animateCounter(TextView textView, int targetValue) {
        ValueAnimator animator = ValueAnimator.ofInt(0, targetValue);
        animator.setDuration(1500);
        animator.addUpdateListener(animation -> {
            textView.setText(String.valueOf(animation.getAnimatedValue()));
        });
        animator.start();
    }

    private void animateProgress(LinearProgressIndicator progressBar, int targetProgress) {
        ValueAnimator animator = ValueAnimator.ofInt(0, targetProgress);
        animator.setDuration(1200);
        animator.addUpdateListener(animation -> {
            progressBar.setProgress((Integer) animation.getAnimatedValue());
        });
        animator.start();
    }

    private void animateProgress(CircularProgressIndicator progressBar, int targetProgress) {
        ValueAnimator animator = ValueAnimator.ofInt(0, targetProgress);
        animator.setDuration(1200);
        animator.addUpdateListener(animation -> {
            progressBar.setProgress((Integer) animation.getAnimatedValue());
        });
        animator.start();
    }

    private void pulseAnimation(View view) {
        ObjectAnimator scaleUpX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.1f);
        ObjectAnimator scaleUpY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.1f);
        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(view, "scaleX", 1.1f, 1f);
        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(view, "scaleY", 1.1f, 1f);

        scaleUpX.setDuration(150);
        scaleUpY.setDuration(150);
        scaleDownX.setDuration(150);
        scaleDownY.setDuration(150);

        scaleUpX.addListener(new android.animation.AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                scaleDownX.start();
                scaleDownY.start();
            }
        });
        scaleUpX.start();
        scaleUpY.start();
    }

    private void successPulse(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotation", 0f, 360f);
        animator.setDuration(500);
        animator.start();
    }

    private void animateEntry() {
        // Animation d'entrée en cascade pour les éléments
        View[] views = {headerGradient, nutritionCountersCard, remindersContainer, rvMeals};

        for (int i = 0; i < views.length; i++) {
            View view = views[i];
            view.setAlpha(0f);
            view.setTranslationY(100f);

            view.animate()
                    .alpha(1f)
                    .translationY(0f)
                    .setDuration(600)
                    .setStartDelay(i * 150L)
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .start();
        }
    }

    private void showEncouragementMessage(String message) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT);
        snackbar.setBackgroundTint(ContextCompat.getColor(this, R.color.accent_red));
        snackbar.show();
    }

    private void addCustomMeal() {
        // Dialog pour ajouter un repas personnalisé
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("🍽️ Ajouter un repas");

        // Layout pour le dialog
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 20, 50, 20);

        // Champ nom du repas
        android.widget.EditText nameInput = new android.widget.EditText(this);
        nameInput.setHint("Nom du repas");
        layout.addView(nameInput);

        // Champ heure
        android.widget.EditText timeInput = new android.widget.EditText(this);
        timeInput.setHint("Heure (ex: 15:30)");
        layout.addView(timeInput);

        // Champ aliments
        android.widget.EditText foodsInput = new android.widget.EditText(this);
        foodsInput.setHint("Aliments (séparés par des virgules)");
        layout.addView(foodsInput);

        // Champ calories
        android.widget.EditText caloriesInput = new android.widget.EditText(this);
        caloriesInput.setHint("Calories");
        caloriesInput.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        layout.addView(caloriesInput);

        builder.setView(layout);

        builder.setPositiveButton("Ajouter", (dialog, which) -> {
            String name = nameInput.getText().toString().trim();
            String time = timeInput.getText().toString().trim();
            String foods = foodsInput.getText().toString().trim();
            String caloriesStr = caloriesInput.getText().toString().trim();

            if (!name.isEmpty() && !time.isEmpty() && !foods.isEmpty() && !caloriesStr.isEmpty()) {
                try {
                    int calories = Integer.parseInt(caloriesStr);
                    List<String> foodList = Arrays.asList(foods.split(","));

                    // Nettoyage des espaces
                    foodList = foodList.stream()
                            .map(String::trim)
                            .collect(java.util.stream.Collectors.toList());

                    Meal newMeal = new Meal(name, time, foodList, calories, "🍽️",
                            "Repas personnalisé - Restez dans vos objectifs !");

                    mealsList.add(newMeal);
                    mealsAdapter.notifyItemInserted(mealsList.size() - 1);
                    updateNutritionCounters();

                    showEncouragementMessage("✅ Repas ajouté ! Continuez sur cette lancée");

                } catch (NumberFormatException e) {
                    showEncouragementMessage("❌ Veuillez entrer un nombre valide pour les calories");
                }
            } else {
                showEncouragementMessage("❌ Veuillez remplir tous les champs");
            }
        });

        builder.setNegativeButton("Annuler", null);
        builder.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null && waterReminderRunnable != null) {
            handler.removeCallbacks(waterReminderRunnable);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Sauvegarder les données utilisateur (eau, pas)
        saveUserProgress();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Restaurer les données utilisateur
        loadUserProgress();
    }

    private void saveUserProgress() {
        android.content.SharedPreferences prefs = getSharedPreferences("NutritionPP", MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("water_count", currentWaterCount);
        editor.putInt("steps_count", stepsCount);
        editor.putLong("last_save_date", System.currentTimeMillis());
        editor.apply();
    }

    private void loadUserProgress() {
        android.content.SharedPreferences prefs = getSharedPreferences("NutritionPP", MODE_PRIVATE);

        // Vérifier si c'est un nouveau jour
        long lastSaveDate = prefs.getLong("last_save_date", 0);
        long currentDate = System.currentTimeMillis();
        long dayDiff = (currentDate - lastSaveDate) / (1000 * 60 * 60 * 24);

        if (dayDiff >= 1) {
            // Nouveau jour = reset des compteurs
            currentWaterCount = 0;
            stepsCount = 0;
        } else {
            // Même jour = restaurer les valeurs
            currentWaterCount = prefs.getInt("water_count", 0);
            stepsCount = prefs.getInt("steps_count", 0);
        }

        updateCounters();
    }

    // Classe interne pour les repas
    public static class Meal {
        private String name;
        private String time;
        private List<String> foods;
        private int calories;
        private String emoji;
        private String tip;
        private boolean completed = false;

        public Meal(String name, String time, List<String> foods, int calories, String emoji, String tip) {
            this.name = name;
            this.time = time;
            this.foods = foods;
            this.calories = calories;
            this.emoji = emoji;
            this.tip = tip;
        }

        // Getters
        public String getName() { return name; }
        public String getTime() { return time; }
        public List<String> getFoods() { return foods; }
        public int getCalories() { return calories; }
        public String getEmoji() { return emoji; }
        public String getTip() { return tip; }
        public boolean isCompleted() { return completed; }

        // Setters
        public void setCompleted(boolean completed) { this.completed = completed; }
    }

    // Interface pour le callback des repas
    public interface MealCompletionCallback {
        void onMealCompleted(Meal meal);
    }
}