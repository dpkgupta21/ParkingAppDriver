package com.parking.app.parkingappdriver.webservices.control;

import android.app.Activity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.parking.app.parkingappdriver.R;
import com.parking.app.parkingappdriver.utils.AppUtils;


/**
 * Webservice API Error Handler
 */
public class WebserviceAPIErrorHandler {
    /**
     * Instance of This class
     */
    public static WebserviceAPIErrorHandler mErrorHandler;
    /**
     * Debugging TAG
     */
    private String TAG = WebserviceAPIErrorHandler.class.getSimpleName();

    private WebserviceAPIErrorHandler() {
    }

    /**
     * Get Instance of this class
     *
     * @return
     */
    public static WebserviceAPIErrorHandler getInstance() {
        if (mErrorHandler == null)
            mErrorHandler = new WebserviceAPIErrorHandler();
        return mErrorHandler;

    }

    /**
     * Volley Error Handler
     *
     * @param mError
     */
    public void VolleyErrorHandler(VolleyError mError, Activity mActivity) {
        AppUtils.showErrorLog(TAG, "VolleyError :" + mError);
        if (mError instanceof NoConnectionError) {
            AppUtils.showToast(mActivity, mActivity.getResources()
                    .getString(R.string.network_error));
        } else if (mError instanceof TimeoutError) {
            AppUtils.showToast(mActivity, mActivity.getResources()
                    .getString(R.string.network_slow_error));
        } else if (mError instanceof AuthFailureError) {
        } else if (mError instanceof ServerError) {
        } else if (mError instanceof NetworkError) {
            AppUtils.showToast(mActivity, mActivity.getResources()
                    .getString(R.string.network_error));
        } else if (mError instanceof ParseError) {
        }
    }
}
