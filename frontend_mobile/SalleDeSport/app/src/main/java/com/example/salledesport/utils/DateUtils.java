package com.example.salledesport.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Classe utilitaire pour gérer les dates et les formats
 */
public class DateUtils {

    private static final String API_DATE_FORMAT = "yyyy-MM-dd";
    private static final String DISPLAY_DATE_FORMAT = "dd/MM/yyyy";
    private static final SimpleDateFormat apiFormat = new SimpleDateFormat(API_DATE_FORMAT, Locale.getDefault());
    private static final SimpleDateFormat displayFormat = new SimpleDateFormat(DISPLAY_DATE_FORMAT, Locale.getDefault());

    /**
     * Convertit une date au format API (yyyy-MM-dd) en format d'affichage (dd/MM/yyyy)
     */
    public static String apiToDisplayFormat(String apiDate) {
        if (apiDate == null || apiDate.isEmpty()) {
            return "";
        }
        
        try {
            Date date = apiFormat.parse(apiDate);
            return displayFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return apiDate; // En cas d'erreur, retourne la date originale
        }
    }

    /**
     * Convertit une date au format d'affichage (dd/MM/yyyy) en format API (yyyy-MM-dd)
     */
    public static String displayToApiFormat(String displayDate) {
        if (displayDate == null || displayDate.isEmpty()) {
            return "";
        }
        
        try {
            Date date = displayFormat.parse(displayDate);
            return apiFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return displayDate; // En cas d'erreur, retourne la date originale
        }
    }

    /**
     * Calcule la date de fin en ajoutant un nombre de mois à la date de début
     */
    public static String calculateEndDate(String startDate, int durationMonths) {
        try {
            Date date = apiFormat.parse(startDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MONTH, durationMonths);
            return apiFormat.format(calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return ""; // En cas d'erreur, retourne une chaîne vide
        }
    }

    /**
     * Vérifie si une date est dans le futur
     */
    public static boolean isFutureDate(String dateStr) {
        try {
            Date date = apiFormat.parse(dateStr);
            Date now = new Date();
            return date.after(now);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Calcule le nombre de jours restants jusqu'à une date
     */
    public static long getDaysUntil(String dateStr) {
        try {
            Date date = apiFormat.parse(dateStr);
            Date now = new Date();
            long diffInMillies = date.getTime() - now.getTime();
            return TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Obtient la date actuelle au format API
     */
    public static String getCurrentDateApiFormat() {
        return apiFormat.format(new Date());
    }
}
