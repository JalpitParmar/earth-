package com.scarycat.earth;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.scarycat.earth.utils.DailySpinReceiver;
import com.scarycat.earth.utils.MusicManager;
import com.scarycat.earth.utils.PreferencesManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class spin extends AppCompatActivity {
    SoundPool soundPool;
    int tapSound,spinsound,rewadsound;
    TextView tvGoldBars, tvHearts, tvCoins,tvhammer,tvbomb,tvswap,tvcolor_bomb;
    private ImageView imgWheel;
    private ImageButton btnSpin, btnWatchAdSpin;
    private TextView tvResult;

    private PreferencesManager prefs;
    private RewardedAd rewardedAd;
    private boolean isAdLoading = false;

    private static final String PREF_LAST_SPIN_DATE = "last_spin_date";
    private static final String PREF_EXTRA_SPIN = "extra_spin_count";

    private Random random = new Random();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spin);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        );
        soundPool = new SoundPool.Builder()
                .setMaxStreams(5)
                .build();

        tapSound = soundPool.load(this, R.raw.btn, 1);
        spinsound = soundPool.load(this,R.raw.spin,1);
        rewadsound = soundPool.load(this,R.raw.rewad,1);

        prefs = new PreferencesManager(this);

        imgWheel = findViewById(R.id.imgWheel);
        btnSpin = findViewById(R.id.btnSpin);
        btnWatchAdSpin = findViewById(R.id.btnWatchAdSpin);
        tvResult = findViewById(R.id.tvSpinResult);
        tvGoldBars = findViewById(R.id.tvGoldBars);
        tvHearts = findViewById(R.id.tvHearts);
        tvCoins = findViewById(R.id.tvCoins);
        tvhammer = findViewById(R.id.tvHammer);
        tvbomb = findViewById(R.id.tvBomb);
        tvswap = findViewById(R.id.tvSwap);
        tvcolor_bomb = findViewById(R.id.tvColorBomb);

       updateui();
        // ✅ PRELOAD AD
        loadRewardedAd();

        btnSpin.setOnClickListener(v -> {

            if (!canSpin()) {
                Toast.makeText(this,
                        "No spins left! Watch an ad 🎥",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if(prefs.getBoolean("sfx_on", true)){
                float val = prefs.getInt("sfx_volume", 80);
                soundPool.play(tapSound, val, val, 1, 0, 1f);
            }
            v.animate()
                    .scaleX(0.8f)
                    .scaleY(0.8f)
                    .rotationBy(-5f)
                    .setDuration(80)
                    .withEndAction(() -> v.animate()
                            .scaleX(1.1f)
                            .scaleY(1.1f)
                            .rotationBy(10f)
                            .setDuration(120)
                            .withEndAction(() -> v.animate()
                                    .scaleX(1f)
                                    .scaleY(1f)
                                    .rotation(0f)
                                    .setDuration(80)
                                    .withEndAction(() -> {
                                        consumeSpin();
                                        spinWheel();
                                        updateui();
                                    })
                            )
                    );
        });

        btnWatchAdSpin.setOnClickListener(v -> {



            if(prefs.getBoolean("sfx_on", true)){
                float val = prefs.getInt("sfx_volume", 80);
                soundPool.play(tapSound, val, val, 1, 0, 1f);
            }
            if (rewardedAd == null) {
                Toast.makeText(this,
                        "Ad is loading, please wait…",
                        Toast.LENGTH_SHORT).show();
                v.animate()
                        .scaleX(0.8f)
                        .scaleY(0.8f)
                        .rotationBy(-5f)
                        .setDuration(80)
                        .withEndAction(() -> v.animate()
                                .scaleX(1.1f)
                                .scaleY(1.1f)
                                .rotationBy(10f)
                                .setDuration(120)
                                .withEndAction(() -> v.animate()
                                        .scaleX(1f)
                                        .scaleY(1f)
                                        .rotation(0f)
                                        .setDuration(80)
                                        .withEndAction(this::loadRewardedAd)
                                )
                        );

                return;
            }
            rewardedAd.show(this, rewardItem -> {

                SharedPreferences sp =
                        getSharedPreferences("earth_game_prefs", MODE_PRIVATE);

                int spins = sp.getInt(PREF_EXTRA_SPIN, 0);
                sp.edit()
                        .putInt(PREF_EXTRA_SPIN, spins + 1)
                        .apply();

                Toast.makeText(this,
                        "+1 Extra Spin 🎁",
                        Toast.LENGTH_SHORT).show();
            });

            rewardedAd = null;
            loadRewardedAd(); // preload next
        });

        if(!prefs.getBoolean("music_on", true)){
            MusicManager.pause();
        }
    }
    void updateui(){
        tvCoins.setText(""+prefs.getInt("coins", 0));
        tvHearts.setText(""+prefs.getInt("hearts", 0));
        tvGoldBars.setText(""+prefs.getInt("goldbars", 0));
        tvhammer.setText("" + prefs.getInt("booster_hammer_count", 0));
        tvbomb.setText("" + prefs.getInt("booster_bomb_count", 0));
        tvswap.setText("" + prefs.getInt("booster_swap_count", 0));
        tvcolor_bomb.setText("" + prefs.getInt("booster_color_bomb_count", 0));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadRewardedAd();
        if(!prefs.getBoolean("music_on", true)){
            MusicManager.pause();
        }else {
            MusicManager.resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MusicManager.pause();
    }
    // ---------------- SPIN LOGIC ----------------

    private void spinWheel() {
        if(prefs.getBoolean("sfx_on", true)){
            float val = prefs.getInt("sfx_volume", 80);
            soundPool.play(spinsound, val, val, 1, 0, 1f);
        }
        btnSpin.setEnabled(false);
        tvResult.setText("");

        RotateAnimation rotate = new RotateAnimation(
                0,
                360 * 5 + random.nextInt(360),
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );

        rotate.setDuration(3000);
        rotate.setFillAfter(true);
        rotate.setAnimationListener(new Animation.AnimationListener() {

            @Override public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {

                String reward = getReward();
                applyReward(reward);

//                tvResult.setText("You won: " + reward);
                showRewardPopup(reward);
                prefs.putString(PREF_LAST_SPIN_DATE, getToday());

                btnSpin.setEnabled(true);
            }

            @Override public void onAnimationRepeat(Animation animation) {}
        });

        imgWheel.startAnimation(rotate);
    }

    private String getReward() {

        int r = random.nextInt(100) + 1;

        if (r <= 30) return "100 Coins";
        if (r <= 50) return "Hammer";
        if (r <= 65) return "Bomb";
        if (r <= 75) return "Heart";
        if (r <= 85) return "Swap";
        if (r <= 93) return "Color Bomb";
        return "10 Gold Bars";
    }

    private void applyReward(String reward) {

        switch (reward) {
            case "100 Coins":
                prefs.putInt("coins", prefs.getInt("coins", 0) + 100);
                break;
            case "Hammer":
                prefs.putInt("booster_hammer_count",
                        prefs.getInt("booster_hammer_count", 0) + 1);
                break;
            case "Bomb":
                prefs.putInt("booster_bomb_count",
                        prefs.getInt("booster_bomb_count", 0) + 1);
                break;
            case "Heart":
                prefs.putInt("hearts", prefs.getInt("hearts", 0) + 1);
                break;
            case "Swap":
                prefs.putInt("booster_swap_count",
                        prefs.getInt("booster_swap_count", 0) + 1);
                break;
            case "Color Bomb":
                prefs.putInt("booster_color_bomb_count",
                        prefs.getInt("booster_color_bomb_count", 0) + 1);
                break;
            case "10 Gold Bars":
                prefs.putInt("goldbars", prefs.getInt("goldbars", 0) + 1);
                break;
        }
        updateui();
    }

    // ---------------- SPIN LIMIT ----------------

    private boolean canSpin() {

        SharedPreferences sp = getSharedPreferences("earth_game_prefs", MODE_PRIVATE);

        String lastSpinDate = sp.getString(PREF_LAST_SPIN_DATE, "");
        int extraSpins = sp.getInt(PREF_EXTRA_SPIN, 0);

        return !getToday().equals(lastSpinDate) || extraSpins > 0;
    }

    private void consumeSpin() {

        SharedPreferences sp = getSharedPreferences("earth_game_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        int extraSpins = sp.getInt(PREF_EXTRA_SPIN, 0);

        if (extraSpins > 0) {
            editor.putInt(PREF_EXTRA_SPIN, extraSpins - 1);
        } else {
            editor.putString(PREF_LAST_SPIN_DATE, getToday());
        }
        editor.apply();
        if (prefs.getBoolean("notification_on", true)) {
            scheduleDailySpinNotification();
        }
    }
    private String getToday() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(new Date());
    }

    // ---------------- ADS ----------------
    private void loadRewardedAd() {

        if (rewardedAd != null || isAdLoading) return;

        isAdLoading = true;

        AdRequest adRequest = new AdRequest.Builder().build();

        RewardedAd.load(
                this,
                "ca-app-pub-3940256099942544/5224354917", // TEST AD ID
                adRequest,
                new RewardedAdLoadCallback() {

                    @Override
                    public void onAdLoaded(RewardedAd ad) {
                        rewardedAd = ad;
                        isAdLoading = false;
                    }

                    @Override
                    public void onAdFailedToLoad(LoadAdError error) {
                        rewardedAd = null;
                        isAdLoading = false;
                    }
                }
        );
    }
    private void showRewardPopup(String reward) {
        if(prefs.getBoolean("sfx_on", true)){
            float val = prefs.getInt("sfx_volume", 80);
            soundPool.play(rewadsound, val, val, 1, 0, 1f);
        }
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_spin_reward);
        dialog.setCancelable(false);

        TextView tv = dialog.findViewById(R.id.tvReward);
        tv.setText("🎁 You won: " + reward);
        ImageView ri = dialog.findViewById(R.id.rewardimg);

        if (reward == "100 Coins") {
            ri.setImageResource(R.drawable.rewad_coin);

        } else if (reward == "Hammer") {
            ri.setImageResource(R.drawable.hammer_rewad);

        } else if (reward == "Bomb") {
            ri.setImageResource(R.drawable.bomb_rewad);
        } else if (reward == "Heart") {
            ri.setImageResource(R.drawable.heart_rewad);
        } else if (reward == "Swap") {
            ri.setImageResource(R.drawable.swap_rewad);
        } else if (reward == "Color Bomb") {
            ri.setImageResource(R.drawable.colorboomb_rewad);
        } else if (reward == "10 Gold Bars") {
            ri.setImageResource(R.drawable.goldbar_rewad);
        }

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(
                    new ColorDrawable(Color.TRANSPARENT)
            );
        }
        dialog.show();
        // Auto dismiss after 2 seconds
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }, 2000);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
    }
    @SuppressLint("ScheduleExactAlarm")
    private void scheduleDailySpinNotification() {

        long now = System.currentTimeMillis();

        // Next day (24h later)
        long nextDay = now + 24 * 60 * 60 * 1000;

        Intent intent = new Intent(this, DailySpinReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager =
                (AlarmManager) getSystemService(ALARM_SERVICE);

        if (alarmManager != null) {
            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    nextDay,
                    pendingIntent
            );
        }
    }
}
