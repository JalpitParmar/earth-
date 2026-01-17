package com.scarycat.earth;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.scarycat.earth.utils.HeartsManager;
import com.scarycat.earth.utils.MissionChestManager;
import com.scarycat.earth.utils.MusicManager;

public class home extends AppCompatActivity {
    private SharedPreferences prefs;
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

        prefs = getSharedPreferences("earth_game_prefs", MODE_PRIVATE);
        ImageButton btnPlay = findViewById(R.id.btnPlay);
        ImageButton btnSettings = findViewById(R.id.btnSettings);
        ImageButton btnQuit = findViewById(R.id.btnQuit);
        // ▶ PLAY → Main Page
        btnPlay.setOnClickListener(v -> {
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
            Intent intent = new Intent(this, LoadingActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });

        // ⚙ SETTINGS → Settings Page
        btnSettings.setOnClickListener(v -> {
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
            Intent intent = new Intent(this, settings.class);
            startActivity(intent);
        });

        // ❌ QUIT → Exit App
        btnQuit.setOnClickListener(v -> {
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
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                finishAffinity(); // closes all activities
                System.exit(0);
            },300);
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
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(prefs.getBoolean("music_on", true)){
            MusicManager.resume();
        }
    }

}