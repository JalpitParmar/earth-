package com.scarycat.earth.levels;

import android.os.Bundle;

import com.scarycat.earth.base.BaseLevelActivity;

public class Level4 extends BaseLevelActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        LEVEL_NUMBER = 4;

        ROWS = 7;
        COLS = 7;
        MOVES = 25;
        TIME_LEFT = 90;
        ALLOWED_CANDIES = new int[]{0,1,2,3,4,5,6,7,8};
        TARGETS = new int[]{7,7,0,0,0,0,0,0,0};
        SPAWN_RATE =new int[]{34,20,24,16,6,0,0,0,0};

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
