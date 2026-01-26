package com.scarycat.earth.levels;

import android.os.Bundle;

import com.scarycat.earth.R;
import com.scarycat.earth.base.BlockerLevelBaseActivity;

public class Level33 extends BlockerLevelBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        LEVEL_NUMBER = 33;
        ROWS = 9;
        COLS = 9;
        MOVES = 20;
        TIME_LEFT = 70;
        HAS_BLOCKERS = true;
        ICE = new int[] {
                R.drawable.ice_1,
                R.drawable.ice_2,
                R.drawable.ice_3
        };
        ALLOWED_CANDIES = new int[]{0,1,2,3,4,5,6,7,8};
        SPAWN_RATE =new int[]{0,19,18,16,15,12,10,10,0};
        star3 = 1000;
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
                {0,0,2,2,3,2,2,0,0}
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
