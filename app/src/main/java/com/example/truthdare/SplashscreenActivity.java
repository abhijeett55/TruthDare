package com.example.truthdare;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


@SuppressLint("CustomSplashScreen")

public class SplashscreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splashscreen);

        ImageView logo = findViewById(R.id.applogo);
        Animation fade_in = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        logo.startAnimation(fade_in);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(SplashscreenActivity.this, StartActivity.class);
            startActivity(intent);
            finish();
        },1300);
    }
}