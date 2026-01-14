package com.scarycat.earth.base;

public abstract class ScoreLevelBaseActivity extends BaseLevelActivity {

    protected int TARGET_SCORE;



    @Override
    protected void updateTargetText() {
        txtTarget.setText("Target: " + TARGET_SCORE);
    }

    @Override
    protected boolean isLevelComplete() {
        return score >= TARGET_SCORE;
    }

    @Override
    protected boolean hasTargetsRemaining() {
        return false; // 🚫 no candy targets
    }
    @Override
    protected void onScoreUpdated() {
        super.onScoreUpdated();

        if (score >= TARGET_SCORE) {
            onLevelWin(); // ✅ now exists
        }
    }

}