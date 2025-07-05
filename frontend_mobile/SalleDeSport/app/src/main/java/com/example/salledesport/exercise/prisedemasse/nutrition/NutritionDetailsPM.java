package com.example.salledesport.exercise.prisedemasse.nutrition;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class NutritionDetailsPM extends AppCompatActivity {

    // Vues
    private LinearLayout headerGradient;
    private MaterialCardView nutritionCountersCard, waterReminderCard, fruitVegCard;
    private LinearLayout remindersContainer;
    private RecyclerView rvMeals;
    private TextView tvCaloriesCount, tvProteinCount, tvCarbsCount;
    private TextView tvWaterCount, tvFruitVegCount;
    private TextView waterIcon, fruitVegIcon;
    private CircularProgressIndicator progressCalories;
    private LinearProgressIndicator progressWater, progressFruitVeg;
    private MaterialButton btnAddMeal;
    private FloatingActionButton btnWaterReminder;

    // Variables
    private MealsAdapter mealsAdapter;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable waterReminderRunnable;
    private int fruitVegCount = 0;
    private List<Meal> mealsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition_details_pm);

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
            Intent intent = new Intent(NutritionDetailsPM.this, ProgramCompleteActivity.class);
            startActivity(intent);
        });
    }

    private void initializeMealsData() {
        mealsList = new ArrayList<>();

        mealsList.add(new Meal("Petit-déjeuner", "07:00",
                Arrays.asList("3 œufs brouillés", "2 tranches pain complet", "1 avocat", "1 banane"),
                850, "🍳",
                "Commencez fort ! Les protéines et glucides complexes donnent l'énergie pour la journée"));

        mealsList.add(new Meal("Collation matin", "10:00",
                Arrays.asList("Smoothie protéiné", "Flocons d'avoine", "Fruits rouges"),
                420, "🥤",
                "Boost énergétique pré-entraînement ! Les glucides rapides + protéines = combo gagnant"));

        mealsList.add(new Meal("Déjeuner", "13:00",
                Arrays.asList("200g poulet grillé", "150g riz basmati", "Légumes verts", "Huile d'olive"),
                720, "🍗",
                "Le repas roi ! Protéines complètes + glucides pour alimenter vos muscles"));

        mealsList.add(new Meal("Collation post-training", "16:30",
                Arrays.asList("Whey protein", "1 banane", "Amandes"),
                380, "💪",
                "Fenêtre anabolique ! Dans les 30min post-entraînement pour optimiser la récupération"));

        mealsList.add(new Meal("Dîner", "19:30",
                Arrays.asList("200g saumon", "Patates douces", "Brocolis", "Salade verte"),
                680, "🐟",
                "Oméga-3 pour la récupération. Glucides lents pour tenir toute la nuit"));

        mealsList.add(new Meal("Collation soir", "22:00",
                Arrays.asList("Fromage blanc", "Noix", "Miel"),
                290, "🥛",
                "Caséine pour nourrir vos muscles pendant le sommeil. Patience, ça travaille la nuit !"));
    }

    private void initializeViews() {
        headerGradient = findViewById(R.id.headerGradient);
        nutritionCountersCard = findViewById(R.id.nutritionCountersCard);
        waterReminderCard = findViewById(R.id.waterReminderCard);
        fruitVegCard = findViewById(R.id.fruitVegCard);
        remindersContainer = findViewById(R.id.remindersContainer);
        rvMeals = findViewById(R.id.rvMeals);

        tvCaloriesCount = findViewById(R.id.tvCaloriesCount);
        tvProteinCount = findViewById(R.id.tvProteinCount);
        tvCarbsCount = findViewById(R.id.tvCarbsCount);
        tvWaterCount = findViewById(R.id.tvWaterCount);
        tvFruitVegCount = findViewById(R.id.tvFruitVegCount);

        waterIcon = findViewById(R.id.waterIcon);
        fruitVegIcon = findViewById(R.id.fruitVegIcon);

        progressCalories = findViewById(R.id.progressCalories);
        progressWater = findViewById(R.id.progressWater);
        progressFruitVeg = findViewById(R.id.progressFruitVeg);

        btnAddMeal = findViewById(R.id.btnAddMeal);
        btnWaterReminder = findViewById(R.id.btnWaterReminder);
    }

    private void setupUI() {
        // Configuration du header avec gradient
        headerGradient.setBackground(ContextCompat.getDrawable(this, R.drawable.gradient_nutrition_header));

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

        int proteinGrams = (int) (totalCalories * 0.25 / 4); // 25% des calories en protéines
        int carbsGrams = (int) (totalCalories * 0.45 / 4); // 45% en glucides

        animateCounter(tvCaloriesCount, totalCalories);
        animateCounter(tvProteinCount, proteinGrams);
        animateCounter(tvCarbsCount, carbsGrams);

        // Animation de la barre de progression des calories
        int progressTarget = (int) ((totalCalories / 3500.0f) * 100);
        animateProgress(progressCalories, progressTarget);
    }

    private void setupReminderCards() {
        // Configuration de la carte hydratation
        waterReminderCard.setOnClickListener(v -> {
            drinkWater();
            pulseAnimation(waterReminderCard);
        });

        // Configuration de la carte fruits & légumes
        fruitVegCard.setOnClickListener(v -> {
            incrementFruitVeg();
            pulseAnimation(fruitVegCard);
        });

        updateFruitVegCounter();
    }

    private void startWaterReminder() {
        waterReminderRunnable = new Runnable() {
            @Override
            public void run() {
                showWaterReminder();
                handler.postDelayed(this, 90 * 60 * 1000); // Toutes les 90 minutes
            }
        };
        handler.postDelayed(waterReminderRunnable, 90 * 60 * 1000);
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
    }

    private void drinkWater() {
        String currentWaterText = tvWaterCount.getText().toString();
        int currentWater = 0;
        try {
            currentWater = Integer.parseInt(currentWaterText.split("/")[0]);
        } catch (Exception e) {
            currentWater = 0;
        }

        int newWaterCount = currentWater + 1;
        tvWaterCount.setText(newWaterCount + "/8");

        // Animation de succès
        successPulse(waterIcon);

        // Mise à jour de la barre de progression
        int progressValue = (int) (newWaterCount / 8.0f * 100);
        animateProgress(progressWater, progressValue);
    }

    private void incrementFruitVeg() {
        fruitVegCount++;
        updateFruitVegCounter();
        successPulse(fruitVegIcon);
    }

    private void updateFruitVegCounter() {
        tvFruitVegCount.setText(fruitVegCount + "/5");
        int progressValue = (int) (fruitVegCount / 5.0f * 100);
        animateProgress(progressFruitVeg, progressValue);

        // Message d'encouragement
        switch (fruitVegCount) {
            case 1:
                showEncouragementMessage("Excellent début ! 🌱");
                break;
            case 3:
                showEncouragementMessage("À mi-chemin ! Continuez ! 🥕");
                break;
            case 5:
                showEncouragementMessage("Objectif atteint ! Bravo ! 🎉");
                break;
        }
    }

    private void animateMealCompletion(Meal meal) {
        // Animation visuelle pour repas terminé
        int position = mealsList.indexOf(meal);
        View mealView = rvMeals.findViewHolderForAdapterPosition(position).itemView;

        if (mealView != null) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(mealView, "scaleX", 1f, 1.05f, 1f);
            animator.setDuration(300);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.start();

            // Changement de couleur temporaire
            MaterialCardView cardView = mealView.findViewById(R.id.mealCard);
            cardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.success_green));
            handler.postDelayed(() -> {
                cardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.card_background));
            }, 1000);
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
        ObjectAnimator scaleUp = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.1f);
        ObjectAnimator scaleDown = ObjectAnimator.ofFloat(view, "scaleX", 1.1f, 1f);
        scaleUp.setDuration(150);
        scaleDown.setDuration(150);

        scaleUp.addListener(new android.animation.AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                scaleDown.start();
            }
        });
        scaleUp.start();
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
        snackbar.setBackgroundTint(ContextCompat.getColor(this, R.color.success_green));
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
        if (waterReminderRunnable != null) {
            handler.removeCallbacks(waterReminderRunnable);
        }
    }
}

