package com.example.salledesport;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.FrameLayout;
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
    private final FrameLayout homeTab;           // ✅ Changé de MaterialCardView à FrameLayout
    private final FrameLayout statsTab;         // ✅ Changé de LinearLayout à FrameLayout
    private final FrameLayout addTab;           // ✅ Changé de LinearLayout à FrameLayout
    private final FrameLayout profileTab;       // ✅ Changé de LinearLayout à FrameLayout
    private final ImageView homeIcon, abonnementIcon, objectifIcon, profileIcon;
    private final TextView homeText, abonnementText, objectifText, profileText;

    // Nouveaux éléments du design moderne
    private final View homeActiveBackground;
    private final View homePulseRing;
    private final View statsNotificationDot;
    private final View profileOnlineIndicator;

    public BottomNavigationHandler(Activity activity) {
        this.activity = activity;

        // Initialiser les vues principales - Tous sont maintenant des FrameLayout
        homeTab = activity.findViewById(R.id.homeTab);
        statsTab = activity.findViewById(R.id.statsTab);
        addTab = activity.findViewById(R.id.addTab);
        profileTab = activity.findViewById(R.id.profileTab);

        // Initialiser les icônes et textes
        homeIcon = homeTab.findViewById(R.id.homeIcon);
        homeText = homeTab.findViewById(R.id.homeText);
        abonnementIcon = statsTab.findViewById(R.id.statsIcon);
        abonnementText = statsTab.findViewById(R.id.statsText);
        objectifIcon = addTab.findViewById(R.id.addIcon);
        objectifText = addTab.findViewById(R.id.addText);
        profileIcon = profileTab.findViewById(R.id.profileIcon);
        profileText = profileTab.findViewById(R.id.profileText);

        // Initialiser les nouveaux éléments du design moderne
        homeActiveBackground = homeTab.findViewById(R.id.homeActiveBackground);
        homePulseRing = homeTab.findViewById(R.id.homePulseRing);
        statsNotificationDot = statsTab.findViewById(R.id.statsNotificationDot);
        profileOnlineIndicator = profileTab.findViewById(R.id.profileOnlineIndicator);
    }

    public void setupNavigation(ActiveTab activeTab) {
        // Définir l'onglet actif
        switch (activeTab) {
            case HOME:
                setTabActive(homeTab, homeIcon, homeText, true);
                setTabInactive(statsTab, abonnementIcon, abonnementText, false);
                setTabInactive(addTab, objectifIcon, objectifText, false);
                setTabInactive(profileTab, profileIcon, profileText, false);
                break;
            case ABONNEMENT:
                setTabActive(statsTab, abonnementIcon, abonnementText, false);
                setTabInactive(homeTab, homeIcon, homeText, true);
                setTabInactive(addTab, objectifIcon, objectifText, false);
                setTabInactive(profileTab, profileIcon, profileText, false);
                break;
            case OBJECTIF:
                setTabActive(addTab, objectifIcon, objectifText, false);
                setTabInactive(homeTab, homeIcon, homeText, true);
                setTabInactive(statsTab, abonnementIcon, abonnementText, false);
                setTabInactive(profileTab, profileIcon, profileText, false);
                break;
            case PROFILE:
                setTabActive(profileTab, profileIcon, profileText, false);
                setTabInactive(homeTab, homeIcon, homeText, true);
                setTabInactive(statsTab, abonnementIcon, abonnementText, false);
                setTabInactive(addTab, objectifIcon, objectifText, false);
                break;
        }

        // Configurer les écouteurs de clics
        homeTab.setOnClickListener(v -> {
            if (activeTab != ActiveTab.HOME) {
                animateTabSelection();
                activity.startActivity(new Intent(activity, Home.class));
                activity.finish();
            }
        });

        statsTab.setOnClickListener(v -> {
            if (activeTab != ActiveTab.ABONNEMENT) {
                animateTabSelection();
                activity.startActivity(new Intent(activity, MySubscriptionsActivity.class));
                activity.finish();
            }
        });

        addTab.setOnClickListener(v -> {
            if (activeTab != ActiveTab.OBJECTIF) {
                animateTabSelection();
                activity.startActivity(new Intent(activity, MainActivity.class));
                activity.finish();
            }
        });

        profileTab.setOnClickListener(v -> {
            if (activeTab != ActiveTab.PROFILE) {
                animateTabSelection();
                activity.startActivity(new Intent(activity, ProfileActivity.class));
                activity.finish();
            }
        });
    }

    private void setTabActive(View tab, ImageView icon, TextView text, boolean isHome) {
        // Activer l'onglet avec le nouveau design
        if (isHome && homeActiveBackground != null) {
            homeActiveBackground.setVisibility(View.VISIBLE);
            if (homePulseRing != null) {
                homePulseRing.setVisibility(View.VISIBLE);
                startPulseAnimation(homePulseRing);
            }
        }

        // Style des icônes et textes actifs
        icon.setColorFilter(ContextCompat.getColor(activity, R.color.white));
        icon.setAlpha(1.0f);
        text.setTextColor(ContextCompat.getColor(activity, R.color.white));
        text.setAlpha(1.0f);

        // Police en gras pour l'onglet actif
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            text.setTypeface(activity.getResources().getFont(R.font.poppins_bold));
        }
    }

    private void setTabInactive(View tab, ImageView icon, TextView text, boolean isHome) {
        // Désactiver l'onglet
        if (isHome && homeActiveBackground != null) {
            homeActiveBackground.setVisibility(View.GONE);
            if (homePulseRing != null) {
                homePulseRing.setVisibility(View.GONE);
                homePulseRing.clearAnimation();
            }
        }

        // Style des icônes et textes inactifs
        icon.setColorFilter(ContextCompat.getColor(activity, R.color.text_secondary));
        icon.setAlpha(0.6f);
        text.setTextColor(ContextCompat.getColor(activity, R.color.text_secondary));
        text.setAlpha(0.6f);

        // Police normale pour les onglets inactifs
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            text.setTypeface(activity.getResources().getFont(R.font.poppins_medium));
        }
    }

    // ✨ Nouvelle méthode pour l'animation de sélection
    private void animateTabSelection() {
        // Animation simple de scale sur l'onglet cliqué
        // Peut être étendue avec ObjectAnimator pour des animations plus complexes
    }

    // ✨ Animation de pulse pour l'onglet actif
    private void startPulseAnimation(View pulseRing) {
        if (pulseRing != null) {
            // Animation de pulse basique
            pulseRing.animate()
                    .scaleX(1.2f)
                    .scaleY(1.2f)
                    .alpha(0.3f)
                    .setDuration(1500)
                    .withEndAction(() -> {
                        if (pulseRing.getVisibility() == View.VISIBLE) {
                            pulseRing.animate()
                                    .scaleX(1.0f)
                                    .scaleY(1.0f)
                                    .alpha(0.6f)
                                    .setDuration(1500)
                                    .withEndAction(() -> startPulseAnimation(pulseRing))
                                    .start();
                        }
                    })
                    .start();
        }
    }

    // ✨ Méthode pour afficher/masquer la notification dot
    public void showNotificationDot(boolean show) {
        if (statsNotificationDot != null) {
            statsNotificationDot.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    // ✨ Méthode pour afficher/masquer l'indicateur en ligne
    public void showOnlineIndicator(boolean show) {
        if (profileOnlineIndicator != null) {
            profileOnlineIndicator.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }
}