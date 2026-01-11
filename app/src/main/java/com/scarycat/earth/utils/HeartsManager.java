package com.scarycat.earth.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HeartsManager {

    private static final String PREFS_NAME = "earth_game_prefs";
    private static final String KEY_HEARTS = "hearts";
    private static final String KEY_LAST_HEART_DATE = "last_heart_date";
    private static final int DAILY_HEARTS = 5;

    private SharedPreferences prefs;

    public HeartsManager(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    // Call this once at app launch or when user returns to home/menu screen
    public void giveDailyHeartsIfNeeded() {
        String today = getTodayDate();
        String lastDate = prefs.getString(KEY_LAST_HEART_DATE, "");

        if (!today.equals(lastDate)) {
            // New day - give 5 hearts
            int currentHearts = prefs.getInt(KEY_HEARTS, 0);
            prefs.edit()
                    .putInt(KEY_HEARTS, currentHearts + DAILY_HEARTS)
                    .putString(KEY_LAST_HEART_DATE, today)
                    .apply();
        }
    }

    public int getHearts() {
        return prefs.getInt(KEY_HEARTS, 0);
    }

    public void setHearts(int hearts) {
        prefs.edit().putInt(KEY_HEARTS, hearts).apply();
    }

    private String getTodayDate() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }
}
