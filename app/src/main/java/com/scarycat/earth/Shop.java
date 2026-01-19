package com.scarycat.earth;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.scarycat.earth.utils.PreferencesManager;

public class Shop extends AppCompatActivity {
    private TextView tvCoins, tvHearts, tvGoldBars,tvhammer,tvbomb,tvswap,tvcolor_bomb;
    private ImageButton btnBuyHammer, btnBuyBomb, btnBuyBombHammerCombo, btnBuySwap,
            btnBuyColorBomb, btnBuySwapColorBombCombo, btnBuyHeart, btnAddCoins;

    private PreferencesManager prefs;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        );

        prefs = new PreferencesManager(this);

        tvCoins = findViewById(R.id.tvCoins);
        tvHearts = findViewById(R.id.tvHearts);
        tvGoldBars = findViewById(R.id.tvGoldBars);
        tvhammer = findViewById(R.id.tvHammer);
        tvbomb = findViewById(R.id.tvBomb);
        tvcolor_bomb = findViewById(R.id.tvColorBomb);
        tvswap = findViewById(R.id.tvSwap);

        btnBuyHammer = findViewById(R.id.btnBuyHammer);
        btnBuyBomb = findViewById(R.id.btnBuyBomb);
        btnBuyBombHammerCombo = findViewById(R.id.btnBuyBombHammerCombo);
        btnBuySwap = findViewById(R.id.btnBuySwap);
        btnBuyColorBomb = findViewById(R.id.btnBuyColorBomb);
        btnBuySwapColorBombCombo = findViewById(R.id.btnBuySwapColorBombCombo);
        btnBuyHeart = findViewById(R.id.btnBuyHeart);
        btnAddCoins = findViewById(R.id.btnAddCoins);


        updateCurrencyDisplay();

        // Button click listeners
        btnBuyHammer.setOnClickListener(v -> buyBooster("hammer", 100));
        btnBuyBomb.setOnClickListener(v -> buyBooster("bomb", 200));
        btnBuyBombHammerCombo.setOnClickListener(v -> buyComboBoosters(new String[]{"bomb", "hammer"}, 400));
        btnBuySwap.setOnClickListener(v -> buyBooster("swap", 200));
        btnBuyColorBomb.setOnClickListener(v -> buyBooster("color_bomb", 300));
        btnBuySwapColorBombCombo.setOnClickListener(v -> buyComboBoosters(new String[]{"swap", "color_bomb"}, 600));
        btnBuyHeart.setOnClickListener(v -> buyHeart(500, 1));
        btnAddCoins.setOnClickListener(v -> addCoins(1000)); // free coins button


    }

    private void updateCurrencyDisplay() {
        tvCoins.setText("" + prefs.getInt("coins", 0));
        tvHearts.setText("" + prefs.getInt("hearts", 0));
        tvGoldBars.setText("" + prefs.getInt("goldbars", 0));
        tvhammer.setText("" + prefs.getInt("booster_hammer_count", 0));
        tvbomb.setText("" + prefs.getInt("booster_bomb_count", 0));
        tvswap.setText("" + prefs.getInt("booster_swap_count", 0));
        tvcolor_bomb.setText("" + prefs.getInt("booster_color_bomb_count", 0));
    }

    private boolean spendCoins(int amount) {
        int coins = prefs.getInt("coins", 0);
        if (coins >= amount) {
            prefs.putInt("coins", coins - amount);
            updateCurrencyDisplay();
            return true;
        }
        Toast.makeText(this, "Not enough coins", Toast.LENGTH_SHORT).show();
        return false;
    }

    private void buyBooster(String boosterId, int price) {
        if (spendCoins(price)) {
            int count = prefs.getInt("booster_" + boosterId + "_count", 0);
            prefs.putInt("booster_" + boosterId + "_count", count + 1);
            Toast.makeText(this, "Purchased " + boosterId, Toast.LENGTH_SHORT).show();
            updateCurrencyDisplay();
        }
    }

    private void buyComboBoosters(String[] boosterIds, int price) {
        if (spendCoins(price)) {
            for (String boosterId : boosterIds) {
                int count = prefs.getInt("booster_" + boosterId + "_count", 0);
                prefs.putInt("booster_" + boosterId + "_count", count + 1);
            }
            Toast.makeText(this, "Purchased combo boosters", Toast.LENGTH_SHORT).show();
        }
    }

    private void buyHeart(int price, int amount) {
        if (spendCoins(price)) {
            int hearts = prefs.getInt("hearts", 0);
            prefs.putInt("hearts", hearts + amount);
            updateCurrencyDisplay();
            Toast.makeText(this, "Purchased " + amount + " heart(s)", Toast.LENGTH_SHORT).show();
        }
    }

    private void addCoins(int amount) {
        int coins = prefs.getInt("coins", 0);
        prefs.putInt("coins", coins + amount);
        updateCurrencyDisplay();
        Toast.makeText(this, amount + " coins added!", Toast.LENGTH_SHORT).show();
    }
}