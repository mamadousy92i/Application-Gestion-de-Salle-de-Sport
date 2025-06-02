package com.example.salledesport.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.salledesport.R;
import com.example.salledesport.model.Subscription;

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

        public SubscriptionViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.subscriptionTitle);
            status = itemView.findViewById(R.id.subscriptionStatus);
            dates = itemView.findViewById(R.id.subscriptionDates);
            btnRenew = itemView.findViewById(R.id.btnRenew);
            btnCancel = itemView.findViewById(R.id.btnCancel);
        }
    }
}