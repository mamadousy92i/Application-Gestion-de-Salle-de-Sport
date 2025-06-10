package com.example.salledesport.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.salledesport.R;
import com.example.salledesport.model.Goal;
import java.util.List;

public class GoalAdapter extends RecyclerView.Adapter<GoalAdapter.GoalViewHolder> {

    private List<Goal> goalList;

    public GoalAdapter(List<Goal> goalList) {
        this.goalList = goalList;
    }

    @Override
    public GoalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.goal_item, parent, false);
        return new GoalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GoalViewHolder holder, int position) {
        Goal goal = goalList.get(position);
        holder.goalName.setText(goal.getName());  // Afficher le nom de l'objectif
        holder.goalDescription.setText(goal.getDescription());  // Afficher la description de l'objectif
    }

    @Override
    public int getItemCount() {
        return goalList.size();
    }

    public static class GoalViewHolder extends RecyclerView.ViewHolder {
        TextView goalName, goalDescription;

        public GoalViewHolder(View itemView) {
            super(itemView);
            goalName = itemView.findViewById(R.id.goalName);
            goalDescription = itemView.findViewById(R.id.goalDescription);
        }
    }
}
