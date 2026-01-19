package com.scarycat.earth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.MobileAds;
import com.scarycat.earth.utils.MusicManager;
import com.scarycat.earth.utils.PreferencesManager;

public class LoadingActivity extends AppCompatActivity {
    View progressFill;
    TextView tvLoading;
    FrameLayout barContainer;
    int progress = 0;
    Handler uiHandler;
    ImageView cloudLeft, cloudRight;
    FrameLayout cloudLayer;
    private static final int CLOUD_ANIM_DURATION = 1000;
    private PreferencesManager prefs1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_loading);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        );

        progressFill = findViewById(R.id.progressFill);
        tvLoading = findViewById(R.id.tvLoading);
        barContainer = findViewById(R.id.barContainer);
        cloudLeft = findViewById(R.id.cloudLeft);
        cloudRight = findViewById(R.id.cloudRight);
        cloudLayer = findViewById(R.id.cloudLayer);


        uiHandler = new Handler(Looper.getMainLooper());

        startLoading();
    }

    private void startLoading() {

        // 🔹 BACKGROUND THREAD (REAL PRELOAD)
        new Thread(() -> {
            preload();   // 👈 heavy work here
        }).start();

        // 🔹 UI PROGRESS ANIMATION
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (progress <= 100) {

                    int maxWidth = barContainer.getWidth();
                    int newWidth = (maxWidth * progress) / 100;

                    progressFill.getLayoutParams().width = newWidth;
                    progressFill.requestLayout();

                    tvLoading.setText("LOADING... " + progress + "%");

                    progress++;
                    uiHandler.postDelayed(this, 25);

                } else {
                    openNext();
                }
            }
        };

        barContainer.post(() -> uiHandler.post(runnable));
    }

    // 🚀 ALL HEAVY WORK HERE
    private void preload() {

        try {
            // 1️⃣ Music
            MusicManager.init(this);
            updateProgress(20);

            // 2️⃣ Ads SDK
            MobileAds.initialize(this, status -> {});
            updateProgress(40);

            // 3️⃣ Preferences warm-up
            SharedPreferences prefs =
                    getSharedPreferences("earth_game_prefs", MODE_PRIVATE);
            prefs.getInt("coins", 0);
            prefs.getInt("hearts", 0);
            updateProgress(60);

            prefs1 = new PreferencesManager(this);
            // Initialize starting currency if not set
            if (!prefs.getBoolean("initialized", false)) {
                prefs1.putInt("coins", 1000); // Give starting coins
                prefs1.putInt("hearts", 5);
                prefs1.putInt("goldbars", 0);
                prefs1.putInt("booster_hammer_count", 0);
                prefs1.putInt("booster_bomb_count", 0);
                prefs1.putInt("booster_swap_count", 0);
                prefs1.putInt("booster_color_bomb_count", 0);
                prefs1.putBoolean("initialized", true);
            }


            // 4️⃣ Fake delay (smooth animation)
            Thread.sleep(600);
            updateProgress(80);

            // 5️⃣ Done
            updateProgress(100);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ✅ SAFE UI UPDATE
    private void updateProgress(int value) {
        uiHandler.post(() -> progress = value);
    }

    private void openNext() {

        showCloudsIn(() -> {

            // ⏸ Small pause so screen is FULLY white
            new Handler(Looper.getMainLooper()).postDelayed(() -> {

                Intent intent = new Intent(this, Meanu.class);
                startActivity(intent);
                overridePendingTransition(0, 0);

                // ⏸ Let new activity draw first
                new Handler(Looper.getMainLooper()).postDelayed(
                        this::showCloudsOut,
                        50
                );

                finish();

            }, 100); // 👈 THIS DELAY FIXES POP
        });
    }


    private void showCloudsIn(Runnable onComplete) {

        cloudLayer.setVisibility(View.VISIBLE);
        barContainer.setVisibility(View.GONE);
        tvLoading.setVisibility(View.GONE);
        progressFill.setVisibility(View.GONE);
        cloudLeft.animate()
                .translationX(0)
                .setDuration(CLOUD_ANIM_DURATION)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .start();

        cloudRight.animate()
                .translationX(0)
                .setDuration(CLOUD_ANIM_DURATION)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .withEndAction(onComplete) // ✅ wait until fully covered
                .start();
    }

    private void showCloudsOut() {

        cloudLeft.animate()
                .translationX(-cloudLeft.getWidth())
                .setDuration(CLOUD_ANIM_DURATION)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .start();

        cloudRight.animate()
                .translationX(cloudRight.getWidth())
                .setDuration(CLOUD_ANIM_DURATION)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .withEndAction(() ->
                        cloudLayer.setVisibility(View.GONE)
                )
                .start();
    }


}