package com.scarycat.earth.levels;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.scarycat.earth.base.TutorialBaseLevelActivity;

public class Level1 extends TutorialBaseLevelActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        LEVEL_NUMBER = 1;

        ROWS = 6;
        COLS = 6;
        MOVES = 20;


        ALLOWED_CANDIES = new int[]{0,1,2,3};
        TARGETS = new int[]{10,0,0,0,0};


        super.onCreate(savedInstanceState);
    }

    @Override
    protected void showStep0() {
        txtTutorial.setText("Welcome! Let’s learn how to play.");
        txtTutorial.setVisibility(View.VISIBLE);

        handler.postDelayed(this::nextStep, 1200);
    }

    @Override
    protected void showStep1() {
        txtTutorial.setText("Swipe these candies to match 3!");

        // Highlight a guaranteed move
        glow(3, 2, 3, 3);
        disableInput = false;
    }

    @Override
    protected void showStep2() {
        txtTutorial.setText("Great! Matches clear candies 🎉");

        handler.postDelayed(this::finishTutorial, 1500);

    }
}


