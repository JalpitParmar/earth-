package com.scarycat.earth.levels;

import android.os.Bundle;

import com.scarycat.earth.base.ScoreLevelBaseActivity;

public class Level28 extends ScoreLevelBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LEVEL_NUMBER = 28;

        ROWS = 9;
        COLS = 9;
        TIME_LEFT = 55;
        MOVES = 16;
        TARGET_SCORE = 950;
        SCORE_ONLY = true;
        ALLOWED_CANDIES = new int[]{0,1,2,3,4,5,6};
        SPAWN_RATE =new int[]{19,23,18,16,10,9,5};
        star3 = 1200;
        star2 = 760;

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
