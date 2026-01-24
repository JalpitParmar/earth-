package com.scarycat.earth.levels;

import android.os.Bundle;

import com.scarycat.earth.base.ScoreLevelBaseActivity;

public class Level32 extends ScoreLevelBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LEVEL_NUMBER = 32;

        ROWS = 9;
        COLS = 9;
        TIME_LEFT = 55;
        MOVES = 15;
        TARGET_SCORE = 1000;
        SCORE_ONLY = true;
        ALLOWED_CANDIES = new int[]{0,1,2,3,4,5,6,7};
        SPAWN_RATE =new int[]{0,20,18,16,15,12,10,9};
        star3 = 1300;
        star2 = 820;

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
