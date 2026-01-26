package com.scarycat.earth.utils;


import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PreferencesManager {

    private static final String PREFS_NAME = "earth_game_prefs";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public PreferencesManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // Save int
    public void putInt(String key, int value) {
        editor.putInt(key, value).apply();
    }

    // Get int
    public int getInt(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    // Save boolean
    public void putBoolean(String key, boolean value) {
        editor.putBoolean(key, value).apply();
    }

    // Get boolean
    public boolean getBoolean(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    // Save string
    public void putString(String key, String value) {
        editor.putString(key, value).apply();
    }

    // Get string
    public String getString(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    // Remove a key
    public void remove(String key) {
        editor.remove(key).apply();
    }

    // Clear all preferences
    public void clear() {
        editor.clear().apply();
    }
    public static String getToday() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(new Date());
    }
}
