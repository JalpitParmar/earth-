package com.scarycat.earth.levels;

import android.os.Bundle;

import com.scarycat.earth.base.ScoreLevelBaseActivity;

public class Level5 extends ScoreLevelBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LEVEL_NUMBER = 5;

        ROWS = 7;
        COLS = 7;
        TIME_LEFT = 90;
        MOVES = 25;
        TARGET_SCORE = 700;
        SCORE_ONLY = true;
        ALLOWED_CANDIES = new int[]{0,1,2,3,4,5,6,7,8};
        SPAWN_RATE =new int[]{25,25,20,15,15,0,0,0,0};
        star3 = 500;
        star2 = 400;

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
