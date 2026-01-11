package com.scarycat.earth.utils;



import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;


import com.scarycat.earth.R;

import java.util.Random;
public class MusicService extends Service {

    private static MusicService instance;
    private MediaPlayer mediaPlayer;

    private final int[] playlist = {
            R.raw.music1,
            R.raw.music2,
            R.raw.music3
    };

    public static MusicService getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        playRandomSong();
    }

    private void playRandomSong() {
        int song = playlist[new Random().nextInt(playlist.length)];

        mediaPlayer = MediaPlayer.create(this, song);
        mediaPlayer.setLooping(false);
        mediaPlayer.start();

        mediaPlayer.setOnCompletionListener(mp -> {
            mp.release();
            playRandomSong();
        });
    }

    // 🎚 VOLUME CONTROL
    public void setVolume(float volume) {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume, volume);
        }
    }

    // 🔇 PAUSE
    public void pauseMusic() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    // ▶ RESUME
    public void resumeMusic() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        instance = null;
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
