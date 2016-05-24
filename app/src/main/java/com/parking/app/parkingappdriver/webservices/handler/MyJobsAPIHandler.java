package com.parking.app.parkingappdriver.webservices.handler;

import android.app.Activity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.parking.app.parkingappdriver.application.ParkingAppController;
import com.parking.app.parkingappdriver.iClasses.GlobalKeys;
import com.parking.app.parkingappdriver.preferences.SessionManager;
import com.parking.app.parkingappdriver.utils.AppConstants;
import com.parking.app.parkingappdriver.utils.AppUtils;
import com.parking.app.parkingappdriver.webservices.control.WebserviceAPIErrorHandler;
import com.parking.app.parkingappdriver.webservices.ihelper.WebAPIResponseListener;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;


/**
 * Pillion Login API Handler
 */
public class MyJobsAPIHandler {
    /**
     * Instance object of Login API
     */
    private Activity mActivity;
    /**
     * Debug TAG
     */
    private String TAG = MyJobsAPIHandler.class.getSimpleName();
    /**
     * API Response Listener
     */
    private WebAPIResponseListener mResponseListener;


    private String authToken = "";
    private String userid = "";

    /**
     * @param mActivity
     * @param webAPIResponseListener
     */
    public MyJobsAPIHandler(Activity mActivity, WebAPIResponseListener webAPIResponseListener) {
        AppUtils
                .showProgressDialog(mActivity, "Fetching Jobs...", false);
        this.mActivity = mActivity;
        this.mResponseListener = webAPIResponseListener;
        authToken = SessionManager.getInstance(mActivity).getAuthToken();
        userid= SessionManager.getInstance(mActivity).getUserId();
        postAPICall();

    }

    /**
     * Making json object request
     */
    public void postAPICall() {


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest((AppConstants.APP_WEBSERVICE_API_URL
                + GlobalKeys.LOAD_JOBS_API)
                .trim(), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                AppUtils.showInfoLog(TAG, "Response :"
                        + jsonArray);
                mResponseListener.onSuccessOfResponse(jsonArray.toString());
                AppUtils.hideProgressDialog();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                WebserviceAPIErrorHandler.getInstance()
                        .VolleyErrorHandler(error, mActivity);
                AppUtils.hideProgressDialog();
                mResponseListener.onFailOfResponse(error);
            }
        }) {
            /*
             * /* (non-Javadoc)
             *
             * @see com.android.volley.Request#getHeaders()
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(GlobalKeys.HEADER_KEY_CONTENT_TYPE,
                        GlobalKeys.HEADER_VALUE_CONTENT_TYPE);
                params.put(GlobalKeys.HEADER_KEY_ACCEPT,
                        GlobalKeys.HEADER_VALUE_CONTENT_TYPE);
                params.put(GlobalKeys.USERID, userid);
                params.put(GlobalKeys.AUTHTOKEN, authToken);
                return params;
            }

        };
        // Adding request to request queue
        if (ParkingAppController.getInstance() != null) {
            ParkingAppController.getInstance().addToRequestQueue(
                    jsonArrayRequest, GlobalKeys.PILLION_LOAD_JOB_REQUEST_KEY);
        }
        // set request time-out
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                AppConstants.ONE_SECOND * 30, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }


}
