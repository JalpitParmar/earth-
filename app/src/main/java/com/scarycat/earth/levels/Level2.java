package com.scarycat.earth.levels;

import android.os.Bundle;
import android.widget.Toast;


import com.scarycat.earth.base.ScoreLevelBaseActivity;

public class Level2 extends ScoreLevelBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LEVEL_NUMBER = 2;

        ROWS = 8;
        COLS = 8;

        MOVES = 20;
        TARGET_SCORE = 1000;
        SCORE_ONLY = true;
        ALLOWED_CANDIES = new int[]{0,1,2,3,4};
        Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void showStep0() {

    }

    @Override
    protected void showStep1() {

    }

    @Override
    protected void showStep2() {

    }
}


