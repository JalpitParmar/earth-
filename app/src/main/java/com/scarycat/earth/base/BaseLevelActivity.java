package com.scarycat.earth.base;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import android.view.MotionEvent;
import android.app.Dialog;
import android.content.Intent;
import android.view.Window;
import android.widget.Button;
import android.graphics.Rect;
import android.view.ViewGroup;
import android.widget.Toast;


import com.scarycat.earth.Meanu;
import com.scarycat.earth.R;
import com.scarycat.earth.utils.HeartsManager;
import com.scarycat.earth.utils.LevelRouter;

import com.scarycat.earth.boosters.BoosterType;
import com.scarycat.earth.boosters.BoosterManager;
import com.scarycat.earth.utils.MissionChestManager;


import com.scarycat.earth.utils.MusicManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import java.util.Random;

public abstract class BaseLevelActivity extends AppCompatActivity {


    // ================= LEVEL CONFIG =================
    protected int LEVEL_NUMBER = 1;
    boolean levelFinished = false;

    boolean gameOver = false;

    protected int ROWS = 6;          // change grid height
    protected int COLS = 6;          // change grid width
    protected int MOVES = 5;        // total moves

    // Allowed candies in this level
    // 0=Red, 1=Blue, 2=Green, 3=Yellow, 4=Purple
    protected int[] ALLOWED_CANDIES = {0, 1, 2, 3};

    protected int[] TARGETS = {5, 5, 0, 0, 0};
    // ===============================================

    GridLayout grid;
    TextView txtLevel, txtMoves, txtTarget, txtScore;

    protected ImageView[][] views;
    protected int[][] board;

    protected int score = 0;
    float startX, startY;

    protected Handler handler = new Handler();
    Random random = new Random();

    int[] candyImages = {
            R.drawable.e1,
            R.drawable.e2,
            R.drawable.e3,
            R.drawable.e4,
            R.drawable.e5
    };

    int[] SPAWN_RATE = {
            40, // Red
            30, // Blue
            20, // Green
            10, // Yellow
            0   // Purple (0 = never spawn)
    };
    // ================= TIMER =================
    Handler timerHandler = new Handler();
    Runnable timerRunnable;

    int TIME_LEFT = 60;
    boolean timerRunning = false;
    boolean isPaused = false;

    TextView txtTime;
    Button btnPause;


    // ================= COMBO SYSTEM =================
    int comboCount = 0;
    float comboMultiplier = 1f;
    TextView txtCombo;
    ProgressBar progressTargets;

    int TOTAL_TARGET_COUNT = 0;

    int lastProgress = 0;

    Handler hintHandler = new Handler();
    Runnable hintRunnable;

    boolean hintActive = false;
    protected boolean disableInput = false;
    int BONUS_PER_MOVE = 10;
    boolean bonusApplied = false;
    int star3 = 300;//
    int star2 = 200;//

    protected boolean UNLIMITED_MOVES = false;
    protected boolean SHOW_TUTORIAL = false;
    protected boolean HAS_BLOCKERS = false;
    protected boolean HAS_DROP_ITEMS = false;
    protected boolean SCORE_ONLY = false;

    // ============ BLOCKERS ============
    protected int[][] blockers;   // 0 = none, 1–3 = strength
    protected int TOTAL_BLOCKERS = 0;
    protected static final int CELL_EMPTY = -1;
    protected static final int CELL_BLOCKER = -2;
    boolean[][] blockerDamagedThisMove;
    protected BoosterType activeBooster = BoosterType.NONE;
    ImageView btnHammer, btnShuffle, btnBomb, btnColor;
    TextView txtHammerCount, txtShuffleCount, txtBombCount, txtColorCount;
    FrameLayout effectsLayer;
    private RewardedAd rewardedAd;

