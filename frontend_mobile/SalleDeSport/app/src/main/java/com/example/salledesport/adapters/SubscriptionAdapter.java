package com.example.salledesport.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
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
import java.util.Locale;
import java.util.concurrent.TimeUnit;

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

        // Titre du plan
        holder.title.setText(sub.getPlan().getName());

        // Statut avec émoji et couleurs
        setupStatus(holder, sub);

        // Dates formatées
        holder.dates.setText(formatDateRange(sub.getStartDate(), sub.getEndDate()));

        // Charger l'image
        loadSubscriptionImage(holder, sub);

        // Configuration des boutons selon le statut
        setupButtons(holder, sub);

        // Configuration de la barre de progression
        setupProgressBar(holder, sub);

        // Badge de statut (indicateur coloré)
        setupStatusIndicator(holder, sub);
    }

    private void setupStatus(SubscriptionViewHolder holder, Subscription sub) {
        String status = sub.getStatus();
        String statusText;
        int statusColor;

        switch (status.toLowerCase()) {
            case "active":
                statusText = "✅ Actif";
                statusColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.accent_green);
                break;
            case "expired":
                statusText = "⏰ Expiré";
                statusColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.accent_orange);
                break;
            case "cancelled":
                statusText = "🚫 Annulé";
                statusColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.error_red);
                break;
            case "pending":
                statusText = "⏳ En attente";
                statusColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.accent_orange);
                break;
            default:
                statusText = "❓ " + status;
                statusColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.text_secondary);
                break;
        }

        holder.status.setText(statusText);
        holder.status.setTextColor(statusColor);
    }

    private void setupStatusIndicator(SubscriptionViewHolder holder, Subscription sub) {
        int indicatorColor;

        switch (sub.getStatus().toLowerCase()) {
            case "active":
                indicatorColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.accent_green);
                break;
            case "expired":
                indicatorColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.accent_orange);
                break;
            case "cancelled":
                indicatorColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.error_red);
                break;
            case "pending":
                indicatorColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.accent_orange);
                break;
            default:
                indicatorColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.text_secondary);
                break;
        }

        if (holder.statusIndicator != null) {
            holder.statusIndicator.setBackgroundColor(indicatorColor);
        }
    }

    private void setupProgressBar(SubscriptionViewHolder holder, Subscription sub) {
        if (holder.subscriptionProgress == null || holder.timeRemaining == null) return;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date startDate = sdf.parse(sub.getStartDate());
            Date endDate = sdf.parse(sub.getEndDate());
            Date currentDate = new Date();

            if (startDate != null && endDate != null) {
                long totalDuration = endDate.getTime() - startDate.getTime();
                long elapsed = currentDate.getTime() - startDate.getTime();
                long remaining = endDate.getTime() - currentDate.getTime();

                // Calculer le pourcentage
                int progressPercent = (int) ((elapsed * 100) / totalDuration);
                progressPercent = Math.max(0, Math.min(100, progressPercent));

                holder.subscriptionProgress.setProgress(progressPercent);

                // Calculer les jours restants
                long daysRemaining = TimeUnit.MILLISECONDS.toDays(remaining);

                if (daysRemaining > 0) {
                    holder.timeRemaining.setText(daysRemaining + " jours");
                    holder.timeRemaining.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.white));
                } else {
                    holder.timeRemaining.setText("Expiré");
                    holder.timeRemaining.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.error_red));
                }

                // Couleur de la barre selon le temps restant
                int progressColor;
                if (daysRemaining > 30) {
                    progressColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.accent_green);
                } else if (daysRemaining > 7) {
                    progressColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.accent_orange);
                } else {
                    progressColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.error_red);
                }

                holder.subscriptionProgress.setProgressTintList(android.content.res.ColorStateList.valueOf(progressColor));
            }
        } catch (ParseException e) {
            // En cas d'erreur, masquer la barre de progression
            holder.subscriptionProgress.setVisibility(View.GONE);
            if (holder.timeRemaining != null) {
                holder.timeRemaining.setText("--");
            }
        }
    }

    private String formatDateRange(String startDate, String endDate) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM", Locale.FRENCH);
            SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());

            Date start = inputFormat.parse(startDate);
            Date end = inputFormat.parse(endDate);

            if (start != null && end != null) {
                String startFormatted = outputFormat.format(start);
                String endFormatted = outputFormat.format(end);
                String year = yearFormat.format(end);

                return "Du " + startFormatted + " au " + endFormatted + " " + year;
            }
        } catch (ParseException e) {
            // Fallback vers le format original
        }

        return "Du " + startDate + " au " + endDate;
    }

    private void loadSubscriptionImage(SubscriptionViewHolder holder, Subscription sub) {
        if (sub.getPlan() != null && sub.getPlan().getImage() != null && !sub.getPlan().getImage().isEmpty()) {
            String imageUrl;

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
    }

    private void setupButtons(SubscriptionViewHolder holder, Subscription sub) {
        boolean isExpired = isSubscriptionExpired(sub.getEndDate());
        String status = sub.getStatus().toLowerCase();

        // Configuration des boutons selon le statut
        if ("cancelled".equals(status) || isExpired) {
            holder.btnRenew.setVisibility(View.VISIBLE);
            holder.btnCancel.setVisibility(View.GONE);
        } else if ("active".equals(status)) {
            holder.btnRenew.setVisibility(View.GONE);
            holder.btnCancel.setVisibility(View.VISIBLE);
        } else if ("pending".equals(status)) {
            holder.btnRenew.setVisibility(View.GONE);
            holder.btnCancel.setVisibility(View.VISIBLE);
        } else {
            holder.btnRenew.setVisibility(View.GONE);
            holder.btnCancel.setVisibility(View.GONE);
        }

        // Listeners des boutons
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

    private boolean isSubscriptionExpired(String endDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date endDateParsed = sdf.parse(endDate);
            Date currentDate = new Date();
            return endDateParsed != null && endDateParsed.before(currentDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return subscriptions != null ? subscriptions.size() : 0;
    }

    static class SubscriptionViewHolder extends RecyclerView.ViewHolder {
        TextView title, status, dates, timeRemaining;
        Button btnRenew, btnCancel;
        ImageView subscriptionImage;
        ProgressBar subscriptionProgress;
        View statusIndicator;

        public SubscriptionViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.subscriptionTitle);
            status = itemView.findViewById(R.id.subscriptionStatus);
            dates = itemView.findViewById(R.id.subscriptionDates);
            btnRenew = itemView.findViewById(R.id.btnRenew);
            btnCancel = itemView.findViewById(R.id.btnCancel);
            subscriptionImage = itemView.findViewById(R.id.subscriptionImage);
            subscriptionProgress = itemView.findViewById(R.id.subscriptionProgress);
            timeRemaining = itemView.findViewById(R.id.timeRemaining);
            statusIndicator = itemView.findViewById(R.id.statusIndicator);
        }
    }
}