package com.parking.app.parkingappdriver.webservices.handler;

import android.app.Activity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.parking.app.parkingappdriver.application.ParkingAppController;
import com.parking.app.parkingappdriver.iClasses.GlobalKeys;
import com.parking.app.parkingappdriver.preferences.SessionManager;
import com.parking.app.parkingappdriver.utils.AppConstants;
import com.parking.app.parkingappdriver.utils.AppUtils;
import com.parking.app.parkingappdriver.webservices.control.WebserviceAPIErrorHandler;
import com.parking.app.parkingappdriver.webservices.ihelper.WebAPIResponseListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class CurrentJobsAPIHandler {

    /**
     * Instance object of Login API
     */
    private Activity mActivity;
    /**
     * Debug TAG
     */
    private String TAG = CurrentJobsAPIHandler.class.getSimpleName();
    /**
     * API Response Listener
     */
    private WebAPIResponseListener mResponseListener;


    String authToken = "";

    public CurrentJobsAPIHandler(Activity mActivity, WebAPIResponseListener mResponseListener) {

        this.mActivity = mActivity;
        this.mResponseListener = mResponseListener;
        authToken = SessionManager.getInstance(mActivity).getAuthToken();
        postAPICall();
    }

    private void postAPICall() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest((AppConstants.APP_WEBSERVICE_API_URL
                + GlobalKeys.CURRENT_JOB_API).trim() + "?valletNumber="
                + SessionManager.getInstance(mActivity).getValletNumber(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        AppUtils.showInfoLog(TAG, "Response :"
                                + jsonArray);
                        mResponseListener.onSuccessOfResponse(jsonArray.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        JSONObject errorJsonObj = null;
                        try {
                            Response<JSONObject> errorResponse = Response.error(error);
                            String errorString = new String(errorResponse.error.networkResponse.data,
                                    HttpHeaderParser
                                            .parseCharset(errorResponse.error.networkResponse.headers));
                            errorJsonObj = new JSONObject(errorString);
                            WebserviceAPIErrorHandler.getInstance()
                                    .VolleyErrorHandler(error, mActivity);
                            mResponseListener.onFailOfResponse(errorJsonObj);
                        } catch (UnsupportedEncodingException e) {
                            mResponseListener.onFailOfResponse(errorJsonObj);
                            e.printStackTrace();
                        } catch (JSONException e) {
                            mResponseListener.onFailOfResponse(errorJsonObj);
                            e.printStackTrace();
                        } catch (Exception e) {
                            mResponseListener.onFailOfResponse(errorJsonObj);
                            e.printStackTrace();
                        }
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(GlobalKeys.HEADER_KEY_CONTENT_TYPE,
                        GlobalKeys.HEADER_VALUE_CONTENT_TYPE);
                params.put(GlobalKeys.AUTHTOKEN, authToken);
                params.put(GlobalKeys.USERID, SessionManager.getInstance(mActivity).getUserId());
                return params;
            }
        };

        // Adding request to request queue
        if (ParkingAppController.getInstance() != null) {
            ParkingAppController.getInstance().addToRequestQueue(
                    jsonArrayRequest, GlobalKeys.CURRENT_JOB_REQUEST_KEY);
        }
        // set request time-out
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                AppConstants.ONE_SECOND * 20, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
}
