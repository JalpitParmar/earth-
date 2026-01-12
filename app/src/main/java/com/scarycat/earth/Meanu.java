package com.scarycat.earth;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.scarycat.earth.utils.LevelAdapter;
import com.scarycat.earth.utils.LevelItem;
import com.scarycat.earth.utils.LevelRouter;
import com.scarycat.earth.utils.MusicManager;
import com.scarycat.earth.utils.PreferencesManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import java.util.ArrayList;
import java.util.List;

public class Meanu extends AppCompatActivity {

    ImageButton btnShop, btnWatchAdHeart, btnNext, btnPrev,btnspin,btnmission;
    TextView tvGoldBars, tvHearts, tvCoins,tvhammer,tvbomb,tvswap,tvcolor_bomb;
    GridView gridLevels;
    RelativeLayout rootLayout;

    private RewardedAd rewardedAd;
    private PreferencesManager prefs;

    private int page = 0;
    private static final int LEVELS_PER_PAGE = 10;

    private int[] backgrounds = {
            R.drawable.level_bg_1,
            R.drawable.level_bg_2,
            R.drawable.level_bg_3,
            R.drawable.level_bg_4,
            R.drawable.level_bg_5,
            R.drawable.level_bg_6,
            R.drawable.level_bg_7,
            R.drawable.level_bg_8,
            R.drawable.level_bg_9,
            R.drawable.level_bg_10

    };
    private static final int MAX_LEVEL = 100; // or however many you have

