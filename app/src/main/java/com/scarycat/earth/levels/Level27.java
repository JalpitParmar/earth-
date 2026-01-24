package com.scarycat.earth.levels;

import android.os.Bundle;

import com.scarycat.earth.base.BaseLevelActivity;

public class Level27 extends BaseLevelActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        LEVEL_NUMBER =27;
        ROWS = 9;
        COLS = 9;
        MOVES = 22;
        TIME_LEFT = 70;
        ALLOWED_CANDIES = new int[]{0,1,2,3,4,5,6,7,8};
        TARGETS = new int[]{9,9,8,0,0,0,0,0,0};
        SPAWN_RATE =new int[]{20,22,18,16,10,9,5,0,0};
        star3 = 1000;
        star2 = 720;
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
