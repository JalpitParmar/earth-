package com.scarycat.earth.utils;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.scarycat.earth.R;

import java.util.Random;

public class MusicManager {

    private static MediaPlayer player;
    private static Context appContext;

    private static float volume = 0.4f;
    private static float targetVolume = 0.4f;

    private static boolean initialized = false;
    private static boolean appInForeground = true;

    private static AudioManager audioManager;
    private static AudioFocusRequest focusRequest;
    private static boolean hasAudioFocus = false;

    private static final int FADE_DURATION = 700;
    private static final int FADE_STEP_TIME = 100;

    private static final Handler handler = new Handler(Looper.getMainLooper());
    private static Runnable fadeRunnable;

    private static final int[] PLAYLIST = {
            R.raw.music1,
            R.raw.music2,
            R.raw.music4,
            R.raw.music5,
            R.raw.music6,
            R.raw.music7,
            R.raw.music8,
            R.raw.music9,
            R.raw.music10
    };

    private static final Random random = new Random();

    // 🔹 INIT (call once from Home/Splash)
    public static void init(Context context) {
        if (initialized) return;

        initialized = true;
        appContext = context.getApplicationContext();
        audioManager = (AudioManager) appContext.getSystemService(Context.AUDIO_SERVICE);

        requestAudioFocus();
    }

    // 🎧 AUDIO FOCUS (API 24+ SAFE)
    private static void requestAudioFocus() {
        if (audioManager == null) return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            AudioAttributes attrs = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();

            focusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                    .setAudioAttributes(attrs)
                    .setOnAudioFocusChangeListener(focusChangeListener)
                    .build();

            hasAudioFocus =
                    audioManager.requestAudioFocus(focusRequest)
                            == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;

        } else {
            hasAudioFocus =
                    audioManager.requestAudioFocus(
                            focusChangeListener,
                            AudioManager.STREAM_MUSIC,
                            AudioManager.AUDIOFOCUS_GAIN
                    ) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
        }

        if (hasAudioFocus) playNextSafe();
    }

    private static final AudioManager.OnAudioFocusChangeListener focusChangeListener =
            focusChange -> {
                switch (focusChange) {
                    case AudioManager.AUDIOFOCUS_LOSS:
                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                        fadeOutAndPause();
                        break;

                    case AudioManager.AUDIOFOCUS_GAIN:
                        fadeIn();
                        break;

                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                        setVolume(targetVolume * 0.3f);
                        break;
                }
            };

    // 🔁 PLAY
    private static void playNextSafe() {
        if (!appInForeground || !hasAudioFocus) return;

        stop();

        handler.postDelayed(() -> {
            try {
                int song = PLAYLIST[random.nextInt(PLAYLIST.length)];
                player = MediaPlayer.create(appContext, song);

                if (player == null) return;

                player.setLooping(false);
                player.setVolume(0f, 0f);

                player.setOnCompletionListener(mp -> playNextSafe());
                player.setOnErrorListener((mp, w, e) -> {
                    playNextSafe();
                    return true;
                });

                player.start();
                fadeIn();

            } catch (Exception e) {
                Log.e("MusicManager", "Play error", e);
            }
        }, 200);
    }

    // 🔊 VOLUME
    public static void setVolume(float v) {
        targetVolume = v;
        volume = v;

        if (player != null) {
            player.setVolume(v, v);
        }
    }

    // 🔺 FOREGROUND
    public static void onAppForeground() {
        appInForeground = true;
        if (player == null) playNextSafe();
        else fadeIn();
    }

    // 🔻 BACKGROUND
    public static void onAppBackground() {
        appInForeground = false;
        fadeOutAndPause();
    }

    // 🌊 FADE IN
    private static void fadeIn() {
        if (player == null) return;

        cancelFade();

        final int steps = FADE_DURATION / FADE_STEP_TIME;
        final float step = targetVolume / steps;

        fadeRunnable = new Runnable() {
            float v = 0f;
            public void run() {
                if (player == null) return;
                v += step;
                if (v >= targetVolume) {
                    player.setVolume(targetVolume, targetVolume);
                    return;
                }
                player.setVolume(v, v);
                handler.postDelayed(this, FADE_STEP_TIME);
            }
        };
        handler.post(fadeRunnable);
    }
    // 🌑 FADE OUT
    private static void fadeOutAndPause() {
        if (player == null) return;

        cancelFade();

        final int steps = FADE_DURATION / FADE_STEP_TIME;
        final float step = targetVolume / steps;

        fadeRunnable = new Runnable() {
            float v = targetVolume;
            public void run() {
                if (player == null) return;
                v -= step;
                if (v <= 0f) {
                    player.pause();
                    return;
                }
                player.setVolume(v, v);
                handler.postDelayed(this, FADE_STEP_TIME);
            }
        };
        handler.post(fadeRunnable);
    }
    // 🛑 STOP
    private static void stop() {
        cancelFade();
        if (player != null) {
            player.release();
            player = null;
        }
    }
    private static void cancelFade() {
        if (fadeRunnable != null) {
            handler.removeCallbacks(fadeRunnable);
            fadeRunnable = null;
        }
    }
    // ▶ Resume music (used in Activity onResume)
    public static void resume() {
        appInForeground = true;

        if (!hasAudioFocus) {
            requestAudioFocus();
            return;
        }
        if (player != null) {
            if (!player.isPlaying()) {
                player.start();
            }
            fadeIn();
        } else {
            playNextSafe();
        }
    }
    // ⏸ Pause music (used in Activity onPause)
    public static void pause() {
        appInForeground = false;
        fadeOutAndPause();
    }
}
