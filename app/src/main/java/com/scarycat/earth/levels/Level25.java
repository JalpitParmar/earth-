package com.scarycat.earth.levels;

import android.os.Bundle;

import com.scarycat.earth.base.ScoreLevelBaseActivity;

public class Level25 extends ScoreLevelBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LEVEL_NUMBER = 25;

        ROWS = 9;
        COLS = 9;
        TIME_LEFT = 60;
        MOVES = 17;
        TARGET_SCORE = 900;
        SCORE_ONLY = true;
        ALLOWED_CANDIES = new int[]{0,1,2,3,4,5,6};
        SPAWN_RATE =new int[]{21,23,18,15,10,8,5};
        star3 = 1150;
        star2 = 720;

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
