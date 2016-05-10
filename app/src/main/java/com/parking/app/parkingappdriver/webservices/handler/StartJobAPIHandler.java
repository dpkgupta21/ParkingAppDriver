package com.parking.app.parkingappdriver.webservices.handler;

import android.app.Activity;

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

/**
 * Created by Deepak Singh on 09-May-16.
 */
public class StartJobAPIHandler {

    private Activity mActivity;
    private String TAG = StartJobAPIHandler.class.getSimpleName();
    private WebAPIResponseListener responseListener;
    private String authToken = "";
    private LoadJobsDTO jobsDTO;

    public StartJobAPIHandler(Activity activity, WebAPIResponseListener responseListener,
                              LoadJobsDTO jobsDTO) {
        AppUtils.showProgressDialog(mActivity, "Starting Job...", false);
        this.mActivity = activity;
        this.responseListener = responseListener;
        this.authToken = SessionManager.getInstance(mActivity).getAuthToken();
        this.jobsDTO = jobsDTO;

        postAPICall();
    }

    private void postAPICall() {
        try {
            JSONObject object = new JSONObject();
            object.put("jobId", jobsDTO.getJobId());
            object.put("valletId", SessionManager.getInstance(mActivity).getVallet_Id());

            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST,
                    (AppConstants.APP_WEBSERVICE_API_URL + GlobalKeys.START_JOB_API).trim(),
                    object,
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
            );

            // Adding request to request queue
            if (ParkingAppController.getInstance() != null) {
                ParkingAppController.getInstance().addToRequestQueue(
                        jsonRequest, GlobalKeys.START_JOB_REQUEST_KEY);
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
