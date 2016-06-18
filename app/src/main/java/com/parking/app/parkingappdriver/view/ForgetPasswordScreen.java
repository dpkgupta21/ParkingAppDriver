package com.parking.app.parkingappdriver.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.parking.app.parkingappdriver.R;
import com.parking.app.parkingappdriver.activity.BaseActivity;
import com.parking.app.parkingappdriver.customViews.CustomProgressDialog;
import com.parking.app.parkingappdriver.iClasses.GlobalKeys;
import com.parking.app.parkingappdriver.utils.AppUtils;
import com.parking.app.parkingappdriver.webservices.handler.ForgetPasswordAPIHandler;
import com.parking.app.parkingappdriver.webservices.ihelper.WebAPIResponseListener;

import org.json.JSONException;
import org.json.JSONObject;


public class ForgetPasswordScreen extends BaseActivity implements View.OnClickListener {


    private String TAG = ForgetPasswordScreen.class.getSimpleName();
    private Activity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password_screen);

        mActivity = ForgetPasswordScreen.this;
        initViews();
    }


    private void initViews() {
        setClick(R.id.forget_password_button);

    }


    @Override
    public void onClick(final View v) {

        switch (v.getId()) {

            case R.id.forget_password_button:
                CustomProgressDialog.showProgDialog(mActivity, null);

                final String email = getViewText(R.id.edt_email);
                if (validateForm(v)) {

                    ForgetPasswordAPIHandler mLoginAPIHandler = new ForgetPasswordAPIHandler(
                            mActivity,
                            email,
                            new WebAPIResponseListener() {
                                @Override
                                public void onSuccessOfResponse(Object... arguments) {

                                    try {
                                        CustomProgressDialog.hideProgressDialog();
                                        JSONObject mJsonObject = (JSONObject) arguments[0];
                                        if (mJsonObject != null) {
                                            if (mJsonObject.has(GlobalKeys.MESSAGE)) {

                                                AppUtils.showDialog(mActivity, "Message",
                                                        mJsonObject.getString(GlobalKeys.MESSAGE));
                                            }
                                        }

                                    } catch (JSONException e) {
                                        CustomProgressDialog.hideProgressDialog();
                                        e.printStackTrace();
                                    }
                                }


                                @Override
                                public void onFailOfResponse(Object... arguments) {
                                    CustomProgressDialog.hideProgressDialog();
                                    try {
                                        if(arguments!=null) {
                                            JSONObject errorJsonObj = (JSONObject) arguments[0];
                                            if (errorJsonObj.has(GlobalKeys.MESSAGE)) {
                                                AppUtils.showDialog(mActivity,
                                                        getString(R.string.dialog_title_message),
                                                        errorJsonObj.getString(GlobalKeys.MESSAGE));
                                            }
                                        }
                                    } catch (Exception e) {
                                        CustomProgressDialog.hideProgressDialog();
                                        Snackbar.make(v, getString(R.string.network_error_please_try_again),
                                                Snackbar.LENGTH_LONG).show();
                                        e.printStackTrace();
                                    }
                                }
                            });


                }
                break;
        }

    }


    public boolean validateForm(View v) {

        if (getEditTextText(R.id.edt_email).equals("")) {

            Snackbar.make(v, getString(R.string.please_enter_email), Snackbar.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

}