    private boolean isFirstLoad = true;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_meanu);

        prefs = new PreferencesManager(this);

        MobileAds.initialize(this, initializationStatus -> {});

        tvGoldBars = findViewById(R.id.tvGoldBars);
        tvHearts = findViewById(R.id.tvHearts);
        tvCoins = findViewById(R.id.tvCoins);

        btnShop = findViewById(R.id.shop);
        btnWatchAdHeart = findViewById(R.id.btnWatchAdHeart);

        gridLevels = findViewById(R.id.gridLevels);
        btnNext = findViewById(R.id.btnNext);
        btnPrev = findViewById(R.id.btnPrev);
        rootLayout = findViewById(R.id.rootLayout);
        btnspin = findViewById(R.id.meanu_btnspin);
        btnmission = findViewById(R.id.meanu_btnmission);

        updateCurrencyDisplay();
        btnShop.setOnClickListener(v ->
                startActivity(new Intent(this, Shop.class))
        );

        btnNext.setOnClickListener(v -> {
            int nextStart = (page + 1) * LEVELS_PER_PAGE + 1;
            if (nextStart <= MAX_LEVEL) {

                animateGrid(true);

                page++;

                int bgIndex = page % backgrounds.length;
                animateBackgroundChange(backgrounds[bgIndex]);

                gridLevels.postDelayed(this::loadLevels, 200);
            }
        });
        btnPrev.setOnClickListener(v -> {
            if (page > 0) {

                animateGrid(false);

                page--;

                int bgIndex = page % backgrounds.length;
                animateBackgroundChange(backgrounds[bgIndex]);

                gridLevels.postDelayed(this::loadLevels, 200);
            }
        });
        btnspin.setOnClickListener(view -> {
            Intent intent = new Intent(this,spin.class);
            startActivity(intent);
        });
        btnmission.setOnClickListener(view -> {
            Intent intent = new Intent(this,MissionChest.class);
            startActivity(intent);
        });


        gridLevels.setOnItemClickListener((parent, view, position, id) -> {
            LevelItem item = (LevelItem) parent.getItemAtPosition(position);

            if (!item.unlocked) {
                Toast.makeText(this, "Level locked 🔒", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences prefs = getSharedPreferences("earth_game_prefs", MODE_PRIVATE);
            int hearts = prefs.getInt("hearts", 0);

            if (hearts <= 0) {
                Toast.makeText(this, "You have no hearts left! Watch an ad or wait for hearts to refill.", Toast.LENGTH_LONG).show();
                return;  // Don't start the level if no hearts
            }

            // Start the level if hearts > 0
            startActivity(LevelRouter.getLevel(this, item.levelNumber));
        });


        loadRewardedAd();
        btnWatchAdHeart.setOnClickListener(v -> {
            if (rewardedAd != null) {
                MusicManager.pause();
                rewardedAd.show(this, rewardItem -> giveFreeHeart());
            } else {
                Toast.makeText(this, "Ad not loaded yet", Toast.LENGTH_SHORT).show();
                loadRewardedAd();
            }
        });


        int latestLevel = getLatestUnlockedLevel();
        page = (latestLevel - 1) / LEVELS_PER_PAGE;
        loadLevels();

    }

    // ------------------ LEVEL LOAD ------------------

    private void loadLevels() {

        List<LevelItem> levelList = new ArrayList<>();

        int start = page * LEVELS_PER_PAGE + 1;
        int end = Math.min(start + LEVELS_PER_PAGE - 1, MAX_LEVEL);

        for (int i = start; i <= end; i++) {

            boolean unlocked;
            if (i == 1) {
                unlocked = true;
            } else {
                unlocked = getSharedPreferences("earth_game_prefs", MODE_PRIVATE)
                        .getBoolean("UNLOCK_" + i, false);
            }

            int stars = getSharedPreferences("earth_game_prefs", MODE_PRIVATE)
                    .getInt("LEVEL_" + i, 0);

            levelList.add(new LevelItem(i, unlocked, stars));
        }

        LevelAdapter adapter = new LevelAdapter(this, levelList);
        gridLevels.setAdapter(adapter);

        // Change background per page
        //int bgIndex = page % backgrounds.length;
        //rootLayout.setBackgroundResource(backgrounds[bgIndex]);
        if (isFirstLoad) {
            int bgIndex = page % backgrounds.length;
            rootLayout.setBackgroundResource(backgrounds[bgIndex]);
            isFirstLoad = false;
        }
        // Disable buttons properly
        btnPrev.setEnabled(page > 0);
        btnNext.setEnabled(end < MAX_LEVEL);
    }


    // ------------------ ADS ------------------

    private void loadRewardedAd() {
        AdRequest adRequest = new AdRequest.Builder().build();

        RewardedAd.load(this,
                "ca-app-pub-3940256099942544/5224354917",
                adRequest,
                new RewardedAdLoadCallback() {
                    @Override
                    public void onAdLoaded(RewardedAd ad) {
                        rewardedAd = ad;
                    }

                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        rewardedAd = null;
                        Log.e("ADS", loadAdError.getMessage());
                    }
                });
    }

    private void giveFreeHeart() {
        int hearts = prefs.getInt("hearts", 0);
        prefs.putInt("hearts", hearts + 1);
        updateCurrencyDisplay();
        Toast.makeText(this, "+1 Heart ❤️", Toast.LENGTH_SHORT).show();
        loadRewardedAd();

    }

    @Override
    protected void onResume() {
        super.onResume();
        MusicManager.resume();
        updateCurrencyDisplay();
    }
    // ------------------ UI ------------------

    private void updateCurrencyDisplay() {
        tvCoins.setText("Coins: " + prefs.getInt("coins", 0));
        tvHearts.setText("Hearts: " + prefs.getInt("hearts", 0));
        tvGoldBars.setText("Gold Bars: " + prefs.getInt("goldbars", 0));
        tvhammer.setText("hammer: " + prefs.getInt("booster_hammer_count", 0));
        tvbomb.setText("bomb: " + prefs.getInt("booster_bomb_count", 0));
        tvswap.setText("swap: " + prefs.getInt("booster_swap_count", 0));
        tvcolor_bomb.setText("color bomb: " + prefs.getInt("booster_color_bomb_count", 0));
    }
    private int getLatestUnlockedLevel() {
        SharedPreferences sp = getSharedPreferences("earth_game_prefs", MODE_PRIVATE);
        int latest = 1;

        for (int i = 1; i <= MAX_LEVEL; i++) {
            if (i == 1 || sp.getBoolean("UNLOCK_" + i, false)) {
                latest = i;
            } else {
                break; // stop at first locked level
            }
        }
        return latest;
    }
    private void animateGrid(boolean next) {
        if (next) {
            gridLevels.startAnimation(
                    android.view.animation.AnimationUtils.loadAnimation(this, R.anim.slide_out_left)
            );
        } else {
            gridLevels.startAnimation(
                    android.view.animation.AnimationUtils.loadAnimation(this, R.anim.slide_out_right)
            );
        }
    }
    private void animateBackgroundChange(int newBgRes) {

        android.view.animation.Animation fadeOut =
                android.view.animation.AnimationUtils.loadAnimation(this, R.anim.fade_out);

        android.view.animation.Animation fadeIn =
                android.view.animation.AnimationUtils.loadAnimation(this, R.anim.fade_in);

        fadeOut.setAnimationListener(new android.view.animation.Animation.AnimationListener() {
            @Override
            public void onAnimationStart(android.view.animation.Animation animation) {}

            @Override
            public void onAnimationEnd(android.view.animation.Animation animation) {
                rootLayout.setBackgroundResource(newBgRes);
                rootLayout.startAnimation(fadeIn);
            }

            @Override
            public void onAnimationRepeat(android.view.animation.Animation animation) {}
        });

        rootLayout.startAnimation(fadeOut);
    }

}
