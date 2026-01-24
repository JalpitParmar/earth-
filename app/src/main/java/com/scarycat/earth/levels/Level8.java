package com.scarycat.earth.levels;

import android.os.Bundle;

import com.scarycat.earth.base.BaseLevelActivity;

public class Level8 extends BaseLevelActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        LEVEL_NUMBER = 8;

        ROWS = 8;
        COLS = 8;
        MOVES = 22;
        TIME_LEFT = 80;
        ALLOWED_CANDIES = new int[]{0,1,2,3,4,5,6,7,8};
        TARGETS = new int[]{10,5,10,0,0,0,0,0,0};
        SPAWN_RATE =new int[]{25,25,20,15,15,0,0,0,0};

        star3 = 700;
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
