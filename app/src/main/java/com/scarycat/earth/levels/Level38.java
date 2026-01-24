package com.scarycat.earth.levels;

import android.os.Bundle;

import com.scarycat.earth.base.ScoreLevelBaseActivity;

public class Level38 extends ScoreLevelBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LEVEL_NUMBER = 38;

        ROWS = 9;
        COLS = 9;
        TIME_LEFT = 45;
        MOVES = 13;
        TARGET_SCORE = 1200;
        SCORE_ONLY = true;
        ALLOWED_CANDIES = new int[]{0,1,2,3,4,5,6,7};
        SPAWN_RATE =new int[]{17,16,15,16,12,12,12};
        star3 = 1500;
        star2 = 920;

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
