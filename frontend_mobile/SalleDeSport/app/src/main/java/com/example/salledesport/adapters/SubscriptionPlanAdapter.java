package com.example.salledesport.adapters;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.salledesport.R;
import com.example.salledesport.api.RetrofitClient;
import com.example.salledesport.model.SubscriptionPlan;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;

import java.util.List;
import java.util.Random;

public class SubscriptionPlanAdapter extends RecyclerView.Adapter<SubscriptionPlanAdapter.PlanViewHolder> {

    private List<SubscriptionPlan> plans;
    private OnPlanClickListener listener;

    public interface OnPlanClickListener {
        void onPlanClick(SubscriptionPlan plan);
    }

    public SubscriptionPlanAdapter(List<SubscriptionPlan> plans, OnPlanClickListener listener) {
        this.plans = plans;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subscription_plan, parent, false);
        return new PlanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanViewHolder holder, int position) {
        SubscriptionPlan plan = plans.get(position);

        // Binding des données de base
        holder.planName.setText(plan.getName());
        holder.planPrice.setText(plan.getPrice() + "€");
        holder.planDescription.setText(plan.getDescription());

        // Construire les features
        StringBuilder features = new StringBuilder();
        features.append(plan.getMaxSessionsPerWeek()).append(" séances • ");

        if (plan.isIncludesCoach()) {
            features.append("Coach • ");
        }

        if (plan.isIncludesGroupClasses()) {
            features.append("Cours collectifs • ");
        }

        if (plan.isIncludesPool()) {
            features.append("Piscine");
        }

        // Nettoyer la fin de la chaîne
        String featuresText = features.toString();
        if (featuresText.endsWith(" • ")) {
            featuresText = featuresText.substring(0, featuresText.length() - 3);
        }
        holder.planFeatures.setText(featuresText);

        // Afficher la durée et membres (données simulées pour l'exemple)
        if (holder.planDuration != null) {
            holder.planDuration.setText(plan.getDurationMonths() + " mois");
        }

        if (holder.planMembers != null) {
            // Simuler un nombre de membres basé sur le prix (plus cher = plus populaire)
            int members = (int) (plan.getPrice() * 50 + new Random().nextInt(500));
            if (members > 1000) {
                holder.planMembers.setText((members / 1000) + "." + ((members % 1000) / 100) + "k+");
            } else {
                holder.planMembers.setText(members + "");
            }
        }

        // Rating simulé basé sur la qualité du plan
        if (holder.planRating != null) {
            float rating = (float) (4.2f + (plan.getPrice() / 100f)); // Plus cher = meilleur rating
            if (rating > 5.0f) rating = 5.0f;
            holder.planRating.setText(String.format("%.1f", rating));
        }

        // Gérer les badges
        handleBadges(holder, plan, position);

        // Gérer la barre de progression (exemple : 30% des plans ont une progression active)
        handleProgressBar(holder, plan, position);

        // Prix original (simulation d'une réduction)
        handleOriginalPrice(holder, plan);

        // Charger l'image
        loadPlanImage(holder, plan);

        // Configurer les clics
        setupClickListeners(holder, plan);
    }

    private void handleBadges(@NonNull PlanViewHolder holder, SubscriptionPlan plan, int position) {
        // Badge Premium (pour les plans chers)
        if (holder.premiumBadge != null) {
            if (plan.getPrice() > 40) {
                holder.premiumBadge.setVisibility(View.VISIBLE);
            } else {
                holder.premiumBadge.setVisibility(View.GONE);
            }
        }

        // Badge Populaire (pour certains plans)
        if (holder.popularityChip != null) {
            if (position == 0 || plan.getPrice() > 30) { // Premier plan ou plan cher
                holder.popularityChip.setVisibility(View.VISIBLE);
            } else {
                holder.popularityChip.setVisibility(View.GONE);
            }
        }

        // Icône trending
        if (holder.trendingIcon != null) {
            if (position < 2) { // Les 2 premiers plans
                holder.trendingIcon.setVisibility(View.VISIBLE);
            } else {
                holder.trendingIcon.setVisibility(View.GONE);
            }
        }
    }

    private void handleProgressBar(@NonNull PlanViewHolder holder, SubscriptionPlan plan, int position) {
        // Simuler qu'1 plan sur 3 a une progression active
        boolean hasActiveSubscription = (position % 3 == 0);

        if (holder.subscriptionProgress != null && holder.progressLabel != null) {
            if (hasActiveSubscription) {
                // Afficher la barre avec un progrès simulé
                int progress = 20 + new Random().nextInt(60); // Entre 20% et 80%

                holder.subscriptionProgress.setVisibility(View.VISIBLE);
                holder.subscriptionProgress.setProgress(progress);

                holder.progressLabel.setVisibility(View.VISIBLE);

                // Calculer le temps restant basé sur le progrès
                int totalMonths = plan.getDurationMonths();
                int remainingMonths = (int) ((100 - progress) * totalMonths / 100.0);

                holder.progressLabel.setText(progress + "% terminé • " + remainingMonths + " mois restants");
            } else {
                holder.subscriptionProgress.setVisibility(View.GONE);
                holder.progressLabel.setVisibility(View.GONE);
            }
        }
    }

    private void handleOriginalPrice(@NonNull PlanViewHolder holder, SubscriptionPlan plan) {
        if (holder.originalPrice != null) {
            // Simuler une réduction pour les plans premium
            if (plan.getPrice() > 35) {
                float originalPrice = (float) (plan.getPrice() * 1.3f); // 30% de réduction
                holder.originalPrice.setText(String.format("%.2f€", originalPrice));
                holder.originalPrice.setVisibility(View.VISIBLE);
            } else {
                holder.originalPrice.setVisibility(View.GONE);
            }
        }
    }

    private void loadPlanImage(@NonNull PlanViewHolder holder, SubscriptionPlan plan) {
        if (plan.getImage() != null && !plan.getImage().isEmpty()) {
            String imageUrl;
            String baseUrl = RetrofitClient.getBaseUrl();

            if (plan.getImage().startsWith("http")) {
                imageUrl = plan.getImage();
            } else {
                imageUrl = baseUrl + plan.getImage();
            }

            Log.d("ImageDebug", "Loading image from URL: " + imageUrl);

            Glide.with(holder.itemView.getContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                    @NonNull Target<Drawable> target, boolean isFirstResource) {
                            Log.e("GLIDE", "Erreur de chargement", e);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(@NonNull Drawable resource, @NonNull Object model,
                                                       Target<Drawable> target, @NonNull DataSource dataSource,
                                                       boolean isFirstResource) {
                            Log.d("GLIDE", "Image chargée avec succès");
                            return false;
                        }
                    })
                    .into(holder.planImage);
        } else {
            holder.planImage.setImageResource(R.drawable.placeholder);
        }
    }

    private void setupClickListeners(@NonNull PlanViewHolder holder, SubscriptionPlan plan) {
        // Clic sur toute la carte
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPlanClick(plan);
            }
        });

        // Clic sur le bouton détails
        holder.btnViewDetails.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPlanClick(plan);
            }
        });
    }

    @Override
    public int getItemCount() {
        return plans.size();
    }

    static class PlanViewHolder extends RecyclerView.ViewHolder {
        // Éléments de base (obligatoires)
        TextView planName, planPrice, planDescription, planFeatures;
        ImageView planImage;
        MaterialButton btnViewDetails;

        // Éléments premium (optionnels - peuvent être null)
        Chip premiumBadge, popularityChip;
        ImageView trendingIcon;
        TextView planRating, planDuration, planMembers, originalPrice;
        ProgressBar subscriptionProgress;
        TextView progressLabel;

        public PlanViewHolder(@NonNull View itemView) {
            super(itemView);

            // Éléments obligatoires
            planName = itemView.findViewById(R.id.planName);
            planPrice = itemView.findViewById(R.id.planPrice);
            planDescription = itemView.findViewById(R.id.planDescription);
            planFeatures = itemView.findViewById(R.id.planFeatures);
            planImage = itemView.findViewById(R.id.planImage);
            btnViewDetails = itemView.findViewById(R.id.btnViewDetails);

            // Éléments premium (optionnels)
            try {
                premiumBadge = itemView.findViewById(R.id.premiumBadge);
                popularityChip = itemView.findViewById(R.id.popularityChip);
                trendingIcon = itemView.findViewById(R.id.trendingIcon);
                planRating = itemView.findViewById(R.id.planRating);
                planDuration = itemView.findViewById(R.id.planDuration);
                planMembers = itemView.findViewById(R.id.planMembers);
                originalPrice = itemView.findViewById(R.id.originalPrice);
                subscriptionProgress = itemView.findViewById(R.id.subscriptionProgress);
                progressLabel = itemView.findViewById(R.id.progressLabel);
            } catch (Exception e) {
                // Si certains éléments n'existent pas dans le layout, ce n'est pas grave
                Log.d("Adapter", "Certains éléments premium ne sont pas présents dans le layout");
            }
        }
    }
}