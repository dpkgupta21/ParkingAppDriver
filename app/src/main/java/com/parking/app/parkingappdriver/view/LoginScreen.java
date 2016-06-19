package com.parking.app.parkingappdriver.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.google.gson.Gson;
import com.parking.app.parkingappdriver.R;
import com.parking.app.parkingappdriver.activity.BaseActivity;
import com.parking.app.parkingappdriver.customViews.CustomProgressDialog;
import com.parking.app.parkingappdriver.drivermodel.CustomerComments;
import com.parking.app.parkingappdriver.iClasses.GlobalKeys;
import com.parking.app.parkingappdriver.model.DriverConfigDTO;
import com.parking.app.parkingappdriver.navigationDrawer.DriverNavigationDrawerActivity;
import com.parking.app.parkingappdriver.preferences.SessionManager;
import com.parking.app.parkingappdriver.utils.AppUtils;
import com.parking.app.parkingappdriver.utils.WebserviceResponseConstants;
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
        setClick(R.id.txt_forget_pwd);
    }

    @Override
    public void onClick(final View v) {

        switch (v.getId()) {

            case R.id.txt_forget_pwd:
                startActivity(new Intent(LoginScreen.this, ForgetPasswordScreen.class));
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

                    CustomProgressDialog.showProgDialog(mActivity, "Signing...");
                    new LoginAPIHandler(mActivity, email, pwd, new WebAPIResponseListener() {
                        @Override
                        public void onSuccessOfResponse(Object... arguments) {

                            try {
                                JSONObject mJsonObject = (JSONObject) arguments[0];
                                if (mJsonObject != null) {
                                    if (mJsonObject.has(GlobalKeys.AUTHTOKEN)
                                            && mJsonObject.has(GlobalKeys.EMAIL)) {

                                        email = mJsonObject.getString(GlobalKeys.EMAIL);
                                        String name = mJsonObject.getString(GlobalKeys.NAME);
                                        String auth = mJsonObject.getString(GlobalKeys.AUTHTOKEN);
                                        String userId = mJsonObject.getString("userId");
                                        AppUtils.showLog(TAG, "email: " + email + " " + auth);
                                        SessionManager.getInstance(mActivity).
                                                createLoginSession(email, pwd, auth, name, userId);

                                        //callAddTokenWS();
                                        callDriverConfigWS();

//                                        Intent intent = new Intent(LoginScreen.this,
//                                                DriverNavigationDrawerActivity.class);
//                                        startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//                                        finish();

                                    } else {
                                        CustomProgressDialog.hideProgressDialog();
                                        AppUtils.showDialog(mActivity,
                                                getString(R.string.dialog_title_message),
                                                "Login Failed");
                                    }
                                } else {
                                    CustomProgressDialog.hideProgressDialog();
                                    AppUtils.showDialog(mActivity,
                                            getString(R.string.dialog_title_message),
                                            "Login Failed");

                                }

                            } catch (JSONException e) {
                                CustomProgressDialog.hideProgressDialog();
                                AppUtils.showDialog(mActivity,
                                        getString(R.string.dialog_title_message),
                                        "Login Failed");
                                e.printStackTrace();
                            }
                        }


                        @Override
                        public void onFailOfResponse(Object... arguments) {
                            CustomProgressDialog.hideProgressDialog();
                            try {

                                if (arguments != null) {
                                    JSONObject errorJsonObj = (JSONObject) arguments[0];

                                    if (AppUtils.getWebServiceErrorCode(errorJsonObj).
                                            equalsIgnoreCase
                                                    (WebserviceResponseConstants.LOGIN_ERROR)) {

                                        AppUtils.showDialog(mActivity,
                                                getString(R.string.dialog_title_message),
                                                AppUtils.getWebServiceErrorMsg(errorJsonObj));
                                    }
                                }
                            } catch (Exception e) {
                                CustomProgressDialog.hideProgressDialog();
//                    Snackbar.make(v, getString(R.string.network_error_please_try_again),
//                            Snackbar.LENGTH_LONG).show();
                                e.printStackTrace();
                            }

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
                    CustomProgressDialog.hideProgressDialog();
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
                try {
                    CustomProgressDialog.hideProgressDialog();
                    if (arguments != null) {
                        JSONObject errorJsonObj = (JSONObject) arguments[0];

                        if (AppUtils.getWebServiceErrorCode(errorJsonObj).
                                equalsIgnoreCase
                                        (WebserviceResponseConstants.LOGIN_ERROR)) {

                            AppUtils.showDialog(mActivity,
                                    getString(R.string.dialog_title_message),
                                    AppUtils.getWebServiceErrorMsg(errorJsonObj));
                        }
                    }
                } catch (Exception e) {
                    CustomProgressDialog.hideProgressDialog();
//                    Snackbar.make(v, getString(R.string.network_error_please_try_again),
//                            Snackbar.LENGTH_LONG).show();
                    e.printStackTrace();
                }
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