// Classe Meal
class Meal {
    private String name;
    private String time;
    private List<String> ingredients;
    private int calories;
    private String emoji;
    private String tip;
    private boolean isCompleted;

    public Meal(String name, String time, List<String> ingredients, int calories, String emoji, String tip) {
        this.name = name;
        this.time = time;
        this.ingredients = ingredients;
        this.calories = calories;
        this.emoji = emoji;
        this.tip = tip;
        this.isCompleted = false;
    }

    // Getters et Setters
    public String getName() { return name; }
    public String getTime() { return time; }
    public List<String> getIngredients() { return ingredients; }
    public int getCalories() { return calories; }
    public String getEmoji() { return emoji; }
    public String getTip() { return tip; }
    public boolean isCompleted() { return isCompleted; }
    public void setCompleted(boolean completed) { isCompleted = completed; }
}

// Adapter pour la RecyclerView des repas
class MealsAdapter extends RecyclerView.Adapter<MealsAdapter.MealViewHolder> {

    private List<Meal> meals;
    private OnMealClickListener onMealClickListener;

    public interface OnMealClickListener {
        void onMealClick(Meal meal);
    }

    public MealsAdapter(List<Meal> meals, OnMealClickListener listener) {
        this.meals = meals;
        this.onMealClickListener = listener;
    }

