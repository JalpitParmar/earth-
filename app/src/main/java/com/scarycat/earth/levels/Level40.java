package com.scarycat.earth.levels;

import android.os.Bundle;

import com.scarycat.earth.base.BaseLevelActivity;

public class Level40 extends BaseLevelActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        LEVEL_NUMBER =40;
        ROWS = 9;
        COLS = 9;
        MOVES = 26;
        TIME_LEFT = 60;
        ALLOWED_CANDIES = new int[]{0,1,2,3,4,5,6,7,8};
        TARGETS = new int[]{0,10,10,8,0,0,0,0,0};
        SPAWN_RATE =new int[]{0,16,15,15,16,13,13,12,0};
        star3 = 1200;
        star2 = 900;
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
