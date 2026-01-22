package com.scarycat.earth.levels;

import android.os.Bundle;

import com.scarycat.earth.base.BaseLevelActivity;

public class Level7 extends BaseLevelActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        LEVEL_NUMBER = 7;

        ROWS = 8;
        COLS = 8;
        MOVES = 16;
        TIME_LEFT = 65;
        ALLOWED_CANDIES = new int[]{0,1,2,3};
        TARGETS = new int[]{0,0,10,8,0};
        SPAWN_RATE =new int[]{25,25,20,15,15};

        star3 = 1100;
        star2 = 1000;
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
