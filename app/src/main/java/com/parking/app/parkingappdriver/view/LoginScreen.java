package com.parking.app.parkingappdriver.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.google.gson.Gson;
import com.parking.app.parkingappdriver.R;
import com.parking.app.parkingappdriver.activity.BaseActivity;
import com.parking.app.parkingappdriver.drivermodel.CustomerComments;
import com.parking.app.parkingappdriver.iClasses.GlobalKeys;
import com.parking.app.parkingappdriver.model.DriverConfigDTO;
import com.parking.app.parkingappdriver.navigationDrawer.DriverNavigationDrawerActivity;
import com.parking.app.parkingappdriver.preferences.SessionManager;
import com.parking.app.parkingappdriver.utils.AppUtils;
import com.parking.app.parkingappdriver.webservices.handler.AddTokenPushAPIHandler;
import com.parking.app.parkingappdriver.webservices.handler.DriverDetailsAPIHandler;
import com.parking.app.parkingappdriver.webservices.handler.LoginAPIHandler;
import com.parking.app.parkingappdriver.webservices.ihelper.WebAPIResponseListener;

import org.json.JSONException;
import org.json.JSONObject;


public class LoginScreen extends BaseActivity {

    private String TAG = LoginScreen.class.getSimpleName();
    private String email, pwd;
    private Activity mActivity;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        mActivity = LoginScreen.this;
        assignClick();
    }

    private void assignClick() {
        setClick(R.id.login_button);
        setClick(R.id.register);
    }

    @Override
    public void onClick(final View v) {

        switch (v.getId()) {

            case R.id.register:
//                startActivity(new Intent(LoginScreen.this, SignupScreen.class));

                //startActivityForResult(new Intent(mActivity, CapturePicture.class), 1000);
                break;
            case R.id.login_button:
                email = getEditTextText(R.id.email_et).trim();
                pwd = getEditTextText(R.id.pwd_et).trim();
                if (email.isEmpty()) {
                    Snackbar.make(v, getString(R.string.please_enter_email),
                            Snackbar.LENGTH_LONG).show();
                } else if (pwd.isEmpty()) {
                    Snackbar.make(v, getString(R.string.please_enter_pwd), Snackbar.LENGTH_LONG).show();

                } else {
                    new LoginAPIHandler(mActivity, email, pwd, new WebAPIResponseListener() {
                        @Override
                        public void onSuccessOfResponse(Object... arguments) {
                            AppUtils.hideProgressDialog();
                            try {
                                JSONObject mJsonObject = (JSONObject) arguments[0];
                                if (mJsonObject != null) {
                                    if (mJsonObject.has(GlobalKeys.AUTHTOKEN)
                                            && mJsonObject.has(GlobalKeys.EMAIL)) {

                                        email = mJsonObject.getString(GlobalKeys.EMAIL);
                                        String auth = mJsonObject.getString(GlobalKeys.AUTHTOKEN);
                                        String userId = mJsonObject.getString("userId");
                                        AppUtils.showLog(TAG, "email: " + email + " " + auth);
                                        SessionManager.getInstance(mActivity).
                                                createLoginSession(email, pwd, auth, userId);

                                        //callAddTokenWS();
                                        callDriverConfigWS();

//                                        Intent intent = new Intent(LoginScreen.this,
//                                                DriverNavigationDrawerActivity.class);
//                                        startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//                                        finish();

                                    } else {
                                        AppUtils.showToast(mActivity, "Login Failed");
                                    }
                                } else {
                                    AppUtils.showToast(mActivity, "Login Failed");

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                        @Override
                        public void onFailOfResponse(Object... arguments) {
                            AppUtils.showToast(mActivity, "Login Failed");

                        }
                    });


                }
                break;
        }

    }



    private void callDriverConfigWS() {
        new DriverDetailsAPIHandler(mActivity, new WebAPIResponseListener() {
            @Override
            public void onSuccessOfResponse(Object... arguments) {
                try {
                    JSONObject mJsonObject = (JSONObject) arguments[0];
                    if (mJsonObject != null) {
                        DriverConfigDTO configDTO = new Gson().fromJson(mJsonObject.toString(),
                                DriverConfigDTO.class);

                        SessionManager.getInstance(mActivity)
                                .setVallet_Id(configDTO.get_id());
                        SessionManager.getInstance(mActivity)
                                .setValletNumber(configDTO.getEmployeeNumber());

                        Intent intent = new Intent(mActivity, DriverNavigationDrawerActivity.class);
                        startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailOfResponse(Object... arguments) {
                AppUtils.showToast(mActivity, "Login Failed");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == RESULT_OK) {
                startActivity(new Intent(mActivity, CustomerComments.class));
            }
        }
    }
}
