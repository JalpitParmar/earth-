package com.scarycat.earth.levels;

import android.os.Bundle;

import com.scarycat.earth.base.BaseLevelActivity;

public class Level6 extends BaseLevelActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        LEVEL_NUMBER = 6;
        ROWS = 7;
        COLS = 7;
        MOVES = 25;
        TIME_LEFT = 90;
        ALLOWED_CANDIES = new int[]{0,1,2,3,4,5,6,7,8};
        TARGETS = new int[]{8,8,0,0,0,0,0,0,0};
        SPAWN_RATE =new int[]{25,25,20,15,15,0,0,0,0};
        star3 = 700;
        star2 = 450;
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