    public static class MealViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView mealCard;
        TextView mealEmoji, mealName, mealTime, mealCalories, mealIngredients, mealTip;
        MaterialButton checkButton;

        public MealViewHolder(View itemView) {
            super(itemView);
            mealCard = itemView.findViewById(R.id.mealCard);
            mealEmoji = itemView.findViewById(R.id.tvMealEmoji);
            mealName = itemView.findViewById(R.id.tvMealName);
            mealTime = itemView.findViewById(R.id.tvMealTime);
            mealCalories = itemView.findViewById(R.id.tvMealCalories);
            mealIngredients = itemView.findViewById(R.id.tvMealIngredients);
            mealTip = itemView.findViewById(R.id.tvMealTip);
            checkButton = itemView.findViewById(R.id.btnCheckMeal);
        }
    }

    @Override
    public MealViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_meal_card, parent, false);
        return new MealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MealViewHolder holder, int position) {
        Meal meal = meals.get(position);

        holder.mealEmoji.setText(meal.getEmoji());
        holder.mealName.setText(meal.getName());
        holder.mealTime.setText(meal.getTime());
        holder.mealCalories.setText(meal.getCalories() + " kcal");

        // Formatage des ingrédients
        StringBuilder ingredientsText = new StringBuilder();
        for (int i = 0; i < meal.getIngredients().size(); i++) {
            ingredientsText.append(meal.getIngredients().get(i));
            if (i < meal.getIngredients().size() - 1) {
                ingredientsText.append(" • ");
            }
        }
        holder.mealIngredients.setText(ingredientsText.toString());
        holder.mealTip.setText("💡 " + meal.getTip());

        holder.checkButton.setOnClickListener(v -> {
            meal.setCompleted(!meal.isCompleted());
            onMealClickListener.onMealClick(meal);
            notifyItemChanged(position);
        });

        // Style différent selon si le repas est terminé
        if (meal.isCompleted()) {
            holder.checkButton.setText("✓ Terminé");
            holder.checkButton.setBackgroundColor(
                    ContextCompat.getColor(holder.itemView.getContext(), R.color.success_green)
            );
        } else {
            holder.checkButton.setText("Marquer comme fait");
            holder.checkButton.setBackgroundColor(
                    ContextCompat.getColor(holder.itemView.getContext(), R.color.primary_blue)
            );
        }
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }
}