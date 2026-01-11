package com.scarycat.earth.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class MissionChestManager {

    private static final String PREF = "earth_game_prefs";
    private static final String KEY_TIER = "tier";
    private static final String KEY_WINS = "wins_today";
    private static final String KEY_DATE = "date";
    private static final String KEY_LAST_DATE = "last_open_date";
    private static final int WINS_REQUIRED = 5;

    private final SharedPreferences prefs;
    private final Random random = new Random();

    public MissionChestManager(Context context) {
        prefs = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        updateDailyTier();
    }

    /* ---------------- DAILY UPDATE ---------------- */

    private void updateDailyTier() {

        String today = today();
        String saved = prefs.getString(KEY_DATE, "");

        if (!today.equals(saved)) {
            ChestTier next = getCurrentTier().next();
            prefs.edit()
                    .putString(KEY_TIER, next.name())
                    .putInt(KEY_WINS, 0)
                    .putString(KEY_DATE, today)
                    .apply();
        }
    }

    /* ---------------- WINS ---------------- */

    public void onLevelWon() {
        int wins = prefs.getInt(KEY_WINS, 0);
        prefs.edit().putInt(KEY_WINS, wins + 1).apply();
    }

    public int getWinsToday() {
        return prefs.getInt(KEY_WINS, 0);
    }

    public boolean isChestReady() {
        return getWinsToday() >= WINS_REQUIRED;
    }

    public void consumeChest() {
        prefs.edit().putInt(KEY_WINS, 0).apply();
    }

    /* ---------------- TIER ---------------- */

    public ChestTier getCurrentTier() {
        return ChestTier.valueOf(
                prefs.getString(KEY_TIER, ChestTier.BRONZE.name())
        );
    }

    /* ---------------- REWARD CHANCES (USED BY UI) ---------------- */

    public RewardChance[] getRewardChancesForTier() {

        switch (getCurrentTier()) {

            case BRONZE:
                return new RewardChance[]{
                        new RewardChance("Coins", 50),
                        new RewardChance("Hearts", 25),
                        new RewardChance("Boosters", 20),
                        new RewardChance("Gold Bars", 5)
                };

            case SILVER:
                return new RewardChance[]{
                        new RewardChance("Coins", 40),
                        new RewardChance("Hearts", 25),
                        new RewardChance("Boosters", 25),
                        new RewardChance("Gold Bars", 10)
                };

            case GOLD:
                return new RewardChance[]{
                        new RewardChance("Coins", 30),
                        new RewardChance("Hearts", 25),
                        new RewardChance("Boosters", 30),
                        new RewardChance("Gold Bars", 15)
                };

            case EPIC:
                return new RewardChance[]{
                        new RewardChance("Coins", 25),
                        new RewardChance("Hearts", 20),
                        new RewardChance("Boosters", 35),
                        new RewardChance("Gold Bars", 20)
                };

            case MYTHIC:
                return new RewardChance[]{
                        new RewardChance("Coins", 20),
                        new RewardChance("Hearts", 15),
                        new RewardChance("Boosters", 40),
                        new RewardChance("Gold Bars", 25)
                };
        }
        return new RewardChance[0];
    }

    /* ---------------- OPEN CHEST ---------------- */

    public ChestReward openChest() {

        ChestTier tier = getCurrentTier();
        int m = tier.multiplier;

        int roll = random.nextInt(120);
        if (roll < 35) return new ChestReward("Hammer", 1 * m);
        if (roll < 55) return new ChestReward("Coins", 200 * m);
        if (roll < 70) return new ChestReward("Bomb", 1 * m);
        if (roll < 85) return new ChestReward("Heart", 1 * m);
        if (roll < 95) return new ChestReward("Swap", 1 * m);
        if (roll < 115) return new ChestReward("Color Bomb", 1 * m);
        return new ChestReward("Gold Bars", 1 * m);
    }

    /* ---------------- MODELS ---------------- */

    public static class ChestReward {
        public String type;
        public int amount;

        public ChestReward(String type, int amount) {
            this.type = type;
            this.amount = amount;
        }
    }

    private String today() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(new Date());
    }
    // =================================================
// CALL THIS ONCE PER DAY (Home / Splash)
// =================================================
    public void updateDailyChest() {

        String today = today();
        String last = prefs.getString(KEY_LAST_DATE, "");

        if (today.equals(last)) return;

        ChestTier current = getCurrentTier();
        ChestTier nextTier = current.next();

        prefs.edit()
                .putString(KEY_TIER, nextTier.name())
                .putString(KEY_LAST_DATE, today)
                .apply();
    }

}
