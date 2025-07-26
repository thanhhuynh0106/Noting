package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.example.app.remote.SharedPrefManager;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_TIME = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView logo = findViewById(R.id.logoSplash);

        RequestOptions requestOptions = new RequestOptions()
                .format(DecodeFormat.PREFER_RGB_565)
                .override(400, 700)
                .fitCenter();

        Glide.with(this)
                .load(R.drawable.hongconen)
                .apply(requestOptions)
                .into(logo);

        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        logo.startAnimation(fadeIn);

        new Handler().postDelayed(() -> {
            if (SharedPrefManager.getInstance(SplashActivity.this).isLoggedIn()) {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            } else {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }
//            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
//            finish();
        }, SPLASH_TIME);


    }
}
