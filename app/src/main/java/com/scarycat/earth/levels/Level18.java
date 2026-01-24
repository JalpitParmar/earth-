package com.scarycat.earth.levels;

import android.os.Bundle;

import com.scarycat.earth.base.ScoreLevelBaseActivity;

public class Level18 extends ScoreLevelBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LEVEL_NUMBER = 18;

        ROWS = 9;
        COLS = 9;
        TIME_LEFT = 60;
        MOVES = 16;
        TARGET_SCORE = 800;
        SCORE_ONLY = true;
        ALLOWED_CANDIES = new int[]{0,1,2,3,4};
        SPAWN_RATE =new int[]{25,25,20,15,15};
        star3 = 1000;
        star2 = 640;

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
