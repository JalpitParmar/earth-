package com.scarycat.earth.levels;

import android.os.Bundle;

import com.scarycat.earth.base.BaseLevelActivity;

public class Level17 extends BaseLevelActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        LEVEL_NUMBER =17;
        ROWS = 9;
        COLS = 9;
        MOVES = 16;
        TIME_LEFT = 60;
        ALLOWED_CANDIES = new int[]{0,1,2,3,4,5};
        TARGETS = new int[]{10,10,0,10,0,0};
        SPAWN_RATE =new int[]{30,20,20,15,10,5};
        star3 = 1050;
        star2 = 700;
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
