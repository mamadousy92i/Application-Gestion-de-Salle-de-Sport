package com.example.salledesport.adapters;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.salledesport.R;
import com.example.salledesport.api.RetrofitClient;
import com.example.salledesport.model.SubscriptionPlan;

import java.util.List;

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

        holder.tvName.setText(plan.getName());
        holder.tvPrice.setText(plan.getPrice() + " €/mois");
        holder.tvDuration.setText("Durée: " + plan.getDurationMonths() + " mois");

        // Afficher les options
        StringBuilder features = new StringBuilder();
        features.append("• ").append(plan.getMaxSessionsPerWeek()).append(" séances/semaine\n");

        if (plan.isIncludesCoach()) {
            features.append("• Coach personnel inclus\n");
        }

        if (plan.isIncludesGroupClasses()) {
            features.append("• Cours collectifs inclus\n");
        }

        if (plan.isIncludesPool()) {
            features.append("• Accès à la piscine");
        }

        holder.tvFeatures.setText(features.toString());

        // Charger l'image si disponible
        if (plan.getImage() != null && !plan.getImage().isEmpty()) {
            String imageUrl;
            String baseUrl = RetrofitClient.getBaseUrl();

            // Vérifier si l'URL de l'image contient déjà l'URL de base
            if (plan.getImage().startsWith("http")) {
                imageUrl = plan.getImage();
            } else {
                imageUrl = baseUrl + plan.getImage();
            }

            android.util.Log.d("ImageDebug", "Loading image from URL: " + imageUrl);

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
                            Log.e("GLIDE", "Erreur de chargement" );

                            return false;
                        }
                    })
                    .into(holder.ivPlan);
        } else {
            holder.ivPlan.setImageResource(R.drawable.placeholder);
        }

        // Configurer le clic sur la carte
        holder.cardView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPlanClick(plan);
            }
        });

        // Configurer le clic sur le bouton Détails
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
        CardView cardView;
        TextView tvName, tvPrice, tvDuration, tvFeatures;
        ImageView ivPlan;
        Button btnViewDetails;

        public PlanViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView; // Le CardView est l'élément racine
            tvName = itemView.findViewById(R.id.planName);
            tvPrice = itemView.findViewById(R.id.planPrice);
            tvDuration = itemView.findViewById(R.id.planDescription); // Utiliser planDescription pour la durée
            tvFeatures = itemView.findViewById(R.id.planFeatures);
            ivPlan = itemView.findViewById(R.id.planImage);
            btnViewDetails = itemView.findViewById(R.id.btnViewDetails);
        }
    }
}
