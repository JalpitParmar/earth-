package com.scarycat.earth;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.scarycat.earth.utils.MusicManager;

public class settings extends AppCompatActivity {
    private SharedPreferences prefs;
    SoundPool soundPool;
    int tapSound;

    private long lastBackPressTime = 0;
    private static final long BACK_PRESS_DELAY = 2000; // 2 seconds

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        );

        soundPool = new SoundPool.Builder()
                .setMaxStreams(5)
                .build();

        tapSound = soundPool.load(this, R.raw.btn, 1);

        prefs = getSharedPreferences("earth_game_prefs", MODE_PRIVATE);

        Switch switchMusic = findViewById(R.id.switchMusic);
        Switch switchSfx = findViewById(R.id.switchSfx);
        Switch switchNotification = findViewById(R.id.switchNotification);

        SeekBar seekMusic = findViewById(R.id.seekMusic);
        SeekBar seekSfx = findViewById(R.id.seekSfx);
        ImageButton btnResetGame = findViewById(R.id.btnResetGame);
        ImageButton btnback = findViewById(R.id.btnback);

        // Load saved values
        switchMusic.setChecked(prefs.getBoolean("music_on", true));
        switchSfx.setChecked(prefs.getBoolean("sfx_on", true));
        switchNotification.setChecked(prefs.getBoolean("notification_on", true));

        seekMusic.setProgress(prefs.getInt("music_volume", 80));
        seekSfx.setProgress(prefs.getInt("sfx_volume", 80));

        // MUSIC ON / OFF
        switchMusic.setOnCheckedChangeListener((buttonView, isChecked) -> {

            prefs.edit().putBoolean("music_on", isChecked).apply();

            if (isChecked) {
                MusicManager.resume();
            } else {
                MusicManager.pause();
            }
        });


        // SFX ON/OFF
        switchSfx.setOnCheckedChangeListener((buttonView, isChecked) -> prefs.edit().putBoolean("sfx_on", isChecked).apply());

        // NOTIFICATION ON/OFF
        switchNotification.setOnCheckedChangeListener((buttonView, isChecked) -> prefs.edit().putBoolean("notification_on", isChecked).apply());

        // MUSIC VOLUME
        seekMusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                prefs.edit().putInt("music_volume", progress).apply();
                MusicManager.setVolume(progress / 100f);
            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // SFX VOLUME
        seekSfx.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                prefs.edit().putInt("sfx_volume", progress).apply();
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // RESET GAME
        btnResetGame.setOnClickListener(v -> {
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
                                        showResetDialog();
                                    })
                            )
                    );

        });

        if(!prefs.getBoolean("music_on", true)){
            MusicManager.pause();
        }

        btnback.setOnClickListener(v -> {
            if(prefs.getBoolean("sfx_on", true)){
                float val = prefs.getInt("sfx_volume", 80);
                soundPool.play(tapSound, val, val, 1, 0, 1f);

            }
            Intent intent = new Intent(this, home.class);
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

    }

    private void showResetDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Reset Game")
                .setMessage("This will delete all progress. Are you sure?")
                .setPositiveButton("YES", (dialog, which) -> {
                    prefs.edit().clear().apply();
                    Toast.makeText(this, "Game Reset Successfully", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("NO", null)
                .show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
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