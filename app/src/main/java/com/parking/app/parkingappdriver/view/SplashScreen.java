package com.parking.app.parkingappdriver.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.parking.app.parkingappdriver.R;
import com.parking.app.parkingappdriver.activity.BaseActivity;
import com.parking.app.parkingappdriver.appIntroduction.WelcomeScreen;
import com.parking.app.parkingappdriver.utils.AppConstants;


public class SplashScreen extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, WelcomeScreen.class);
                startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }
        }, AppConstants.ONE_SECOND * 3);
    }
}
