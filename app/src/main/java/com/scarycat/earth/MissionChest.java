package com.scarycat.earth;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.scarycat.earth.utils.ChestTier;
import com.scarycat.earth.utils.MissionChestManager;
import com.scarycat.earth.utils.PreferencesManager;
import com.scarycat.earth.utils.RewardChance;

public class MissionChest extends AppCompatActivity {

    private static final int MAX_WINS = 5;

    private TextView tvProgress, tvReward, tvChestTier;
    private ProgressBar progressBar;
    private ImageButton btnOpenChest;
    private ImageView imgChest,imgname;
    private LinearLayout containerProbabilities;
    private PreferencesManager prefs;
    private MissionChestManager mission;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission_chest);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        );

        mission = new MissionChestManager(this);
        prefs = new PreferencesManager(this);
        tvProgress = findViewById(R.id.tvProgress);
        tvReward = findViewById(R.id.tvReward);
        imgname = findViewById(R.id.name);
//        tvChestTier = findViewById(R.id.tvChestTier);

        progressBar = findViewById(R.id.progressBar);
        btnOpenChest = findViewById(R.id.btnOpenChest);
        imgChest = findViewById(R.id.imgChest);
        containerProbabilities = findViewById(R.id.containerProbabilities);

        progressBar.setMax(MAX_WINS);
        btnOpenChest.setOnClickListener(v -> openChest());

        updateUI();
    }
    @Override
    protected void onResume() {
        super.onResume();
        updateUI();

    }
    private void openChest() {

        if (!mission.isChestReady()) {
            Toast.makeText(this,
                    "Win 5 levels today to unlock!",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        MissionChestManager.ChestReward reward = mission.openChest();

        if (reward != null) {
            ChestTier tier = mission.getCurrentTier();
            if(tier.name()=="BRONZE"){
                imgChest.setImageResource(R.drawable.bronze_chest_open);
                imgname.setImageResource(R.drawable.bronze_name);
            } else if (tier.name()=="SILVER") {
                imgChest.setImageResource(R.drawable.silver_chest_open);
                imgname.setImageResource(R.drawable.silver_name);
            } else if (tier.name()=="GOLD") {
                imgChest.setImageResource(R.drawable.gold_chest_open);
                imgname.setImageResource(R.drawable.gold_name);
            } else if (tier.name()=="EPIC") {
                imgChest.setImageResource(R.drawable.epic_chest_open);
                imgname.setImageResource(R.drawable.epic_name);
            } else if (tier.name()=="MYTHIC") {
                imgChest.setImageResource(R.drawable.mythic_chest_open);
                imgname.setImageResource(R.drawable.mythic_name);
            } else {
                imgChest.setImageResource(R.drawable.bronze_chest_open);
                imgname.setImageResource(R.drawable.bronze_name);
            }
            tvReward.setText("🎁 " + reward.amount + " " + reward.type);
            applyReward(reward.type,reward.amount);
            mission.consumeChest();
            btnOpenChest.setEnabled(false);
        }
    }
    private void applyReward(String reward,int amount) {

        switch (reward) {
            case "Coins":
                prefs.putInt("coins", prefs.getInt("coins", 0) + amount);
                break;
            case "Hammer":
                prefs.putInt("booster_hammer_count",
                        prefs.getInt("booster_hammer_count", 0) + amount);
                break;
            case "Bomb":
                prefs.putInt("booster_bomb_count",
                        prefs.getInt("booster_bomb_count", 0) + amount);
                break;
            case "Heart":
                prefs.putInt("hearts", prefs.getInt("hearts", 0) + amount);
                break;
            case "Swap":
                prefs.putInt("booster_swap_count",
                        prefs.getInt("booster_swap_count", 0) + amount);
                break;
            case "Color Bomb":
                prefs.putInt("booster_color_bomb_count",
                        prefs.getInt("booster_color_bomb_count", 0) + amount);
                break;
            case "Gold Bars":
                prefs.putInt("goldbars", prefs.getInt("goldbars", 0) + amount);
                break;
        }
    }
    private void updateUI() {

        int wins = mission.getWinsToday();
        progressBar.setProgress(wins);
        tvProgress.setText("Wins Today: " + wins + " / " + MAX_WINS);

        ChestTier tier = mission.getCurrentTier();
//        tvChestTier.setText("Chest Tier: " + tier.name());
        
        if(tier.name()=="BRONZE"){
            imgChest.setImageResource(
                    mission.isChestReady()
                            ? R.drawable.bronze_chest_closed
                            : R.drawable.bronze_chest_open
            );
            imgname.setImageResource(R.drawable.bronze_name);

        } else if (tier.name()=="SILVER") {
            imgChest.setImageResource(
                    mission.isChestReady()
                            ? R.drawable.silver_chest_closed
                            : R.drawable.silver_chest_open
            );
            imgname.setImageResource(R.drawable.silver_name);
        } else if (tier.name()=="GOLD") {
            imgChest.setImageResource(
                    mission.isChestReady()
                            ? R.drawable.gold_chest_closed
                            : R.drawable.gold_chest_open
            );
            imgname.setImageResource(R.drawable.gold_name);
        } else if (tier.name()=="EPIC") {
            imgChest.setImageResource(
                    mission.isChestReady()
                            ? R.drawable.epic_chest_closed
                            : R.drawable.epic_chest_open
            );
            imgname.setImageResource(R.drawable.epic_name);
        } else if (tier.name()=="MYTHIC") {
            imgChest.setImageResource(
                    mission.isChestReady()
                            ? R.drawable.mythic_chest_closed
                            : R.drawable.mythic_chest_open
            );
            imgname.setImageResource(R.drawable.mythic_name);
        } else {
            imgChest.setImageResource(
                    mission.isChestReady()
                            ? R.drawable.bronze_chest_closed
                            : R.drawable.bronze_chest_open
            );
            imgname.setImageResource(R.drawable.mythic_name);
        }


        btnOpenChest.setEnabled(mission.isChestReady());

        tvReward.setText(
                mission.isChestReady()
                        ? "Chest Ready to Open!"
                        : "Chest Locked 🔒"
        );

        showRewardProbabilities();
    }
    
    private void showRewardProbabilities() {

        containerProbabilities.removeAllViews();

        RewardChance[] chances =
                mission.getRewardChancesForTier();

        for (RewardChance c : chances) {

            View row = getLayoutInflater()
                    .inflate(R.layout.item_reward_probability,
                            containerProbabilities,
                            false);

            TextView name = row.findViewById(R.id.tvRewardName);
            TextView percent = row.findViewById(R.id.tvRewardPercent);

            name.setText(c.reward);
            percent.setText(c.percent + "%");

            containerProbabilities.addView(row);
        }
    }
}
