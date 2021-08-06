package com.rumooursindoyo.moheeeetgupta;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    private  static  int SPLASH_TIME_OUT = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(MainActivity.check_login == 0){
                    Intent loginIntent = new Intent(SplashScreen.this, loginactivity.class);
                    startActivity(loginIntent);
                    finish();
                }else{
                    Intent mainActivityIntent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(mainActivityIntent);
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);
    }
}