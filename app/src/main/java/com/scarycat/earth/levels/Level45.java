package com.scarycat.earth.levels;

import android.os.Bundle;

import com.scarycat.earth.R;
import com.scarycat.earth.base.BlockerLevelBaseActivity;

public class Level45 extends BlockerLevelBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        LEVEL_NUMBER = 45;
        ROWS = 9;
        COLS = 9;
        MOVES = 22;
        TIME_LEFT = 70;
        HAS_BLOCKERS = true;
        ICE = new int[] {
                R.drawable.lightning_1,
                R.drawable.lightning_2,
                R.drawable.lightning_3
        };
        ALLOWED_CANDIES = new int[]{0,1,2,3,4,5,6,7,8};
        SPAWN_RATE =new int[]{0,0,19,18,16,15,12,10,10};
        star3 = 1200;
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
                {0,0,0,1,2,3,0,0,0},
                {0,0,0,3,2,1,0,0,0}
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
