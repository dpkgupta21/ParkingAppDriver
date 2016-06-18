package com.parking.app.parkingappdriver.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.parking.app.parkingappdriver.R;
import com.parking.app.parkingappdriver.activity.BaseActivity;
import com.parking.app.parkingappdriver.appIntroduction.WelcomeScreen;
import com.parking.app.parkingappdriver.navigationDrawer.DriverNavigationDrawerActivity;
import com.parking.app.parkingappdriver.preferences.SessionManager;
import com.parking.app.parkingappdriver.utils.AppConstants;


public class SplashScreen extends BaseActivity {

    private Activity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        mActivity = SplashScreen.this;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String authToken = SessionManager.getInstance(mActivity).getAuthToken();
                String userId = SessionManager.getInstance(mActivity).getUserId();

                Intent i = null;
                if ((userId == null || userId.equalsIgnoreCase(""))
                        && (authToken == null || authToken.equalsIgnoreCase(""))) {
                    i = new Intent(mActivity, WelcomeScreen.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                } else {

                    //loginInBackground();
                    Intent intent = new Intent(SplashScreen.this,
                            DriverNavigationDrawerActivity.class);
                    startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                }
            }
        }, AppConstants.ONE_SECOND * 3);
    }
}
