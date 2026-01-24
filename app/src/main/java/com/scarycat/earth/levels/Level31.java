package com.scarycat.earth.levels;

import android.os.Bundle;

import com.scarycat.earth.base.BaseLevelActivity;

public class Level31 extends BaseLevelActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        LEVEL_NUMBER =31;
        ROWS = 9;
        COLS = 9;
        MOVES = 15;
        TIME_LEFT = 55;
        ALLOWED_CANDIES = new int[]{0,1,2,3,4,5,6,7};
        TARGETS = new int[]{0,10,10,8,0,0,0,0,0,0,0,0,0,0};
        SPAWN_RATE =new int[]{0,20,18,16,14,12,10,10};
        star3 = 1250;
        star2 = 800;
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
