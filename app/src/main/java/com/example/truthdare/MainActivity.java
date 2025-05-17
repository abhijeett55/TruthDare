package com.example.truthdare;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Button btn, truthBtn, dareBtn;
    private ImageView imgView;
    private Random random = new Random();
    private int lastDirection;
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        btn = findViewById(R.id.button);
        imgView = findViewById(R.id.imageView);
        truthBtn = findViewById(R.id.btn1);
        dareBtn = findViewById(R.id.btn2);


        truthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), TruthActivity.class));
            }
        });

        dareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), DareActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        truthBtn.setEnabled(false);
        dareBtn.setEnabled(false);
        btn.setEnabled(true);
    }

    public void Spin(View view) {
        int newDirection = random.nextInt(5400);
        float pivotX = imgView.getWidth()/2;
        float pivotY = imgView.getHeight()/2;

        Animation rotate = new RotateAnimation(lastDirection, newDirection, pivotX, pivotY);
        rotate.setDuration(2000);
        rotate.setFillAfter(true);
        rotate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mp = MediaPlayer.create(MainActivity.this, R.raw.audio);
                mp.start();
                btn.setEnabled(false);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mp.stop();
                mp.release();
                mp = null;

                int pick = random.nextInt(2);

                // Reset both buttons first
                truthBtn.setEnabled(false);
                dareBtn.setEnabled(false);
                truthBtn.setScaleX(1f);
                truthBtn.setScaleY(1f);
                dareBtn.setScaleX(1f);
                dareBtn.setScaleY(1f);
                truthBtn.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimaryDark));
                dareBtn.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimaryDark));

                // Animate and enable the chosen one
                Button selectedBtn = (pick == 0) ? truthBtn : dareBtn;
                selectedBtn.setEnabled(true);
                selectedBtn.setBackgroundTintList(getResources().getColorStateList(R.color.colorAccent));

                // Pulse animation
                selectedBtn.animate()
                        .scaleX(1.1f)
                        .scaleY(1.1f)
                        .setDuration(300)
                        .withEndAction(() -> selectedBtn.animate().scaleX(1f).scaleY(1f).setDuration(300))
                        .start();
            }


            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        lastDirection = newDirection;
        imgView.startAnimation(rotate);
    }

}