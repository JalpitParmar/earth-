package com.scarycat.earth;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
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

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageButton btnPlay = findViewById(R.id.btnPlay);
        ImageButton btnSettings = findViewById(R.id.btnSettings);
        ImageButton btnQuit = findViewById(R.id.btnQuit);
        // ▶ PLAY → Main Page
        btnPlay.setOnClickListener(v -> {

            Intent intent = new Intent(this, Meanu.class);
            startActivity(intent);
        });

        // ⚙ SETTINGS → Settings Page
        btnSettings.setOnClickListener(v -> {
            Intent intent = new Intent(this, settings.class);
            startActivity(intent);
        });

        // ❌ QUIT → Exit App
        btnQuit.setOnClickListener(v -> {
            finishAffinity(); // closes all activities
            System.exit(0);
        });

        ///////init mission chest
        MissionChestManager chestManager = new MissionChestManager(this);
        chestManager.updateDailyChest();
        //////init dayli haret
        HeartsManager heartsManager = new HeartsManager(this);
        heartsManager.giveDailyHeartsIfNeeded(); // Give 5 hearts if not given today

        MusicManager.init(this);
    }
    @Override
    protected void onResume() {
        super.onResume();
        MusicManager.resume();
    }

}