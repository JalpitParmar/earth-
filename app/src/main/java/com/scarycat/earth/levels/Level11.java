package com.scarycat.earth.levels;

import android.os.Bundle;

import com.scarycat.earth.base.BaseLevelActivity;

public class Level11 extends BaseLevelActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        LEVEL_NUMBER =11;

        ROWS = 8;
        COLS = 8;
        MOVES = 15;
        TIME_LEFT = 55;
        ALLOWED_CANDIES = new int[]{0,1,2,3,4,5};
        TARGETS = new int[]{5,5,5,0,0,0};
        SPAWN_RATE =new int[]{30,20,20,15,10,5};

        star3 = 900;
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
