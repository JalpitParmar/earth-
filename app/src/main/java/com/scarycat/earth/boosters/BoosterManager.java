package com.scarycat.earth.boosters;

import android.content.Context;
import com.scarycat.earth.utils.PreferencesManager;

public class BoosterManager {

    private static String key(BoosterType type) {
        return "booster_" + type.key + "_count";
    }

    public static int get(Context c, BoosterType type) {
        PreferencesManager prefs = new PreferencesManager(c);
        return prefs.getInt(key(type), 0);
    }

    public static boolean use(Context c, BoosterType type) {
        PreferencesManager prefs = new PreferencesManager(c);
        int count = get(c, type);
        if (count <= 0) return false;
        prefs.putInt(key(type), count - 1);
        return true;
    }

    public static void add(Context c, BoosterType type, int amount) {
        PreferencesManager prefs = new PreferencesManager(c);
        prefs.putInt(key(type), get(c, type) + amount);
    }
}
