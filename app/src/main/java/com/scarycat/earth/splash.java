package com.scarycat.earth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class splash extends AppCompatActivity {
    ImageView imgLogo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        );

        imgLogo = findViewById(R.id.imgLogo);

        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in_splash);

        // 👁️ Make visible once
        imgLogo.setVisibility(ImageView.VISIBLE);
        imgLogo.startAnimation(fadeIn);

        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationEnd(Animation animation) {
                // ✅ STAYS VISIBLE
                // Move to next screen after small delay if needed
                imgLogo.postDelayed(() -> {
                    startActivity(new Intent(splash.this, home.class));
                    finish();
                }, 500);
            }

            @Override
            public void onAnimationRepeat(Animation animation) { }
        });
    }
}