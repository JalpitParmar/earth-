package com.scarycat.earth.levels;

import android.os.Bundle;
import android.widget.Toast;

import com.scarycat.earth.base.BlockerLevelBaseActivity;
import com.scarycat.earth.R;


public class Level3 extends BlockerLevelBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        LEVEL_NUMBER = 3;
        ROWS = 7;
        COLS = 7;
        MOVES = 25;
        HAS_BLOCKERS = true;


        Toast.makeText(this, "3", Toast.LENGTH_SHORT).show();
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
                {0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0},
                {0,0,3,0,3,0,0},
                {0,0,0,3,0,0,0},
                {0,0,0,0,0,0,0}
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
