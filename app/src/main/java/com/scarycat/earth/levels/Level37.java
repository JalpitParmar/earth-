package com.scarycat.earth.levels;

import android.os.Bundle;

import com.scarycat.earth.base.BaseLevelActivity;

public class Level37 extends BaseLevelActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        LEVEL_NUMBER =37;
        ROWS = 9;
        COLS = 9;
        MOVES = 13;
        TIME_LEFT = 45;
        ALLOWED_CANDIES = new int[]{0,1,2,3,4,5,6,7};
        TARGETS = new int[]{0,12,12,10,0,0,0,0};
        SPAWN_RATE =new int[]{17,17,15,16,12,12,11};
        star3 = 1350;
        star2 = 850;
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
