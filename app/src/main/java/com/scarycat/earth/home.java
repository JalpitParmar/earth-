package com.scarycat.earth;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.scarycat.earth.utils.HeartsManager;
import com.scarycat.earth.utils.MissionChestManager;
import com.scarycat.earth.utils.MusicManager;
import com.scarycat.earth.utils.SpinReminderReceiver;

import java.util.Calendar;

public class home extends AppCompatActivity {
    private SharedPreferences prefs;
    SoundPool soundPool;
    int tapSound;
    private long lastBackPressTime = 0;
    private static final long BACK_PRESS_DELAY = 2000; // 2 seconds

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        );

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (Build.VERSION.SDK_INT >= 33) {
            requestPermissions(
                    new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                    1001
            );
        }

        soundPool = new SoundPool.Builder()
                .setMaxStreams(5)
                .build();

        tapSound = soundPool.load(this, R.raw.btn, 1);

        prefs = getSharedPreferences("earth_game_prefs", MODE_PRIVATE);
        ImageButton btnPlay = findViewById(R.id.btnPlay);
        ImageButton btnSettings = findViewById(R.id.btnSettings);
        ImageButton btnQuit = findViewById(R.id.btnQuit);
        // ▶ PLAY → Main Page
        btnPlay.setOnClickListener(v -> {
            if(prefs.getBoolean("sfx_on", true)){
                float val = prefs.getInt("sfx_volume", 80);
                soundPool.play(tapSound, val, val, 1, 0, 1f);
            }
            Intent intent = new Intent(this, LoadingActivity.class);
            v.animate()
                    .scaleX(0.85f)
                    .scaleY(0.85f)
                    .setDuration(80)
                    .withEndAction(() -> v.animate()
                            .scaleX(1.05f)
                            .scaleY(1.05f)
                            .setDuration(120)
                            .withEndAction(() -> v.animate()
                                    .scaleX(1f)
                                    .scaleY(1f)
                                    .setDuration(80)
                                    .withEndAction(() ->{
                                        startActivity(intent);
                                        finish();
                                    })
                            )
                    );
            overridePendingTransition(0, 0);
        });

        // ⚙ SETTINGS → Settings Page
        btnSettings.setOnClickListener(v -> {
            if(prefs.getBoolean("sfx_on", true)){
                float val = prefs.getInt("sfx_volume", 80);
                soundPool.play(tapSound, val, val, 1, 0, 1f);
            }


            Intent intent = new Intent(this, settings.class);
            v.animate()
                    .scaleX(0.85f)
                    .scaleY(0.85f)
                    .setDuration(80)
                    .withEndAction(() -> v.animate()
                            .scaleX(1.05f)
                            .scaleY(1.05f)
                            .setDuration(120)
                            .withEndAction(() -> v.animate()
                                    .scaleX(1f)
                                    .scaleY(1f)
                                    .setDuration(80)
                                    .withEndAction(() ->{
                                        startActivity(intent);
                                        finish();
                                    })
                            )
                    );
        });

        // ❌ QUIT → Exit App
        btnQuit.setOnClickListener(v -> {
            if(prefs.getBoolean("sfx_on", true)){
                float val = prefs.getInt("sfx_volume", 80);
                soundPool.play(tapSound, val, val, 1, 0, 1f);
            }

            v.animate()
                    .scaleX(0.85f)
                    .scaleY(0.85f)
                    .setDuration(80)
                    .withEndAction(() -> v.animate()
                            .scaleX(1.05f)
                            .scaleY(1.05f)
                            .setDuration(120)
                            .withEndAction(() -> v.animate()
                                    .scaleX(1f)
                                    .scaleY(1f)
                                    .setDuration(80)
                                    .withEndAction(() ->
                                            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                                                finishAffinity(); // closes all activities
                                                System.exit(0);
                                            },300)
                                    )
                            )
                    );

        });

        ///////init mission chest
        MissionChestManager chestManager = new MissionChestManager(this);
        chestManager.updateDailyChest();
        //////init dayli heart
        HeartsManager heartsManager = new HeartsManager(this);
        heartsManager.giveDailyHeartsIfNeeded(); // Give 5 hearts if not given today

        MusicManager.init(this);
        if(!prefs.getBoolean("music_on", true)){
            MusicManager.pause();
        }
        if(!prefs.contains("coins"))
            prefs.edit()
                    .putInt("hearts",5)
                    .putBoolean("UNLOCK_",false)
                    .putInt("LEVEL_",5)
                    .putInt("coins",100)
                    .putInt("goldbars",0)
                    .putInt("booster_hammer_count",3)
                    .putInt("booster_bomb_count",3)
                    .putInt("booster_swap_count",2)
                    .putInt("booster_color_bomb_count",2)
                    .apply();


        if(prefs.getBoolean("music_on", true)){
            MusicManager.resume();
        }
        if (prefs.getBoolean("notification_on", true)) {
            scheduleDailySpinNotification();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(!prefs.getBoolean("music_on", true)){
            MusicManager.pause();
        }else {
            MusicManager.resume();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
    }

    private void scheduleDailySpinNotification() {

        AlarmManager alarmManager =
                (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, SpinReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // ⏰ Fire at 10:00 AM daily
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        // If time passed today → schedule tomorrow
        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,
                pendingIntent
        );
    }
    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {


        if (System.currentTimeMillis() - lastBackPressTime < BACK_PRESS_DELAY) {
            showExitDialog();
        } else {
            lastBackPressTime = System.currentTimeMillis();
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // SAME behavior
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void showExitDialog() {

        new AlertDialog.Builder(this)
                .setTitle("Exit Game")
                .setMessage("Are you sure you want to close the game?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, which) -> {
                    finishAffinity(); // closes entire app
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }
}