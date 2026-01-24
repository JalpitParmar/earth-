package com.scarycat.earth.levels;

import android.os.Bundle;

import com.scarycat.earth.R;
import com.scarycat.earth.base.BlockerLevelBaseActivity;

public class Level16 extends BlockerLevelBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        LEVEL_NUMBER = 16;
        ROWS = 9;
        COLS = 9;
        MOVES = 22;
        TIME_LEFT = 80;
        HAS_BLOCKERS = true;
        ICE = new int[]{
                R.drawable.ice_1,
                R.drawable.ice_2,
                R.drawable.ice_3
        };
        ALLOWED_CANDIES = new int[]{0,1,2,3,4,5,6,7,8};
        SPAWN_RATE =new int[]{25,25,20,15,10,5,0,0,0};
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
    // ✅ PUT initBlockers HERE
    @Override
    protected void initBlockers() {

        TOTAL_BLOCKERS = 0;

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                blockers[r][c] = 0;
            }
        }

        int[][] layout = {
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0},
                {1,1,1,1,1,1,1,1,1}
        };

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {

                if (layout[r][c] > 0) {
                    blockers[r][c] = layout[r][c];
                    TOTAL_BLOCKERS++;

                    views[r][c].setImageResource(R.drawable.ice_3);
                }
            }
        }
        updateTargetText();
    }
}
