package com.scarycat.earth.levels;

import android.os.Bundle;

import com.scarycat.earth.base.ScoreLevelBaseActivity;

public class Level35 extends ScoreLevelBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LEVEL_NUMBER = 35;

        ROWS = 9;
        COLS = 9;
        TIME_LEFT = 50;
        MOVES = 14;
        TARGET_SCORE = 1100;
        SCORE_ONLY = true;
        ALLOWED_CANDIES = new int[]{0,1,2,3,4,5,6,7};
        SPAWN_RATE =new int[]{18,18,16,15,12,11,10};
        star3 = 1400;
        star2 = 880;

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
