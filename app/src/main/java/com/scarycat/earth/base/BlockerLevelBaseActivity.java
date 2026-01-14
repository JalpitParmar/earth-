package com.scarycat.earth.base;

import android.os.Bundle;

public abstract class BlockerLevelBaseActivity extends BaseLevelActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        HAS_BLOCKERS = true;
        SCORE_ONLY = false;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected boolean isLevelComplete() {
        return TOTAL_BLOCKERS <= 0;
    }

    @Override
    protected boolean hasTargetsRemaining() {
        return TOTAL_BLOCKERS > 0;
    }

    @Override
    protected void onScoreUpdated() {
        // score is secondary in blocker level

    }

    @Override
    protected void updateTargetText() {
        txtTarget.setText("Blockers: " + TOTAL_BLOCKERS);
    }
}
