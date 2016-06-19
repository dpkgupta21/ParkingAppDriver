package com.parking.app.parkingappdriver.webservices.handler;

import android.app.Activity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.parking.app.parkingappdriver.application.ParkingAppController;
import com.parking.app.parkingappdriver.iClasses.GlobalKeys;
import com.parking.app.parkingappdriver.model.CurrentJobsDTO;
import com.parking.app.parkingappdriver.preferences.SessionManager;
import com.parking.app.parkingappdriver.utils.AppConstants;
import com.parking.app.parkingappdriver.utils.AppUtils;
import com.parking.app.parkingappdriver.webservices.control.WebserviceAPIErrorHandler;
import com.parking.app.parkingappdriver.webservices.ihelper.WebAPIResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Deepak Singh on 10-May-16.
 */
public class EndJobAPIHandler {
    private Activity mActivity;
    private String authToken;
    private String TAG = EndJobAPIHandler.class.getSimpleName();
    private WebAPIResponseListener responseListener;
    private String jobId;

    public EndJobAPIHandler(Activity mActivity, String jobId,
                            WebAPIResponseListener responseListener) {
        this.mActivity = mActivity;
        this.responseListener = responseListener;
        this.authToken = SessionManager.getInstance(mActivity).getAuthToken();
        this.jobId = jobId;

        postAPICall();
    }

    private void postAPICall() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("jobId", jobId);
            jsonObject.put("valletId", SessionManager.getInstance(mActivity).getVallet_Id());

            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST,
                    (AppConstants.APP_WEBSERVICE_API_URL + GlobalKeys.END_JOB_API).trim(),
                    jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            AppUtils.showInfoLog(TAG, "Response :"
                                    + jsonObject);
                            responseListener.onSuccessOfResponse(jsonObject.toString());
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
                                responseListener.onFailOfResponse(errorJsonObj);
                            } catch (UnsupportedEncodingException e) {
                                responseListener.onFailOfResponse(errorJsonObj);
                                e.printStackTrace();
                            } catch (JSONException e) {
                                responseListener.onFailOfResponse(errorJsonObj);
                                e.printStackTrace();
                            } catch (Exception e) {
                                responseListener.onFailOfResponse(errorJsonObj);
                                e.printStackTrace();
                            }
                        }
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
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
                        jsonRequest, GlobalKeys.END_JOB_API_REQUEST_KEY);
            }
            // set request time-out
            jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                    AppConstants.ONE_SECOND * 20, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
