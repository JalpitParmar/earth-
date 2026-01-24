package com.scarycat.earth.levels;

import android.os.Bundle;

import com.scarycat.earth.base.ScoreLevelBaseActivity;

public class Level12 extends ScoreLevelBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LEVEL_NUMBER = 12;

        ROWS = 8;
        COLS = 8;
        TIME_LEFT = 80;
        MOVES = 22;
        TARGET_SCORE = 700;
        SCORE_ONLY = true;
        ALLOWED_CANDIES = new int[]{0,1,2,3,4,5,6,7,8};
        SPAWN_RATE =new int[]{25,25,20,15,10,5,0,0,0};
        star3 = 900;
        star2 = 560;

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
