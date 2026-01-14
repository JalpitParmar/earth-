package com.scarycat.earth.base;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.scarycat.earth.R;

public abstract class TutorialBaseLevelActivity extends BaseLevelActivity {

    protected TextView txtTutorial;
    protected int tutorialStep = 0;
    protected boolean tutorialCompleted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // 🔒 Tutorial settings BEFORE engine starts
        UNLIMITED_MOVES = true;
        SCORE_ONLY = false;

        super.onCreate(savedInstanceState);

        txtTutorial = findViewById(R.id.txtTutorial);

        startTutorial();
    }

    // ---------- TUTORIAL FLOW ----------
    protected void startTutorial() {
        disableInput = true;
        showStep0();
    }

    protected void nextStep() {
        tutorialStep++;

        switch (tutorialStep) {
            case 1:
                showStep1();
                break;
            case 2:
                showStep2();
                break;
            default:
                finishTutorial();
        }
    }

    protected void finishTutorial() {
        tutorialCompleted = true;
        disableInput = false;

        if (txtTutorial != null) {
            txtTutorial.setVisibility(View.GONE);
        }
    }

    // ---------- STEPS (OVERRIDABLE) ----------
    protected abstract void showStep0();
    protected abstract void showStep1();
    protected abstract void showStep2();
}
