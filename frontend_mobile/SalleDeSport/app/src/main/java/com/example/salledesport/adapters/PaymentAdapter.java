package com.example.salledesport.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salledesport.R;
import com.example.salledesport.model.Payment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder> {

    private List<Payment> payments;

    public PaymentAdapter(List<Payment> payments) {
        this.payments = payments;
    }

    @NonNull
    @Override
    public PaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payment, parent, false);
        return new PaymentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentViewHolder holder, int position) {
        Payment payment = payments.get(position);

        // Montant avec devise
        holder.amount.setText(String.format("%.2f €", payment.getAmount()));

        // Formatage de la date
        if (payment.getPaymentDate() != null) {
            String formattedDate = formatDate(payment.getPaymentDate());
            holder.date.setText(formattedDate);
        } else {
            holder.date.setText("Date non disponible");
        }

        // Méthode de paiement avec émoji et texte adapté
        String methodText = getFormattedPaymentMethod(payment.getPaymentMethod());
        holder.method.setText(methodText);

        // ✅ ICÔNE SÉCURISÉE - Utiliser une ressource système
        holder.methodIcon.setImageResource(android.R.drawable.ic_menu_info_details);

        // Statut du paiement avec couleur appropriée
        holder.status.setText(getFormattedStatus(payment.getStatus()));

        // ✅ COULEURS SÉCURISÉES
        try {
            int statusColor = getStatusColor(payment.getStatus(), holder.itemView.getContext());
            holder.status.setTextColor(statusColor);

            int statusBgColor = getStatusBackgroundColor(payment.getStatus(), holder.itemView.getContext());
            holder.statusBadge.setBackgroundColor(statusBgColor);
        } catch (Exception e) {
            // Fallback avec couleurs système
            holder.status.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.white));
            holder.statusBadge.setBackgroundColor(holder.itemView.getContext().getResources().getColor(android.R.color.darker_gray));
        }
    }

    @Override
    public int getItemCount() {
        return payments != null ? payments.size() : 0;
    }

    // Formatage de la date
    private String formatDate(String dateString) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy", Locale.FRENCH);
            Date date = inputFormat.parse(dateString.split("T")[0] + "T00:00:00");
            return outputFormat.format(date);
        } catch (ParseException e) {
            // Fallback si le parsing échoue
            return dateString.split("T")[0];
        }
    }

    // Conversion de la méthode de paiement en texte lisible
    private String getFormattedPaymentMethod(String method) {
        switch (method.toLowerCase()) {
            case "card":
                return "💳 Carte de crédit";
            case "transfer":
                return "🏦 Virement bancaire";
            case "paypal":
                return "💳 PayPal";
            case "cash":
                return "💵 Espèces";
            case "check":
                return "📋 Chèque";
            default:
                return "💳 " + method;
        }
    }

    private int getPaymentMethodIcon(String method) {
        // Utiliser des icônes système Android - toujours disponibles
        return android.R.drawable.ic_menu_info_details;
    }

    // Formatage du statut
    private String getFormattedStatus(String status) {
        switch (status.toLowerCase()) {
            case "completed":
                return "✅ Confirmé";
            case "pending":
                return "⏳ En attente";
            case "failed":
                return "❌ Échoué";
            case "cancelled":
                return "🚫 Annulé";
            default:
                return status;
        }
    }

    // Couleur du texte selon le statut
    private int getStatusColor(String status, android.content.Context context) {
        switch (status.toLowerCase()) {
            case "completed":
                return context.getResources().getColor(R.color.success_green);
            case "pending":
                return context.getResources().getColor(R.color.accent_orange);
            case "failed":
            case "cancelled":
                return context.getResources().getColor(R.color.error_red);
            default:
                return context.getResources().getColor(R.color.text_secondary);
        }
    }

    // Couleur de fond du badge selon le statut
    private int getStatusBackgroundColor(String status, android.content.Context context) {
        switch (status.toLowerCase()) {
            case "completed":
                return context.getResources().getColor(R.color.success_green);
            case "pending":
                return context.getResources().getColor(R.color.accent_orange);
            case "failed":
            case "cancelled":
                return context.getResources().getColor(R.color.error_red);
            default:
                return context.getResources().getColor(R.color.white_20);
        }
    }

    // Méthode pour mettre à jour la liste
    public void updatePayments(List<Payment> newPayments) {
        this.payments = newPayments;
        notifyDataSetChanged();
    }

    static class PaymentViewHolder extends RecyclerView.ViewHolder {
        TextView amount, date, method, status;
        ImageView methodIcon;
        View statusBadge;

        public PaymentViewHolder(@NonNull View itemView) {
            super(itemView);
            amount = itemView.findViewById(R.id.paymentAmount);
            date = itemView.findViewById(R.id.paymentDate);
            method = itemView.findViewById(R.id.paymentMethod);
            status = itemView.findViewById(R.id.paymentStatus);
            methodIcon = itemView.findViewById(R.id.paymentMethodIcon);
            statusBadge = itemView.findViewById(R.id.paymentStatusBadge);
        }
    }
}