    protected int fixedh =25;
    protected int fixedw =25;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_level1);
        MusicManager.pause();

        grid = findViewById(R.id.gridLayout);
        txtLevel = findViewById(R.id.txtLevel);
        txtMoves = findViewById(R.id.txtMoves);
        txtTarget = findViewById(R.id.txtTarget);
        txtScore = findViewById(R.id.txtScore);
        txtTime = findViewById(R.id.txtTime);
        btnPause = findViewById(R.id.btnPause);

        txtCombo = findViewById(R.id.txtCombo);
        progressTargets = findViewById(R.id.progressTargets);

        btnHammer = findViewById(R.id.btnHammer);
        btnShuffle = findViewById(R.id.btnShuffle);
        btnBomb = findViewById(R.id.btnBomb);
        btnColor = findViewById(R.id.btnColor);

        txtHammerCount = findViewById(R.id.txtHammerCount);
        txtShuffleCount = findViewById(R.id.txtShuffleCount);
        txtBombCount = findViewById(R.id.txtBombCount);
        txtColorCount = findViewById(R.id.txtColorCount);
        effectsLayer = findViewById(R.id.effectsLayer);

        TOTAL_TARGET_COUNT = 0;
        for (int t : TARGETS) {
            TOTAL_TARGET_COUNT += t;
        }
        updatebossterui();


        btnPause.setOnClickListener(v1 -> togglePause());

        txtLevel.setText(""+ LEVEL_NUMBER);
        txtMoves.setText(""+ MOVES);
        txtScore.setText("0");


        views = new ImageView[ROWS][COLS];
        board = new int[ROWS][COLS];

        //            LEVEL_NUMBER = getIntent().getIntExtra("LEVEL", 1);
        //            txtLevel.setText("Level " + LEVEL_NUMBER);

        updateTargetText();
        btnHammer.setOnClickListener(v -> {
            if (BoosterManager.get(this, BoosterType.HAMMER) > 0) {
                toggleBooster(BoosterType.HAMMER);
                updatebossterui();
            } else {
                Toast.makeText(this, "No Hammer boosters!", Toast.LENGTH_SHORT).show();
            }
        });
        btnHammer.setOnClickListener(v -> {
            if (BoosterManager.get(this, BoosterType.HAMMER) > 0) {
                toggleBooster(BoosterType.HAMMER);
                updatebossterui();
            } else {
                Toast.makeText(this, "No Hammer boosters!", Toast.LENGTH_SHORT).show();
            }
        });

        btnBomb.setOnClickListener(v -> {
            if (BoosterManager.get(this, BoosterType.BOMB) > 0) {
                toggleBooster(BoosterType.BOMB);
                updatebossterui();
            } else {
                Toast.makeText(this, "No Bomb boosters!", Toast.LENGTH_SHORT).show();
            }
        });

        btnShuffle.setOnClickListener(v -> {
            if (BoosterManager.get(this, BoosterType.SHUFFLE) > 0) {
                toggleBooster(BoosterType.SHUFFLE);
                updatebossterui();
            } else {
                Toast.makeText(this, "No Shuffle boosters!", Toast.LENGTH_SHORT).show();
            }
        });

        btnColor.setOnClickListener(v -> {
            if (BoosterManager.get(this, BoosterType.COLOR_BOMB) > 0) {
                toggleBooster(BoosterType.COLOR_BOMB);
                updatebossterui();
            } else {
                Toast.makeText(this, "No Color Bomb boosters!", Toast.LENGTH_SHORT).show();
            }
        });

        initBoard();
        if (HAS_BLOCKERS) {
            blockers = new int[ROWS][COLS];
            blockerDamagedThisMove = new boolean[ROWS][COLS];
            initBlockers();
        }

        removeInitialMatches();
        if (!hasPossibleMoves()) {
            shuffleBoardAnimated();
        }
        startHintTimer();
        startTimer();
        loadRewardedAd();

    }

    @Override
    protected void onResume() {
        super.onResume();
        updatebossterui();
        if (!gameOver && !isPaused) {
            startTimer();
            enableBoardInput(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        handler.removeCallbacksAndMessages(null);
        hintHandler.removeCallbacksAndMessages(null);
        timerHandler.removeCallbacksAndMessages(null);
    }

    // ---------------- GRID INIT ----------------
    void initBoard() {
        grid.removeAllViews();
        grid.setRowCount(ROWS);
        grid.setColumnCount(COLS);

        int size = getResources().getDisplayMetrics().widthPixels / COLS;

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                ImageView img = new ImageView(this);

                GridLayout.LayoutParams p = new GridLayout.LayoutParams();
                p.width = size- fixedw;
                p.height = size- fixedh;
                img.setLayoutParams(p);

                board[r][c] = randomCandy();
                img.setImageResource(candyImages[board[r][c]]);

                int fr = r, fc = c;
                img.setOnTouchListener((v, e) -> swipe(e, fr, fc));

                views[r][c] = img;
                grid.addView(img);
            }
        }
    }

    int randomCandy() {

        int totalWeight = 0;

        // Sum weights only for allowed candies
        for (int c : ALLOWED_CANDIES) {
            totalWeight += SPAWN_RATE[c];
        }

        int rand = random.nextInt(totalWeight);
        int running = 0;

        for (int c : ALLOWED_CANDIES) {
            running += SPAWN_RATE[c];
            if (rand < running) {
                return c;
            }
        }

        return ALLOWED_CANDIES[0]; // fallback
    }


    // ---------------- SWIPE ----------------
    boolean swipe(MotionEvent e, int r, int c) {

        if (e.getAction() == MotionEvent.ACTION_UP) {

            float dx = Math.abs(e.getX() - startX);
            float dy = Math.abs(e.getY() - startY);

            // This is a TAP, not a swipe
            if (dx < 20 && dy < 20) {
                onCellTapped(r, c);
                return true;
            }
        }

        if (disableInput || isPaused || gameOver || levelFinished) {
            return true;
        }
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            startX = e.getX();
            startY = e.getY();
            return true;
        }

        if (e.getAction() == MotionEvent.ACTION_UP) {
            float dx = e.getX() - startX;
            float dy = e.getY() - startY;

            if (Math.abs(dx) > Math.abs(dy)) {
                if (dx > 50 && c < COLS - 1) trySwap(r, c, r, c + 1);
                else if (dx < -50 && c > 0) trySwap(r, c, r, c - 1);
            } else {
                if (dy > 50 && r < ROWS - 1) trySwap(r, c, r + 1, c);
                else if (dy < -50 && r > 0) trySwap(r, c, r - 1, c);
            }
        }
        hintActive = false;
        hintHandler.removeCallbacksAndMessages(null);
        return true;
    }

    void trySwap(int r1, int c1, int r2, int c2) {

        if (levelFinished || gameOver) return;
        if (HAS_BLOCKERS) {
            if (blockers[r1][c1] > 0 || blockers[r2][c2] > 0)
                return;
        }
        swap(r1, c1, r2, c2);

        handler.postDelayed(() -> {
            if (hasMatch()) {
                MOVES--;
                resetBlockerDamageFlags();
                txtMoves.setText("" + MOVES);
                comboCount = 0;
                comboMultiplier = 1f;
                processBoard();
                checkGameOver();
                if (MOVES <= 0) checkGameOver();
            } else {
                swap(r1, c1, r2, c2);
            }
        }, 150);
        hintActive = false;
        hintHandler.removeCallbacksAndMessages(null);

    }


    boolean swap(int r1, int c1, int r2, int c2) {
        if (gameOver || levelFinished) return false;
        int t = board[r1][c1];
        board[r1][c1] = board[r2][c2];
        board[r2][c2] = t;
        refresh();
        return true;
    }

    // ---------------- GAME LOOP ----------------
    void processBoard() {
        if (engineFrozen) return;

        if (this instanceof TutorialBaseLevelActivity) {
            TutorialBaseLevelActivity t = (TutorialBaseLevelActivity) this;
            if (!t.tutorialCompleted) {
                t.nextStep();
            }
        }

        if (destroyMatches()) {

            comboCount++;
            updateCombo();

            handler.postDelayed(() -> {
                gravity();
                handler.postDelayed(this::processBoard, 200);
            }, 200);

        } else {
            // 🟢 Board is stable now

            // 🔄 CHECK FOR POSSIBLE MOVES
            if (!hasPossibleMoves()) {
                shuffleBoardAnimated();
                return; // important: stop further logic
            }

            startHintTimer();
        }
    }


    void updateCombo() {
        if (comboCount <= 1) return;

        switch (comboCount) {
            case 2:
                comboMultiplier = 1.5f;
                showComboText("Sweet!", "COMBO x1.5");
                break;
            case 3:
                comboMultiplier = 2f;
                showComboText("Awesome!", "COMBO x2");
                break;
            case 4:
                comboMultiplier = 2.5f;
                showComboText("Unstoppable!", "COMBO x2.5");
                break;
            default:
                comboMultiplier = 3f;
                showComboText("Savage!", "COMBO x3");
                break;
        }
    }

    boolean hasMatch() {
        for (int r = 0; r < ROWS; r++)
            for (int c = 0; c < COLS - 2; c++)
                if (board[r][c] == board[r][c + 1] &&
                        board[r][c] == board[r][c + 2])
                    return true;

        for (int c = 0; c < COLS; c++)
            for (int r = 0; r < ROWS - 2; r++)
                if (board[r][c] == board[r + 1][c] &&
                        board[r][c] == board[r + 2][c])
                    return true;

        return false;
    }

    boolean destroyMatches() {
        boolean found = false;

        for (int r = 0; r < ROWS; r++) {
            int count = 1;
            for (int c = 1; c < COLS; c++) {
                if (board[r][c] == board[r][c - 1] && board[r][c] != -1)
                    count++;
                else {
                    found |= handleMatch(count, r, c - 1, true);
                    count = 1;
                }
            }
            found |= handleMatch(count, r, COLS - 1, true);
        }

        for (int c = 0; c < COLS; c++) {
            int count = 1;
            for (int r = 1; r < ROWS; r++) {
                if (board[r][c] == board[r - 1][c] && board[r][c] != -1)
                    count++;
                else {
                    found |= handleMatch(count, r - 1, c, false);
                    count = 1;
                }
            }
            found |= handleMatch(count, ROWS - 1, c, false);
        }
        return found;
    }

    boolean handleMatch(int count, int r, int c, boolean horizontal) {
        if (count < 3) return false;

        int candyType = board[r][c];

        // 🎆 Particle effects
        if (count == 4) {
            spawnCandyExplosion(r, c, candyType, false);
            screenShake();
            clearRow(r);
            clearColumn(c);
            return true;
        }

        if (count >= 5) {
            spawnCandyExplosion(r, c, candyType, true);
            screenShake();
            screenShake();
            clear4x4(r, c);
            return true;
        }

        for (int i = 0; i < count; i++) {
            mark(horizontal ? r : r - i, horizontal ? c - i : c);
        }

        return true;
    }


    void clearRow(int r) {
        for (int c = 0; c < COLS; c++)
            mark(r, c);
    }

    void clearColumn(int c) {
        for (int r = 0; r < ROWS; r++)
            mark(r, c);
    }

    void clear4x4(int r, int c) {
        for (int i = r - 1; i <= r + 2; i++) {
            for (int j = c - 1; j <= c + 2; j++) {
                if (i >= 0 && j >= 0 && i < ROWS && j < COLS)
                    mark(i, j);
            }
        }
    }


    void mark(int r, int c) {
        if (r < 0 || c < 0 || r >= ROWS || c >= COLS) return;
        if (board[r][c] < 0) return;


        int candyType = board[r][c];
        board[r][c] = -1;
        damageNearbyBlockers(r, c);

        ImageView img = views[r][c];

        img.animate()
                .scaleX(1.1f)
                .scaleY(1.1f)
                .alpha(0f)
                .rotationBy(10)
                .setDuration(200)
                .withEndAction(() -> {
                    img.setScaleX(1f);
                    img.setScaleY(1f);
                    img.setAlpha(1f);
                    img.setRotation(0f);


                    int baseScore = 10;
                    int gained = Math.round(baseScore * comboMultiplier);
                    addScore(gained);
                    //                    score += gained;
                    //                    txtScore.setText("Score: " + score);

                    if (TARGETS[candyType] > 0) {
                        TARGETS[candyType]--;
                        updateTargetProgress();
                        updateTargetText();
                        checkGameOver();
                    }
                })
                .start();
        if (HAS_BLOCKERS) damageNearbyBlockers(r, c);
    }


    void gravity() {

        for (int c = 0; c < COLS; c++) {

            int writeRow = ROWS - 1;

            for (int r = ROWS - 1; r >= 0; r--) {

                // ⛔ BLOCKER STAYS WHERE IT IS
                if (board[r][c] == CELL_BLOCKER) {
                    writeRow = r - 1;
                    continue;
                }
                if (HAS_BLOCKERS && blockers[r][c] > 0) {
                    writeRow = r - 1;
                    continue;
                }

                // 🍬 MOVE CANDIES ONLY
                if (board[r][c] >= 0) {

                    if (writeRow != r) {
                        board[writeRow][c] = board[r][c];
                        animateFall(r, writeRow, c);
                    }

                    writeRow--;
                }
            }

            // 🎯 SPAWN NEW CANDIES (SKIP BLOCKERS)
            while (writeRow >= 0) {

                if (board[writeRow][c] == CELL_BLOCKER) {
                    writeRow--;
                    continue;
                }

                board[writeRow][c] = randomCandy();
                animateSpawn(writeRow, c);
                writeRow--;
            }
        }

        handler.postDelayed(() -> {
            refresh();
            startHintTimer();
        }, 220);
    }

    void animateFall(int fromRow, int toRow, int col) {
        ImageView img = views[fromRow][col];

        float distance = (toRow - fromRow) * img.getHeight();

        img.animate()
                .translationY(distance)
                .setDuration(200)
                .withEndAction(() -> img.setTranslationY(0))
                .start();
    }

    void animateSpawn(int row, int col) {
        ImageView img = views[row][col];

        img.setTranslationY(-img.getHeight());
        img.animate()
                .translationY(0)
                .setDuration(200)
                .start();
    }


    void refresh() {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {

                // 1️⃣ BLOCKERS FIRST (only if level has blockers)
                if (HAS_BLOCKERS && blockers[r][c] > 0) {

                    if (blockers[r][c] == 3) {
                        views[r][c].setImageResource(R.drawable.ice_3);
                    } else if (blockers[r][c] == 2) {
                        views[r][c].setImageResource(R.drawable.ice_2);
                    } else {
                        views[r][c].setImageResource(R.drawable.ice_1);
                    }

                    continue; // VERY IMPORTANT
                }

                // 2️⃣ NORMAL CANDIES
                int v = board[r][c];
                if (v >= 0 && v < candyImages.length) {
                    views[r][c].setImageResource(candyImages[v]);
                } else {
                    views[r][c].setImageDrawable(null);
                }
            }
        }
    }

    void removeInitialMatches() {
        while (hasMatch())
            for (int r = 0; r < ROWS; r++)
                for (int c = 0; c < COLS; c++)
                    board[r][c] = randomCandy();
        refresh();
    }

    void updateTargetText() {
        String[] names = {"Red", "Blue", "Green", "Yellow", "Purple"};
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < TARGETS.length; i++)
            if (TARGETS[i] > 0)
                sb.append(names[i]).append(" ").append(TARGETS[i]).append("  ");
        txtTarget.setText(sb.toString());
    }

    void checkGameOver() {
        if (levelFinished || gameOver) return;

        // ✅ TARGET-BASED LEVELS ONLY
        if (!SCORE_ONLY) {
            boolean allTargetsDone = true;
            for (int t : TARGETS) {
                if (t > 0) {
                    allTargetsDone = false;
                    break;
                }
            }
            if (HAS_BLOCKERS) {
                if (TOTAL_BLOCKERS <= 0) {
                    onLevelWin();
                }
                return;
            }
            if (allTargetsDone) {
                levelFinished = true;
                applyBonusScore(this::showLevelCompleteDialog);
                return;
            }
        }

        // ❌ SCORE LEVEL DOES NOT AUTO-WIN HERE

        if (MOVES <= 0) {
            gameOver = true;
            stopTimer();
            showGameOverDialog();
        }
    }


    void showLevelCompleteDialog() {
        isPaused = !isPaused;
        new MissionChestManager(this).onLevelWon();

        // Reward coins on win
        SharedPreferences sp = getSharedPreferences("earth_game_prefs", MODE_PRIVATE);
        int coins = sp.getInt("coins", 0);
        int coinsReward = 100; // amount to reward on win
        sp.edit().putInt("coins", coins + coinsReward).apply();

        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_level_complete);
        dialog.setCancelable(false);

        TextView txtScoreFinal = dialog.findViewById(R.id.txtFinalScore);
        ImageView star1 = dialog.findViewById(R.id.star1);
        ImageView star2 = dialog.findViewById(R.id.star2);
        ImageView star3 = dialog.findViewById(R.id.star3);

        Button btnNext = dialog.findViewById(R.id.btnNext);
        Button btnHome = dialog.findViewById(R.id.btnHome);

        int starsEarned = calculateStars();
        txtScoreFinal.setText("" + score);

        // Save best stars and unlock next level
        SharedPreferences.Editor editor = sp.edit();
        int oldStars = sp.getInt("LEVEL_" + LEVEL_NUMBER, 0);
        if (starsEarned > oldStars) {
            editor.putInt("LEVEL_" + LEVEL_NUMBER, starsEarned);
        }
        editor.putBoolean("UNLOCK_" + (LEVEL_NUMBER + 1), true);
        editor.apply();

        // Display stars
        if (starsEarned >= 1) star1.setImageResource(R.drawable.star_filled);
        if (starsEarned >= 2) star2.setImageResource(R.drawable.star_filled);
        if (starsEarned >= 3) star3.setImageResource(R.drawable.star_filled);

        btnNext.setOnClickListener(v -> {
            dialog.dismiss();
            startActivity(LevelRouter.getNextLevel(this, LEVEL_NUMBER));
            finish();
        });

        btnHome.setOnClickListener(v -> {
            dialog.dismiss();
            startActivity(new Intent(this, Meanu.class));
            finish();
            MusicManager.resume();
        });

        dialog.show();
    }

    void showGameOverDialog() {

        pauseTimer();
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_game_over);
        dialog.setCancelable(false);

        TextView txtScoreFinal = dialog.findViewById(R.id.txtGameOverScore);
        Button btnRetry = dialog.findViewById(R.id.btnRetry);
        Button btnHome = dialog.findViewById(R.id.btnHome);
        Button btnWatchAd = dialog.findViewById(R.id.btnWatchAd);

        txtScoreFinal.setText("" + score);

        // ❌ DO NOT REMOVE HEART YET
        HeartsManager heartsManager = new HeartsManager(this);

        // ---------------- RETRY ----------------
        btnRetry.setOnClickListener(v -> {

            int hearts = heartsManager.getHearts();
            if (hearts <= 0) {
                Toast.makeText(this,
                        "No hearts left! Watch an ad or wait.",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            heartsManager.setHearts(hearts - 1);

            dialog.dismiss();
            recreate();
        });

        // ---------------- WATCH AD CONTINUE ----------------
        btnWatchAd.setOnClickListener(v -> {

            if (rewardedAd == null) {
                Toast.makeText(this,
                        "Ad not ready yet",
                        Toast.LENGTH_SHORT).show();
                loadRewardedAd();
                return;
            }

            rewardedAd.show(this, rewardItem -> {

                // ➕ GIVE BONUS
                MOVES += 5;
                TIME_LEFT+=10;
                txtTime.setText("" + TIME_LEFT); // or millis
                txtMoves.setText("" + MOVES);
                resumeGameAfterAd();
                isPaused = false;
                resumeTimer(); // or startTimer()

                dialog.dismiss();

                loadRewardedAd(); // preload next ad
            });
        });

        // ---------------- HOME ----------------
        btnHome.setOnClickListener(v -> {
            int hearts = heartsManager.getHearts();
            if (hearts >= 0) {
                heartsManager.setHearts(hearts - 1);
            }
            dialog.dismiss();
            startActivity(new Intent(this, Meanu.class));
            finish();
            MusicManager.resume();
        });
        dialog.show();
    }

    int calculateStars() {
        if (score >= star3) return 3;
        if (score >= star2) return 2;
        return 1;
    }

    void explodeCandy(ImageView img) {
        img.animate().alpha(0f).setDuration(100).start();

        ViewGroup root = (ViewGroup) getWindow().getDecorView();
        Rect rect = new Rect();
        img.getGlobalVisibleRect(rect);

        for (int i = 0; i < 15; i++) {
            ImageView p = new ImageView(this);
            p.setImageDrawable(img.getDrawable());

            p.setX(rect.centerX());
            p.setY(rect.centerY());
            p.setScaleX(0.3f);
            p.setScaleY(0.3f);

            root.addView(p);

            float dx = random.nextInt(300) - 150;
            float dy = random.nextInt(300) - 150;

            p.animate()
                    .translationXBy(dx)
                    .translationYBy(dy)
                    .rotationBy(random.nextInt(360))
                    .alpha(0f)
                    .setDuration(400)
                    .withEndAction(() -> root.removeView(p))
                    .start();
        }

    }

    void screenShake() {
        View root = findViewById(R.id.rootLayout);

        root.animate()
                .translationXBy(15)
                .setDuration(40)
                .withEndAction(() ->
                        root.animate()
                                .translationXBy(-30)
                                .setDuration(40)
                                .withEndAction(() ->
                                        root.animate()
                                                .translationXBy(15)
                                                .setDuration(40)
                                )
                );
    }

    void startTimer() {
        if (timerRunning) return;

        timerRunning = true;

        timerRunnable = new Runnable() {
            @Override
            public void run() {

                // ⏸️ If paused, just wait
                if (isPaused || levelFinished || gameOver) {
                    timerHandler.postDelayed(this, 1000);
                    return;
                }

                // ⛔ Stop permanently
                if (gameOver || levelFinished) {
                    stopTimer();
                    return;
                }

                TIME_LEFT--;
                txtTime.setText("" + TIME_LEFT);

                // 🔥 Last 10 sec warning
                if (TIME_LEFT <= 10) {
                    txtTime.animate()
                            .scaleX(1.2f)
                            .scaleY(1.2f)
                            .setDuration(200)
                            .withEndAction(() ->
                                    txtTime.animate()
                                            .scaleX(1f)
                                            .scaleY(1f)
                                            .setDuration(200)
                                            .start()
                            ).start();
                }

                // 🔴 Time up
                if (TIME_LEFT <= 0) {
                    gameOver = true;
                    showGameOverDialog();
                    stopTimer();
                    return;
                }

                timerHandler.postDelayed(this, 1000);
            }
        };

        timerHandler.postDelayed(timerRunnable, 1000);
    }

    void stopTimer() {
        timerHandler.removeCallbacks(timerRunnable);
        timerRunning = false;
    }

    void pauseTimer() {
        isPaused = true;
    }

    void resumeTimer() {
        isPaused = false;
    }


    void startTimeWarning() {
        // Blink animation
        AlphaAnimation blink = new AlphaAnimation(1f, 0.2f);
        blink.setDuration(300);
        blink.setRepeatMode(AlphaAnimation.REVERSE);
        blink.setRepeatCount(10);

        txtTime.startAnimation(blink);

        // Screen shake
        shakeScreen();
    }

    void shakeScreen() {
        View root = findViewById(android.R.id.content);

        root.animate()
                .translationXBy(15)
                .setDuration(50)
                .withEndAction(() ->
                        root.animate()
                                .translationXBy(-30)
                                .setDuration(50)
                                .withEndAction(() ->
                                        root.animate()
                                                .translationXBy(15)
                                                .setDuration(50)
                                )
                );
    }

    void togglePause() {
        if (levelFinished || gameOver) return;

        isPaused = !isPaused;
        if (isPaused) {
            btnPause.setText("▶ Resume");
            showPauseDialog();
        } else {
            btnPause.setText("⏸ Pause");
        }
    }

    void showPauseDialog() {
        Dialog d = new Dialog(this);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(R.layout.dialog_pause);
        d.setCancelable(false);

        d.findViewById(R.id.btnResume).setOnClickListener(v -> {
            isPaused = false;
            btnPause.setText("⏸ Pause");
            d.dismiss();
        });
        d.findViewById(R.id.btnRetry).setOnClickListener(view -> {
            HeartsManager heartsManager = new HeartsManager(this);
            int hearts_ = heartsManager.getHearts();

            if (hearts_ <= 0) {
                Toast.makeText(this, "No hearts left! Watch an ad or wait for daily hearts.", Toast.LENGTH_SHORT).show();
                return; // Prevent retry
            }
            heartsManager.setHearts(hearts_ - 1);
            d.dismiss();
            recreate(); // Restart same level

        });
        d.findViewById(R.id.btnHome).setOnClickListener(v -> {
            startActivity(new Intent(this, Meanu.class));
            finish();
        });

        d.show();
    }

    void spawnExplosion(int row, int col, boolean big) {
        int particleCount = big ? 20 : 12;
        int size = views[row][col].getWidth();

        int[] loc = new int[2];
        views[row][col].getLocationOnScreen(loc);

        for (int i = 0; i < particleCount; i++) {
            ImageView p = new ImageView(this);
            p.setImageResource(R.drawable.particle_dot);

            GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
            lp.width = 12;
            lp.height = 12;
            p.setLayoutParams(lp);

            grid.addView(p);

            p.setX(loc[0] + size / 2);
            p.setY(loc[1] + size / 2);

            float dx = (random.nextFloat() - 0.5f) * (big ? 300 : 180);
            float dy = (random.nextFloat() - 0.5f) * (big ? 300 : 180);

            p.animate()
                    .translationXBy(dx)
                    .translationYBy(dy)
                    .alpha(0f)
                    .setDuration(big ? 600 : 400)
                    .withEndAction(() -> grid.removeView(p))
                    .start();
        }
    }

    void spawnCandyExplosion(int row, int col, int candyType, boolean big) {

        int particleCount = big ? 40 : 20;
        float maxDistance = big ? 280f : 220f;

        int[] candyLoc = new int[2];
        views[row][col].getLocationOnScreen(candyLoc);

        int[] rootLoc = new int[2];
        effectsLayer.getLocationOnScreen(rootLoc);

        float centerX = candyLoc[0] - rootLoc[0] + views[row][col].getWidth() / 2f;
        float centerY = candyLoc[1] - rootLoc[1] + views[row][col].getHeight() / 2f;

        for (int i = 0; i < particleCount; i++) {

            View particle = new View(this);
            particle.setBackgroundResource(R.drawable.particle_circle);
            particle.getBackground().setTint(getCandyColor(candyType));

            int size = big ? 100 : 50;
            FrameLayout.LayoutParams lp =
                    new FrameLayout.LayoutParams(size, size);
            particle.setLayoutParams(lp);

            particle.setX(centerX);
            particle.setY(centerY);

            effectsLayer.addView(particle);

            double angle = Math.random() * Math.PI * 2;
            float distance = (float) (Math.random() * maxDistance);

            float dx = (float) Math.cos(angle) * distance;
            float dy = (float) Math.sin(angle) * distance;

            particle.animate()
                    .translationXBy(dx)
                    .translationYBy(dy)
                    .alpha(0f)
                    .scaleX(0.2f)
                    .scaleY(0.2f)
                    .setDuration(700)
                    .withEndAction(() ->
                            effectsLayer.removeView(particle))
                    .start();
        }
    }


    void showComboText(String title, String comboText) {
        txtCombo.setText(title + "\n" + comboText);
        txtCombo.setVisibility(View.VISIBLE);

        txtCombo.setScaleX(0.5f);
        txtCombo.setScaleY(0.5f);
        txtCombo.setAlpha(0f);

        txtCombo.animate()
                .alpha(1f)
                .scaleX(1.3f)
                .scaleY(1.3f)
                .setDuration(200)
                .withEndAction(() ->
                        txtCombo.animate()
                                .alpha(0f)
                                .scaleX(1f)
                                .scaleY(1f)
                                .setDuration(900)
                                .withEndAction(() ->
                                        txtCombo.setVisibility(View.GONE)
                                )
                )
                .start();
    }

    void updateTargetProgress() {
        int remaining = 0;
        for (int t : TARGETS) remaining += t;

        int completed = TOTAL_TARGET_COUNT - remaining;
        int targetProgress = (completed * 100) / TOTAL_TARGET_COUNT;

        animateProgress(lastProgress, targetProgress);
        lastProgress = targetProgress;
    }

    void updateProgressColor(int percent) {
        Drawable drawable = progressTargets.getProgressDrawable();

        if (percent < 50) {
            drawable.setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
        } else if (percent < 90) {
            drawable.setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_IN);
        } else {
            drawable.setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
        }
    }

    void animateProgress(int from, int to) {
        ObjectAnimator animator = ObjectAnimator.ofInt(
                progressTargets,
                "progress",
                from,
                to
        );
        animator.setDuration(1000); // smooth speed
        animator.setInterpolator(new DecelerateInterpolator());
        animator.start();

        updateProgressColor(to);
    }

    void startHintTimer() {
        hintHandler.removeCallbacksAndMessages(null);

        hintRunnable = new Runnable() {
            @Override
            public void run() {
                if (!hintActive && !isPaused && !gameOver && !levelFinished) {
                    showHint();
                }
                // 🔁 repeat every 5 sec
                hintHandler.postDelayed(this, 3000);
            }
        };

        hintHandler.postDelayed(hintRunnable, 8000);
    }

    void showHint() {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {

                if (c < COLS - 1 && canSwapMatch(r, c, r, c + 1)) {
                    glow(r, c, r, c + 1);
                    return;
                }

                if (r < ROWS - 1 && canSwapMatch(r, c, r + 1, c)) {
                    glow(r, c, r + 1, c);
                    return;
                }
            }
        }
    }

    boolean canSwapMatch(int r1, int c1, int r2, int c2) {
        swap(r1, c1, r2, c2);

        boolean match =
                checkLocalMatch(r1, c1) ||
                        checkLocalMatch(r2, c2);

        swap(r1, c1, r2, c2); // revert
        return match;
    }

    boolean checkLocalMatch(int r, int c) {
        int color = board[r][c];
        int count = 1;

        // Horizontal
        for (int i = c - 1; i >= 0 && board[r][i] == color; i--) count++;
        for (int i = c + 1; i < COLS && board[r][i] == color; i++) count++;

        if (count >= 3) return true;

        // Vertical
        count = 1;
        for (int i = r - 1; i >= 0 && board[i][c] == color; i--) count++;
        for (int i = r + 1; i < ROWS && board[i][c] == color; i++) count++;

        return count >= 3;
    }

    protected void glow(int r1, int c1, int r2, int c2) {
        animateGlow(views[r1][c1]);
        animateGlow(views[r2][c2]);

    }

    void animateGlow(View v) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(v, "scaleX", 1f, 1.2f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(v, "scaleY", 1f, 1.2f, 1f);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(v, "alpha", 1f, 0.6f, 1f);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(scaleX, scaleY, alpha);
        set.setDuration(800);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.start();
    }

    void shuffleBoardAnimated() {
        disableInput = true;

        // Fade & scale out
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                ImageView v = views[r][c];
                v.animate()
                        .alpha(0f)
                        .scaleX(0.8f)
                        .scaleY(0.8f)
                        .setDuration(200)
                        .start();
            }
        }

        // Shuffle after fade
        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            do {
                for (int r = 0; r < ROWS; r++) {
                    for (int c = 0; c < COLS; c++) {
                        board[r][c] = randomCandy();
                    }
                }
            } while (hasMatch() || !hasPossibleMoves());

            refresh();

            // Fade & bounce in
            for (int r = 0; r < ROWS; r++) {
                for (int c = 0; c < COLS; c++) {
                    ImageView v = views[r][c];
                    v.setAlpha(0f);
                    v.setScaleX(0.8f);
                    v.setScaleY(0.8f);

                    v.animate()
                            .alpha(1f)
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(300)
                            .setInterpolator(new OvershootInterpolator())
                            .start();
                }
            }

            disableInput = false;

        }, 220);
    }

    boolean hasPossibleMoves() {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {

                // check right swap
                if (c < COLS - 1) {
                    if (canSwapMatchSimple(r, c, r, c + 1)) {
                        return true;
                    }
                }

                // check down swap
                if (r < ROWS - 1) {
                    if (canSwapMatchSimple(r, c, r + 1, c)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    boolean canSwapMatchSimple(int r1, int c1, int r2, int c2) {
        int t = board[r1][c1];
        board[r1][c1] = board[r2][c2];
        board[r2][c2] = t;

        boolean match =
                checkLocalMatch(r1, c1) ||
                        checkLocalMatch(r2, c2);

        // revert swap
        t = board[r1][c1];
        board[r1][c1] = board[r2][c2];
        board[r2][c2] = t;

        return match;
    }

    void applyBonusScore(Runnable onBonusFinished) {
        if (bonusApplied) {
            onBonusFinished.run();
            return;
        }

        bonusApplied = true;

        int bonus = MOVES * BONUS_PER_MOVE;
        if (bonus <= 0) {
            onBonusFinished.run();
            return;
        }

        Handler handler = new Handler();
        int[] tempMoves = {MOVES};

        Runnable bonusRunnable = new Runnable() {
            @Override
            public void run() {
                if (tempMoves[0] > 0) {
                    tempMoves[0]--;
                    score += BONUS_PER_MOVE;

                    txtScore.setText("" + score);
                    txtMoves.setText("" + tempMoves[0]);

                    handler.postDelayed(this, 80);
                } else {
                    MOVES = 0;
                    onBonusFinished.run(); // ✅ bonus finished
                }
            }
        };

        handler.post(bonusRunnable);
    }

    protected abstract void showStep0();

    protected abstract void showStep1();

    protected abstract void showStep2();

    protected boolean engineFrozen = false;

    protected void freezeEngine() {
        engineFrozen = true;

        disableInput = true;

        handler.removeCallbacksAndMessages(null);
        hintHandler.removeCallbacksAndMessages(null);
    }

    protected void addScore(int amount) {
        if (engineFrozen) return;

        score += amount;
        txtScore.setText("" + score);

        onScoreUpdated();
    }

    protected void onScoreUpdated() {
        // overridden by subclasses
    }

    protected boolean isLevelComplete() {
        return false;
    }

    protected boolean hasTargetsRemaining() {
        return true;
    }

    protected boolean levelEnded = false;

    protected void onLevelWin() {
        if (levelEnded) return;
        levelEnded = true;

        freezeEngine();

        showLevelCompleteDialog();
    }

    protected void onLevelLose() {
        if (levelEnded) return;
        levelEnded = true;

        freezeEngine();

        showGameOverDialog();
    }

    protected void initBlockers() {


    }

    void damageNearbyBlockers(int r, int c) {

        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {

                int nr = r + dr;
                int nc = c + dc;

                if (nr < 0 || nc < 0 || nr >= ROWS || nc >= COLS) continue;

                if (!HAS_BLOCKERS) continue;

                if (blockers[nr][nc] > 0 && !blockerDamagedThisMove[nr][nc]) {

                    blockerDamagedThisMove[nr][nc] = true; // 🔒 lock for this move
                    blockers[nr][nc]--;

                    if (blockers[nr][nc] == 2) {
                        views[nr][nc].setImageResource(R.drawable.ice_2);
                    } else if (blockers[nr][nc] == 1) {
                        views[nr][nc].setImageResource(R.drawable.ice_1);
                    }
                    if (blockers[nr][nc] == 0) {
                        TOTAL_BLOCKERS--;
                        board[nr][nc] = -1;
                        views[nr][nc].setImageDrawable(null);
                        fillEmptySpaces();
                        checkGameOver();
                    }
                }
            }
        }
    }


    protected void updateBlockerView(int r, int c) {
        ImageView img = views[r][c];

        switch (blockers[r][c]) {
            case 3:
                img.setImageResource(R.drawable.ice_3);
                break;

            case 2:
                img.setImageResource(R.drawable.ice_2);
                break;

            case 1:
                img.setImageResource(R.drawable.ice_1);
                break;

            case 0:
                img.setImageDrawable(null); // blocker destroyed
                break;
        }
    }

    void fillEmptySpaces() {

        for (int c = 0; c < COLS; c++) {

            int writeRow = ROWS - 1;

            for (int r = ROWS - 1; r >= 0; r--) {

                // Skip active blockers (they block gravity)
                if (HAS_BLOCKERS && blockers[r][c] > 0) {
                    writeRow = r - 1;
                    continue;
                }

                if (board[r][c] != -1) {

                    if (writeRow != r) {
                        board[writeRow][c] = board[r][c];
                        board[r][c] = -1;
                        animateFall(r, writeRow, c);
                    }

                    writeRow--;
                }
            }

            // Spawn new candies above
            for (int r = writeRow; r >= 0; r--) {

                // Do not spawn inside blocker
                if (HAS_BLOCKERS && blockers[r][c] > 0) break;

                board[r][c] = randomCandy();
                animateSpawn(r, c);
            }
        }

        handler.postDelayed(() -> {
            refresh();
            startHintTimer();
        }, 220);
    }

    void resetBlockerDamageFlags() {
        if (blockerDamagedThisMove == null) return;

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                blockerDamagedThisMove[r][c] = false;
            }
        }
    }

    protected void toggleBooster(BoosterType type) {
        if (activeBooster == type) {
            activeBooster = BoosterType.NONE;
        } else {
            activeBooster = type;
        }
    }

    protected void onCellTapped(int r, int c) {

        if (disableInput || isPaused || gameOver || levelFinished) return;

        if (activeBooster != BoosterType.NONE) {

            if (!BoosterManager.use(this, activeBooster)) {
                activeBooster = BoosterType.NONE;
                return;
            }

            disableInput = true;

            switch (activeBooster) {
                case HAMMER:
                    destroySingle(r, c);
                    updatebossterui();
                    break;

                case BOMB:
                    destroy3x3(r, c);
                    updatebossterui();
                    break;

                case COLOR_BOMB:
                    destroyAllSame(board[r][c]);
                    updatebossterui();
                    break;

                case SHUFFLE:
                    shuffleBoardAnimated();
                    updatebossterui();
                    break;
            }

            activeBooster = BoosterType.NONE;

            handler.postDelayed(this::resolveBoard, 300);
        }
    }

    protected void destroySingle(int r, int c) {
        if (board[r][c] >= 0) {
            board[r][c] = CELL_EMPTY;
            views[r][c].setImageDrawable(null);
            score++;
            txtScore.setText("" + score);
        }
    }

    protected void destroy3x3(int r, int c) {
        for (int i = r - 1; i <= r + 1; i++) {
            for (int j = c - 1; j <= c + 1; j++) {
                if (isValid(i, j) && board[i][j] >= 0) {
                    board[i][j] = CELL_EMPTY;
                    views[i][j].setImageDrawable(null);
                    score++;
                    txtScore.setText("" + score);
                }
            }
        }
    }

    protected void destroyAllSame(int candyType) {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (board[i][j] == candyType) {
                    board[i][j] = CELL_EMPTY;
                    views[i][j].setImageDrawable(null);
                    score++;
                    txtScore.setText("" + score);
                }
            }
        }
    }

    protected void playDestroyAnimation(ImageView view) {
        if (view == null) return;

        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0f);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
        scaleX.setDuration(200);
        scaleY.setDuration(200);
        alpha.setDuration(200);
        scaleX.start();
        scaleY.start();
        alpha.start();
    }

    protected void resolveBoard() {
        gravity();
        checkMatches();
    }

    protected boolean isValid(int r, int c) {
        return r >= 0 && r < ROWS && c >= 0 && c < COLS;
    }

    protected void checkMatches() {

        boolean[][] matched = new boolean[ROWS][COLS];
        boolean foundMatch = false;

        // ----------------------------
        // HORIZONTAL CHECK
        // ----------------------------
        for (int r = 0; r < ROWS; r++) {

            int count = 1;

            for (int c = 1; c <= COLS; c++) {

                if (c < COLS
                        && board[r][c] >= 0
                        && board[r][c] == board[r][c - 1]) {
                    count++;
                } else {

                    if (count >= 3) {
                        foundMatch = true;
                        for (int k = 0; k < count; k++) {
                            matched[r][c - 1 - k] = true;
                        }
                    }
                    count = 1;
                }
            }
        }

        // ----------------------------
        // VERTICAL CHECK
        // ----------------------------
        for (int c = 0; c < COLS; c++) {

            int count = 1;

            for (int r = 1; r <= ROWS; r++) {

                if (r < ROWS
                        && board[r][c] >= 0
                        && board[r][c] == board[r - 1][c]) {
                    count++;
                } else {

                    if (count >= 3) {
                        foundMatch = true;
                        for (int k = 0; k < count; k++) {
                            matched[r - 1 - k][c] = true;
                        }
                    }
                    count = 1;
                }
            }
        }

        // ----------------------------
        // NO MATCH FOUND → END CASCADE
        // ----------------------------
        if (!foundMatch) {
            disableInput = false;
            comboCount = 0;
            return;
        }

        // ----------------------------
        // DESTROY MATCHED CELLS
        // ----------------------------
        disableInput = true;
        comboCount++;

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {

                if (matched[r][c]) {

                    score += 10 * comboCount;
                    txtScore.setText("" + score);
                    board[r][c] = CELL_EMPTY;
                    views[r][c].setImageDrawable(null);

                    damageNearbyBlockers(r, c);
                }
            }
        }

        // ----------------------------
        // GRAVITY → CASCADE
        // ----------------------------
        handler.postDelayed(() -> {
            gravity();
            checkMatches();
        }, 250);
    }

    void updatebossterui() {
        txtHammerCount.setText(String.valueOf(
                BoosterManager.get(this, BoosterType.HAMMER)));

        txtBombCount.setText(String.valueOf(
                BoosterManager.get(this, BoosterType.BOMB)));

        txtShuffleCount.setText(String.valueOf(
                BoosterManager.get(this, BoosterType.SHUFFLE)));

        txtColorCount.setText(String.valueOf(
                BoosterManager.get(this, BoosterType.COLOR_BOMB)));

        btnHammer.setAlpha(activeBooster == BoosterType.HAMMER ? 0.6f : 1f);
        btnBomb.setAlpha(activeBooster == BoosterType.BOMB ? 0.6f : 1f);
        btnShuffle.setAlpha(activeBooster == BoosterType.SHUFFLE ? 0.6f : 1f);
        btnColor.setAlpha(activeBooster == BoosterType.COLOR_BOMB ? 0.6f : 1f);
    }

    private int getCandyColor(int type) {
        switch (type) {
            case 0:
                return Color.RED;
            case 1:
                return Color.BLUE;
            case 2:
                return Color.GREEN;
            case 3:
                return Color.YELLOW;
            case 4:
                return Color.MAGENTA;
            default:
                return Color.WHITE;
        }

    }
    private void loadRewardedAd() {

        AdRequest adRequest = new AdRequest.Builder().build();

        RewardedAd.load(
                this,
                "ca-app-pub-3940256099942544/5224354917", // TEST ID
                adRequest,
                new RewardedAdLoadCallback() {

                    @Override
                    public void onAdLoaded(RewardedAd ad) {
                        rewardedAd = ad;
                    }

                    @Override
                    public void onAdFailedToLoad(LoadAdError error) {
                        rewardedAd = null;
                    }
                }
        );
    }
    private void resumeGameAfterAd() {

        // ✅ Resume core flags
        isPaused = false;
        gameOver = false;
        disableInput = false;

        // ✅ Resume timer
        startTimer();   // or resumeTimer() if you use that

        // ✅ Enable grid touches again
        enableBoardInput(true);

        // ✅ Refresh UI
        txtTime.setText("" + TIME_LEFT); // or millis
        txtMoves.setText("" + MOVES);
    }
    private void enableBoardInput(boolean enable) {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                views[r][c].setEnabled(enable);
            }
        }
    }
}