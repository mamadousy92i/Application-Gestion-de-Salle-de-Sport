package com.example.salledesport.exercise.pertedepoids.nutrition;

import android.animation.ObjectAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.salledesport.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import java.util.List;

public class MealsAdapter extends RecyclerView.Adapter<MealsAdapter.MealViewHolder> {

    private List<NutritionDetailsPP.Meal> meals;
    private NutritionDetailsPP.MealCompletionCallback callback;

    public MealsAdapter(List<NutritionDetailsPP.Meal> meals, NutritionDetailsPP.MealCompletionCallback callback) {
        this.meals = meals;
        this.callback = callback;
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_meal_pp, parent, false);
        return new MealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        NutritionDetailsPP.Meal meal = meals.get(position);
        holder.bind(meal, callback);
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    static class MealViewHolder extends RecyclerView.ViewHolder {
        private MaterialCardView mealCard;
        private TextView tvMealEmoji;
        private TextView tvMealName;
        private TextView tvMealTime;
        private TextView tvMealCalories;
        private LinearLayout foodsContainer;
        private TextView tvMealTip;
        private MaterialButton btnCompleteMeal;

        public MealViewHolder(@NonNull View itemView) {
            super(itemView);
            mealCard = itemView.findViewById(R.id.mealCard);
            tvMealEmoji = itemView.findViewById(R.id.tvMealEmoji);
            tvMealName = itemView.findViewById(R.id.tvMealName);
            tvMealTime = itemView.findViewById(R.id.tvMealTime);
            tvMealCalories = itemView.findViewById(R.id.tvMealCalories);
            foodsContainer = itemView.findViewById(R.id.foodsContainer);
            tvMealTip = itemView.findViewById(R.id.tvMealTip);
            btnCompleteMeal = itemView.findViewById(R.id.btnCompleteMeal);
        }

        public void bind(NutritionDetailsPP.Meal meal, NutritionDetailsPP.MealCompletionCallback callback) {
            tvMealEmoji.setText(meal.getEmoji());
            tvMealName.setText(meal.getName());
            tvMealTime.setText(meal.getTime());
            tvMealCalories.setText(meal.getCalories() + " kcal");
            tvMealTip.setText(meal.getTip());

            // Effacer les aliments précédents
            foodsContainer.removeAllViews();

            // Ajouter les aliments
            for (String food : meal.getFoods()) {
                TextView foodView = new TextView(itemView.getContext());
                foodView.setText("• " + food);
                foodView.setTextSize(12);
                foodView.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.text_secondary));
                foodView.setPadding(0, 4, 0, 4);
                foodsContainer.addView(foodView);
            }

            // Gestion de l'état du repas (complété ou non)
            updateMealState(meal);

            // Click listener pour compléter le repas
            btnCompleteMeal.setOnClickListener(v -> {
                meal.setCompleted(!meal.isCompleted());
                updateMealState(meal);

                if (callback != null) {
                    callback.onMealCompleted(meal);
                }

                // Animation du bouton
                ObjectAnimator.ofFloat(btnCompleteMeal, "rotation", 0f, 360f)
                        .setDuration(300)
                        .start();
            });
        }

        private void updateMealState(NutritionDetailsPP.Meal meal) {
            if (meal.isCompleted()) {
                btnCompleteMeal.setText("✅ Terminé");
                btnCompleteMeal.setBackgroundTintList(ContextCompat.getColorStateList(
                        itemView.getContext(), R.color.success_green));
                mealCard.setAlpha(0.7f);
            } else {
                btnCompleteMeal.setText("Valider");
                btnCompleteMeal.setBackgroundTintList(ContextCompat.getColorStateList(
                        itemView.getContext(), R.color.accent_red));
                mealCard.setAlpha(1.0f);
            }
        }
    }
}