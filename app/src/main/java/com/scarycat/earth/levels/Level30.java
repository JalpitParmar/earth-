package com.scarycat.earth.levels;

import android.os.Bundle;

import com.scarycat.earth.base.BaseLevelActivity;

public class Level30 extends BaseLevelActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        LEVEL_NUMBER =30;
        ROWS = 9;
        COLS = 9;
        MOVES = 15;
        TIME_LEFT = 55;
        ALLOWED_CANDIES = new int[]{0,1,2,3,4,5,6};
        TARGETS = new int[]{10,10,9,0,0,0,0};
        SPAWN_RATE =new int[]{18,22,18,17,10,10,5};
        star3 = 1200;
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
