package com.scarycat.earth.levels;

import android.os.Bundle;

import com.scarycat.earth.base.ScoreLevelBaseActivity;

public class Level22 extends ScoreLevelBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LEVEL_NUMBER = 22;

        ROWS = 9;
        COLS = 9;
        TIME_LEFT = 65;
        MOVES = 18;
        TARGET_SCORE = 850;
        SCORE_ONLY = true;
        ALLOWED_CANDIES = new int[]{0,1,2,3,4,5,6};
        SPAWN_RATE =new int[]{23,23,18,14,10,7,5};
        star3 = 1050;
        star2 = 680;

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
