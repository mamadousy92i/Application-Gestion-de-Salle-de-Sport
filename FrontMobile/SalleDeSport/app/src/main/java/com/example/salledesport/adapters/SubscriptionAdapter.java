package com.example.salledesport.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.example.salledesport.R;
import com.example.salledesport.model.Subscription;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.salledesport.api.RetrofitClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import java.util.Locale;

public class SubscriptionAdapter extends RecyclerView.Adapter<SubscriptionAdapter.SubscriptionViewHolder> {

    private List<Subscription> subscriptions;
    private OnSubscriptionActionListener listener;

    public interface OnSubscriptionActionListener {
        void onRenewClick(Subscription subscription);
        void onCancelClick(Subscription subscription);
    }

    public SubscriptionAdapter(List<Subscription> subscriptions, OnSubscriptionActionListener listener) {
        this.subscriptions = subscriptions;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SubscriptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subscription, parent, false);
        return new SubscriptionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubscriptionViewHolder holder, int position) {
        Subscription sub = subscriptions.get(position);
        holder.title.setText(sub.getPlan().getName());
        holder.status.setText("Statut : " + getStatusText(sub.getStatus()));
        holder.dates.setText("Du " + sub.getStartDate() + " au " + sub.getEndDate());

        // Charger l'image du plan d'abonnement
        if (sub.getPlan() != null && sub.getPlan().getImage() != null && !sub.getPlan().getImage().isEmpty()) {
            String imageUrl;

            // Vérifier si l'URL de l'image contient déjà l'URL de base
            if (sub.getPlan().getImage().startsWith("http")) {
                imageUrl = sub.getPlan().getImage();
            } else {
                imageUrl = RetrofitClient.getBaseUrl() + sub.getPlan().getImage();
            }

            Glide.with(holder.itemView.getContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .listener(new RequestListener<android.graphics.drawable.Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                    Target<android.graphics.drawable.Drawable> target, boolean isFirstResource) {
                            android.util.Log.e("GLIDE", "Erreur de chargement dans SubscriptionAdapter", e);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(android.graphics.drawable.Drawable resource, Object model,
                                                       Target<android.graphics.drawable.Drawable> target, DataSource dataSource,
                                                       boolean isFirstResource) {
                            android.util.Log.d("GLIDE", "Image chargée avec succès dans SubscriptionAdapter");
                            return false;
                        }
                    })
                    .into(holder.subscriptionImage);
        } else {
            holder.subscriptionImage.setImageResource(R.drawable.placeholder);
        }

        // Vérifier si l'abonnement est expiré
        boolean isExpired = isSubscriptionExpired(sub.getEndDate());

        // Afficher ou masquer les boutons en fonction du statut et de l'expiration
        if ("cancelled".equals(sub.getStatus()) || isExpired) {
            holder.btnRenew.setVisibility(View.VISIBLE);
            holder.btnCancel.setVisibility(View.GONE); // Pas besoin d'annuler un abonnement déjà annulé ou expiré
        } else if ("active".equals(sub.getStatus())) {
            holder.btnRenew.setVisibility(View.GONE); // On ne peut pas renouveler un abonnement actif
            holder.btnCancel.setVisibility(View.VISIBLE);
        } else if ("pending".equals(sub.getStatus())) {
            holder.btnRenew.setVisibility(View.GONE);
            holder.btnCancel.setVisibility(View.VISIBLE);
        } else {
            holder.btnRenew.setVisibility(View.GONE);
            holder.btnCancel.setVisibility(View.GONE);
        }

        // Configurer les listeners des boutons
        holder.btnRenew.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRenewClick(sub);
            }
        });

        holder.btnCancel.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCancelClick(sub);
            }
        });
    }

    @Override
    public int getItemCount() {
        return subscriptions.size();
    }

    // Convertir les codes de statut en texte lisible
    private String getStatusText(String statusCode) {
        switch (statusCode) {
            case "active":
                return "Actif";
            case "expired":
                return "Expiré";
            case "cancelled":
                return "Annulé";
            case "pending":
                return "En attente";
            default:
                return statusCode;
        }
    }

    // Vérifier si un abonnement est expiré
    private boolean isSubscriptionExpired(String endDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date endDateParsed = sdf.parse(endDate);
            Date currentDate = new Date(); // Date actuelle (aujourd'hui : 02/06/2025)
            return endDateParsed != null && endDateParsed.before(currentDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return false; // En cas d'erreur de parsing, on assume qu'il n'est pas expiré
        }
    }

    static class SubscriptionViewHolder extends RecyclerView.ViewHolder {
        TextView title, status, dates;
        Button btnRenew, btnCancel;
        ImageView subscriptionImage;

        public SubscriptionViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.subscriptionTitle);
            status = itemView.findViewById(R.id.subscriptionStatus);
            dates = itemView.findViewById(R.id.subscriptionDates);
            btnRenew = itemView.findViewById(R.id.btnRenew);
            btnCancel = itemView.findViewById(R.id.btnCancel);
            subscriptionImage = itemView.findViewById(R.id.subscriptionImage);
        }
    }
}