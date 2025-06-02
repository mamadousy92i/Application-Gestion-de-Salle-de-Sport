package com.example.salledesport.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salledesport.R;
import com.example.salledesport.model.Payment;

import java.util.List;

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

        holder.amount.setText(payment.getAmount() + " €");
        holder.date.setText("Le " + payment.getPaymentDate().split("T")[0]);
        holder.method.setText("Méthode : " + payment.getPaymentMethod());
    }

    @Override
    public int getItemCount() {
        return payments.size();
    }

    static class PaymentViewHolder extends RecyclerView.ViewHolder {
        TextView amount, date, method;

        public PaymentViewHolder(@NonNull View itemView) {
            super(itemView);
            amount = itemView.findViewById(R.id.paymentAmount);
            date = itemView.findViewById(R.id.paymentDate);
            method = itemView.findViewById(R.id.paymentMethod);
        }
    }
}

