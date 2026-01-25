package com.scarycat.earth;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.media.SoundPool;
import android.os.Bundle;
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
}