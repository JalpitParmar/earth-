package com.scarycat.earth.levels;

import android.os.Bundle;
import android.widget.Toast;

import com.scarycat.earth.base.BlockerLevelBaseActivity;
import com.scarycat.earth.R;
import com.scarycat.earth.base.ScoreLevelBaseActivity;


public class Level3 extends ScoreLevelBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LEVEL_NUMBER = 3;

        ROWS = 6;
        COLS = 6;
        TIME_LEFT = 80;
        MOVES = 20;
        TARGET_SCORE = 800;
        SCORE_ONLY = true;
        ALLOWED_CANDIES = new int[]{0,1,2,3,4};
        SPAWN_RATE =new int[]{25,25,20,15,15};
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
