package com.scarycat.earth.levels;

import android.os.Bundle;

import com.scarycat.earth.base.BaseLevelActivity;

public class Level21 extends BaseLevelActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        LEVEL_NUMBER =21;
        ROWS = 9;
        COLS = 9;
        MOVES = 18;
        TIME_LEFT = 65;
        ALLOWED_CANDIES = new int[]{0,1,2,3,4,5,6};
        TARGETS = new int[]{10,10,5,0,0,0,0};
        SPAWN_RATE =new int[]{24,22,18,14,10,7,5};
        star3 = 1100;
        star2 = 750;
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
