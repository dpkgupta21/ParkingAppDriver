package com.parking.app.parkingappdriver.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.parking.app.parkingappdriver.R;
import com.parking.app.parkingappdriver.drivermodel.navigationDrawer.DriverNavigationDrawerActivity;


public class LoginScreen extends AppCompatActivity implements View.OnClickListener {


    private RelativeLayout login_button;
    private EditText email_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        initViews();
        assignClick();
    }


    private void initViews() {

        login_button = (RelativeLayout) findViewById(R.id.login_button);
        email_et = (EditText) findViewById(R.id.email_et);
    }

    private void assignClick() {

        login_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.login_button:
                Intent intent = new Intent(this, DriverNavigationDrawerActivity.class);
                startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
                break;
        }

    }
}
