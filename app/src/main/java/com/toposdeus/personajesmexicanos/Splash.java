package com.toposdeus.personajesmexicanos;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;



public class Splash extends Activity {


    private static final long temporiser = 2500;
    ImageView splashing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashactivity);
        splashing = findViewById(R.id.imagensplash);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                // codigo a ejecutar
                Intent mainIntent = new Intent().setClass(
                        Splash.this, PantallaPrincipal.class);
                startActivity(mainIntent);
                finish();
            }
        };
        // Simulate a long loading process on application startup.
        Animation transparent = AnimationUtils.loadAnimation(this, R.anim.transparencia);
        transparent.reset();
        splashing.startAnimation(transparent);
        Timer timer = new Timer();
        timer.schedule(task, temporiser);
    }
}