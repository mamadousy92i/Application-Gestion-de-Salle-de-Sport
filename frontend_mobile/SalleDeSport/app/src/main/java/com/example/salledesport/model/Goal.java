package com.example.salledesport.model;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Goal implements Parcelable {
    private String name;
    private String description;
    private List<Exercise> exercises;  // Liste d'exercices associés
    private List<DietPlan> diet_plans;  // Liste de régimes associés

    // Constructeur par défaut
    public Goal() {}

    // Constructeur pour Parcelable
    protected Goal(Parcel in) {
        name = in.readString();
        description = in.readString();
        exercises = new ArrayList<>();
        in.readList(exercises, Exercise.class.getClassLoader());
        diet_plans = new ArrayList<>();
        in.readList(diet_plans, DietPlan.class.getClassLoader());
    }

    public static final Creator<Goal> CREATOR = new Creator<Goal>() {
        @Override
        public Goal createFromParcel(Parcel in) {
            return new Goal(in);
        }

        @Override
        public Goal[] newArray(int size) {
            return new Goal[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeList(exercises);
        dest.writeList(diet_plans);
    }

    // Getters et Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    public List<DietPlan> getDiet_plans() {
        return diet_plans;
    }

    public void setDiet_plans(List<DietPlan> diet_plans) {
        this.diet_plans = diet_plans;
    }
}