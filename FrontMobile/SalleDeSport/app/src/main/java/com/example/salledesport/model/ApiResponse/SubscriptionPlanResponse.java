package com.example.salledesport.model.ApiResponse;

import java.util.List;

public class SubscriptionPlanResponse<T> {
        private int count;
        private String next;
        private String previous;
        private List<T> results;

        // Getters et Setters
        public List<T> getResults() {
            return results;
        }
}

