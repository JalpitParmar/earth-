package com.scarycat.earth.levels;

import android.os.Bundle;

import com.scarycat.earth.base.BaseLevelActivity;

public class Level34 extends BaseLevelActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        LEVEL_NUMBER =34;
        ROWS = 9;
        COLS = 9;
        MOVES = 14;
        TIME_LEFT = 50;
        ALLOWED_CANDIES = new int[]{0,1,2,3,4,5,6,7};
        TARGETS = new int[]{0,11,11,9,0,0,0,0};
        SPAWN_RATE =new int[]{19,18,16,15,12,11,9};
        star3 = 1300;
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
