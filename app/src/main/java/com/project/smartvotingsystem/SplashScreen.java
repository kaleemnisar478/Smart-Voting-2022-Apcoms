package com.project.smartvotingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_SCREEN=2500;
    Animation top,bottom;

    TextView text;
    ImageView image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        View decorView = getWindow().getDecorView();
// Hide both the navigation bar and the status bar.
// SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
// a general rule, you should design your app to hide the status bar whenever you
// hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        top= AnimationUtils.loadAnimation(this,R.anim.splash_top_animation);
        bottom= AnimationUtils.loadAnimation(this,R.anim.splash_bottom_animation);

        text=findViewById(R.id.smartHome);
        image=findViewById(R.id.splashLogo);

        text.setAnimation(top);
        image.setAnimation(bottom);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(SplashScreen.this,Login.class);

                Pair[] pair=new Pair[2];

                pair[0]=new Pair<View,String>(text,"s_text");
                pair[1]=new Pair<View,String>(image,"s_logo");

                ActivityOptions op=ActivityOptions.makeSceneTransitionAnimation(SplashScreen.this,pair);

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent,op.toBundle());

                //finish(); it will exit this activity no more calls
            }
        },SPLASH_SCREEN);

    }
}