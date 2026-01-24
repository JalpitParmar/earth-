package com.scarycat.earth.levels;

import android.os.Bundle;

import com.scarycat.earth.base.BaseLevelActivity;

public class Level44 extends BaseLevelActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        LEVEL_NUMBER =44;
        ROWS = 9;
        COLS = 9;
        MOVES = 18;
        TIME_LEFT = 60;
        ALLOWED_CANDIES = new int[]{0,1,2,3,4,5,6,7,8};
        TARGETS = new int[]{0,0,11,0,0,5,0,0,0};
        SPAWN_RATE =new int[]{0,0,19,18,16,15,12,11,9};
        star3 = 1000;
        star2 = 820;
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
