package com.scarycat.earth.levels;

import android.os.Bundle;

import com.scarycat.earth.base.ScoreLevelBaseActivity;

public class Level9 extends ScoreLevelBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LEVEL_NUMBER = 9;

        ROWS = 8;
        COLS = 8;
        TIME_LEFT = 65;
        MOVES = 17;
        TARGET_SCORE = 800;
        SCORE_ONLY = true;
        ALLOWED_CANDIES = new int[]{0,1,2,3,4};
        SPAWN_RATE =new int[]{25,25,20,15,15};
        star3 = 900;
        star2 = 650;

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
