package com.parking.app.parkingappdriver.webservices.handler;

import android.app.Activity;
import android.content.pm.PackageInstaller;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.parking.app.parkingappdriver.application.ParkingAppController;
import com.parking.app.parkingappdriver.iClasses.GlobalKeys;
import com.parking.app.parkingappdriver.model.LoadJobsDTO;
import com.parking.app.parkingappdriver.preferences.SessionManager;
import com.parking.app.parkingappdriver.utils.AppConstants;
import com.parking.app.parkingappdriver.utils.AppUtils;
import com.parking.app.parkingappdriver.webservices.control.WebserviceAPIErrorHandler;
import com.parking.app.parkingappdriver.webservices.ihelper.WebAPIResponseListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Deepak Singh on 10-May-16.
 */
public class ReleaseAPIHandler {

    private Activity mActivity;
    private String authToken;
    private String TAG = ReleaseAPIHandler.class.getSimpleName();
    private WebAPIResponseListener responseListener;
    private LoadJobsDTO jobsDTO;

    public ReleaseAPIHandler(Activity mActivity, LoadJobsDTO jobsDTO,
                             WebAPIResponseListener responseListener) {
        AppUtils.showProgressDialog(mActivity, "Releasing job...", false);
        this.mActivity = mActivity;
        this.responseListener = responseListener;
        this.authToken = SessionManager.getInstance(mActivity).getAuthToken();
        this.jobsDTO = jobsDTO;

        postAPICall();
    }

    private void postAPICall() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("jobId",jobsDTO.getJobId());
            jsonObject.put("valletId", SessionManager.getInstance(mActivity).getVallet_Id());

            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST,
                    (AppConstants.APP_WEBSERVICE_API_URL + GlobalKeys.RELEASE_JOB_API).trim(),
                    jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            AppUtils.showInfoLog(TAG, "Response :"
                                    + jsonObject);
                            responseListener.onSuccessOfResponse(jsonObject.toString());
                            AppUtils.hideProgressDialog();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            WebserviceAPIErrorHandler.getInstance()
                                    .VolleyErrorHandler(volleyError, mActivity);
                            AppUtils.hideProgressDialog();
                            responseListener.onFailOfResponse(volleyError);
                        }
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(GlobalKeys.HEADER_KEY_CONTENT_TYPE,
                            GlobalKeys.HEADER_VALUE_CONTENT_TYPE);
                    params.put(GlobalKeys.AUTHTOKEN, authToken);
                    return params;
                }
            };

            // Adding request to request queue
            if (ParkingAppController.getInstance() != null) {
                ParkingAppController.getInstance().addToRequestQueue(
                        jsonRequest, GlobalKeys.RELEASE_API_REQUEST_KEY);
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
