package com.scarycat.earth;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
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
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);

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
            v.animate()
                    .scaleX(0.9f)
                    .scaleY(0.9f)
                    .setDuration(80)
                    .withEndAction(() ->
                            v.animate()
                                    .scaleX(1f)
                                    .scaleY(1f)
                                    .setDuration(80)
                    );
            showResetDialog();
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
}