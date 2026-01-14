package com.scarycat.earth.base;


import android.os.Bundle;

import com.scarycat.earth.R;

public abstract class TouchBottomLevelBaseActivity extends BaseLevelActivity {

    protected int movesLeft;
    protected int targetItemsRemaining;

    // You can set default max moves here or override in child classes
    protected int MAX_MOVES = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        HAS_BLOCKERS = false;   // No blockers in this level type
        SCORE_ONLY = false;
        super.onCreate(savedInstanceState);

        movesLeft = MAX_MOVES;
        targetItemsRemaining = countTargetItemsOnBoard();
    }

    // Count how many target items are on the board at the start
    protected int countTargetItemsOnBoard() {
        int count = 0;
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (isTargetItem(r, c)) {
                    count++;
                }
            }
        }
        return count;
    }

    // Check if a cell contains a target item
    protected boolean isTargetItem(int row, int col) {
        // Replace with your actual target item condition
        // For example, if target items are type 7
        return board[row][col] == 7;
    }

    // Called after each move to update moves and check win/lose conditions
    protected void onMoveMade() {
        movesLeft--;

        checkTargetsAtBottom();

        if (isLevelComplete()) {
            onLevelWin();
        } else if (movesLeft <= 0) {
            onLevelFail();
        } else {
            updateMovesLeftUI();
            updateTargetText();
        }
    }

    // Check if target items have reached the bottom row
    protected void checkTargetsAtBottom() {
        for (int c = 0; c < COLS; c++) {
            if (isTargetItem(ROWS - 1, c)) {
                // Mark target as reached (remove it from board)
                removeTargetAt(ROWS - 1, c);
                targetItemsRemaining--;
            }
        }
    }

    // Remove the target item from board and UI (mark as empty)
    protected void removeTargetAt(int row, int col) {
        board[row][col] = -1; // or your empty cell constant
        views[row][col].setImageResource(R.drawable.candy_striped_h); // set empty image
    }

    @Override
    protected boolean isLevelComplete() {
        return targetItemsRemaining <= 0;
    }

    @Override
    protected boolean hasTargetsRemaining() {
        return targetItemsRemaining > 0;
    }

    // Override this to update your target UI
    @Override
    protected void updateTargetText() {
        txtTarget.setText("Targets Remaining: " + targetItemsRemaining);
    }

    // Update UI element for moves left (implement as needed)
    protected void updateMovesLeftUI() {
        txtMoves.setText("Moves Left: " + movesLeft);
    }

    // Called when player wins the level
    protected void onLevelWin() {
        // show win UI, save progress, etc.
        showLevelCompleteDialog();
    }

    // Called when player fails the level (runs out of moves)
    protected void onLevelFail() {
        // show fail UI, offer retry, etc.
        showGameOverDialog();
    }
}

