package com.example.salledesport;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import com.google.android.material.card.MaterialCardView;

public class BottomNavigationHandler {

    public enum ActiveTab {
        HOME, ABONNEMENT, OBJECTIF, PROFILE
    }

    private final Activity activity;
    private final MaterialCardView homeTab;
    private final LinearLayout statsTab;
    private final LinearLayout addTab;
    private final LinearLayout profileTab;
    private final ImageView homeIcon, abonnementIcon, objectifIcon, profileIcon;
    private final TextView homeText, abonnementText, objectifText, profileText;

    public BottomNavigationHandler(Activity activity) {
        this.activity = activity;

        // Initialiser les vues
        homeTab = activity.findViewById(R.id.homeTab);
        statsTab = activity.findViewById(R.id.statsTab);
        addTab = activity.findViewById(R.id.addTab);
        profileTab = activity.findViewById(R.id.profileTab);

        homeIcon = homeTab.findViewById(R.id.homeIcon);
        homeText = homeTab.findViewById(R.id.homeText);
        abonnementIcon = statsTab.findViewById(R.id.statsIcon);
        abonnementText = statsTab.findViewById(R.id.statsText);
        objectifIcon = addTab.findViewById(R.id.addIcon);
        objectifText = addTab.findViewById(R.id.addText);
        profileIcon = profileTab.findViewById(R.id.profileIcon);
        profileText = profileTab.findViewById(R.id.profileText);
    }

    public void setupNavigation(ActiveTab activeTab) {
        // Définir l'onglet actif
        switch (activeTab) {
            case HOME:
                setTabActive(homeTab, homeIcon, homeText);
                setTabInactive(statsTab, abonnementIcon, abonnementText);
                setTabInactive(addTab, objectifIcon, objectifText);
                setTabInactive(profileTab, profileIcon, profileText);
                break;
            case ABONNEMENT:
                setTabActive(statsTab, abonnementIcon, abonnementText);
                setTabInactive(homeTab, homeIcon, homeText);
                setTabInactive(addTab, objectifIcon, objectifText);
                setTabInactive(profileTab, profileIcon, profileText);
                break;
            case OBJECTIF:
                setTabActive(addTab, objectifIcon, objectifText);
                setTabInactive(homeTab, homeIcon, homeText);
                setTabInactive(statsTab, abonnementIcon, abonnementText);
                setTabInactive(profileTab, profileIcon, profileText);
                break;
            case PROFILE:
                setTabActive(profileTab, profileIcon, profileText);
                setTabInactive(homeTab, homeIcon, homeText);
                setTabInactive(statsTab, abonnementIcon, abonnementText);
                setTabInactive(addTab, objectifIcon, objectifText);
                break;
        }

        // Configurer les écouteurs de clics
        homeTab.setOnClickListener(v -> {
            if (activeTab != ActiveTab.HOME) {
                setTabActive(homeTab, homeIcon, homeText);
                setTabInactive(statsTab, abonnementIcon, abonnementText);
                setTabInactive(addTab, objectifIcon, objectifText);
                setTabInactive(profileTab, profileIcon, profileText);
                activity.startActivity(new Intent(activity, Home.class));
                activity.finish();
            }
        });

        statsTab.setOnClickListener(v -> {
            if (activeTab != ActiveTab.ABONNEMENT) {
                setTabActive(statsTab, abonnementIcon, abonnementText);
                setTabInactive(homeTab, homeIcon, homeText);
                setTabInactive(addTab, objectifIcon, objectifText);
                setTabInactive(profileTab, profileIcon, profileText);
                activity.startActivity(new Intent(activity, MySubscriptionsActivity.class));
                activity.finish();
            }
        });

        addTab.setOnClickListener(v -> {
            if (activeTab != ActiveTab.OBJECTIF) {
                setTabActive(addTab, objectifIcon, objectifText);
                setTabInactive(homeTab, homeIcon, homeText);
                setTabInactive(statsTab, abonnementIcon, abonnementText);
                setTabInactive(profileTab, profileIcon, profileText);
                activity.startActivity(new Intent(activity, InfoActivity.class));
                activity.finish();
            }
        });

        profileTab.setOnClickListener(v -> {
            if (activeTab != ActiveTab.PROFILE) {
                setTabActive(profileTab, profileIcon, profileText);
                setTabInactive(homeTab, homeIcon, homeText);
                setTabInactive(statsTab, abonnementIcon, abonnementText);
                setTabInactive(addTab, objectifIcon, objectifText);
                activity.startActivity(new Intent(activity, ProfileActivity.class));
                activity.finish();
            }
        });
    }

    private void setTabActive(View tab, ImageView icon, TextView text) {
        if (tab instanceof MaterialCardView) {
            ((MaterialCardView) tab).setCardBackgroundColor(ContextCompat.getColor(activity, R.color.accent_green));
            ((MaterialCardView) tab).setCardElevation(0f);
            ((MaterialCardView) tab).setRadius(28f);
        } else {
            tab.setBackgroundColor(ContextCompat.getColor(activity, R.color.accent_green));
        }
        icon.setAlpha(1.0f);
        text.setAlpha(1.0f);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            text.setTypeface(activity.getResources().getFont(R.font.poppins_semibold));
        }
    }

    private void setTabInactive(View tab, ImageView icon, TextView text) {
        if (tab instanceof MaterialCardView) {
            ((MaterialCardView) tab).setCardBackgroundColor(ContextCompat.getColor(activity, android.R.color.transparent));
            ((MaterialCardView) tab).setCardElevation(0f);
            ((MaterialCardView) tab).setRadius(0f);
        } else {
            tab.setBackgroundColor(ContextCompat.getColor(activity, android.R.color.transparent));
        }
        icon.setAlpha(0.6f);
        text.setAlpha(0.6f);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            text.setTypeface(activity.getResources().getFont(R.font.poppins_medium));
        }
    }
}