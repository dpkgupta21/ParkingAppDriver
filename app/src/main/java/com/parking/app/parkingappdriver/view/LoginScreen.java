package com.parking.app.parkingappdriver.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.parking.app.parkingappdriver.R;
import com.parking.app.parkingappdriver.iClasses.GlobalKeys;
import com.parking.app.parkingappdriver.model.DriverConfigDTO;
import com.parking.app.parkingappdriver.navigationDrawer.DriverNavigationDrawerActivity;
import com.parking.app.parkingappdriver.preferences.SessionManager;
import com.parking.app.parkingappdriver.utils.AppUtils;
import com.parking.app.parkingappdriver.webservices.handler.DriverConfigAPIHandler;
import com.parking.app.parkingappdriver.webservices.handler.LoginAPIHandler;
import com.parking.app.parkingappdriver.webservices.ihelper.WebAPIResponseListener;

import org.json.JSONException;
import org.json.JSONObject;


public class LoginScreen extends AppCompatActivity implements View.OnClickListener {


    private RelativeLayout login_button, register;
    private EditText email_et, pwd_et;
    private LoginAPIHandler mLoginAPIHandler;
    private String TAG = LoginScreen.class.getSimpleName();
    private String email, pwd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        initViews();
        assignClick();
    }


    private void initViews() {

        login_button = (RelativeLayout) findViewById(R.id.login_button);
        register = (RelativeLayout) findViewById(R.id.register);
        email_et = (EditText) findViewById(R.id.email_et);
        pwd_et = (EditText) findViewById(R.id.pwd_et);
    }

    private void assignClick() {

        login_button.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(final View v) {

        switch (v.getId()) {

            case R.id.register:
//                startActivity(new Intent(LoginScreen.this, SignupScreen.class));
                break;
            case R.id.login_button:
                email = email_et.getText().toString().trim();
                pwd = pwd_et.getText().toString().trim();
                if (email.isEmpty()) {
                    Snackbar.make(v, getString(R.string.please_enter_email), Snackbar.LENGTH_LONG).show();
                } else if (pwd.isEmpty()) {
                    Snackbar.make(v, getString(R.string.please_enter_pwd), Snackbar.LENGTH_LONG).show();

                } else {
                    mLoginAPIHandler = new LoginAPIHandler(this, email, pwd, new WebAPIResponseListener() {
                        @Override
                        public void onSuccessOfResponse(Object... arguments) {
                            AppUtils.hideProgressDialog();
                            try {
                                JSONObject mJsonObject = (JSONObject) arguments[0];
                                if (mJsonObject != null) {
                                    if (mJsonObject.has(GlobalKeys.AUTHTOKEN) && mJsonObject.has(GlobalKeys.EMAIL)) {

                                        email = mJsonObject.getString(GlobalKeys.EMAIL);
                                        String auth = mJsonObject.getString(GlobalKeys.AUTHTOKEN);
                                        AppUtils.showLog(TAG, "email: " + email + " " + auth);
                                        SessionManager.getInstance(LoginScreen.this).createLoginSession(email, pwd, auth);

                                        callDriverConfigWS();

                                    } else {
                                        AppUtils.showToast(LoginScreen.this, "Login Failed");
                                    }
                                } else {
                                    AppUtils.showToast(LoginScreen.this, "Login Failed");

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                        @Override
                        public void onFailOfResponse(Object... arguments) {
                            AppUtils.showToast(LoginScreen.this, "Login Failed");

                        }
                    });


                }
                break;
        }

    }

    private void callDriverConfigWS() {
        new DriverConfigAPIHandler(LoginScreen.this, new WebAPIResponseListener() {
            @Override
            public void onSuccessOfResponse(Object... arguments) {
                try {
                    JSONObject mJsonObject = (JSONObject) arguments[0];
                    if (mJsonObject != null) {
                        DriverConfigDTO configDTO = new Gson().fromJson(mJsonObject.toString(),
                                DriverConfigDTO.class);

                        SessionManager.getInstance(LoginScreen.this)
                                .setVallet_Id(configDTO.get_id());

                        Intent intent = new Intent(LoginScreen.this, DriverNavigationDrawerActivity.class);
                        startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailOfResponse(Object... arguments) {
                AppUtils.showToast(LoginScreen.this, "Login Failed");
            }
        });
    }
}
