package com.scarycat.earth.levels;

import android.os.Bundle;

import com.scarycat.earth.base.BaseLevelActivity;

public class Level20 extends BaseLevelActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        LEVEL_NUMBER =20;
        ROWS = 9;
        COLS = 9;
        MOVES = 22;
        TIME_LEFT = 80;
        ALLOWED_CANDIES = new int[]{0,1,2,3,4,5,6,7,8};
        TARGETS = new int[]{10,0,5,10,0,0,0,0,0};
        SPAWN_RATE =new int[]{30,20,20,15,10,5,0,0,0};
        star3 = 900;
        star2 = 750;
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
