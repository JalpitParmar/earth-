package com.scarycat.earth.levels;

import android.os.Bundle;
import android.widget.Toast;


import com.scarycat.earth.base.ScoreLevelBaseActivity;

public class Level2 extends ScoreLevelBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LEVEL_NUMBER = 2;

        ROWS = 6;
        COLS = 6;
        TIME_LEFT = 90;
        MOVES = 20;
        TARGET_SCORE = 500;
        SCORE_ONLY = true;
        ALLOWED_CANDIES = new int[]{0,1,2,3,4};
        SPAWN_RATE =new int[]{25,25,20,15,15};
        star3 = 600;
        star2 = 500;

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


