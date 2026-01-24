package com.scarycat.earth.levels;

import android.os.Bundle;

import com.scarycat.earth.base.BaseLevelActivity;

public class Level24 extends BaseLevelActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        LEVEL_NUMBER =24;
        ROWS = 9;
        COLS = 9;
        MOVES = 22;
        TIME_LEFT = 80;
        ALLOWED_CANDIES = new int[]{0,1,2,3,4,5,6,7,8};
        TARGETS = new int[]{8,8,7,0,0,0,0,0,0};
        SPAWN_RATE =new int[]{22,22,18,15,10,8,5,0,0};
        star3 = 900;
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
