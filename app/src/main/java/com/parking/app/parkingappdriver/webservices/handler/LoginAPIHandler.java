package com.parking.app.parkingappdriver.webservices.handler;

import android.app.Activity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.parking.app.parkingappdriver.application.ParkingAppController;
import com.parking.app.parkingappdriver.iClasses.GlobalKeys;
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
 * Pillion Login API Handler
 */
public class LoginAPIHandler {
    /**
     * Instance object of Login API
     */
    private Activity mActivity;
    /**
     * Debug TAG
     */
    private String TAG = LoginAPIHandler.class.getSimpleName();
    /**
     * Request Data
     */
    private String emailId, password;

    private String device_token, device_platform;
    /**
     * API Response Listener
     */
    private WebAPIResponseListener mResponseListener;

    /**
     * @param mActivity
     * @param password
     * @param email
     * @param webAPIResponseListener
     */
    public LoginAPIHandler(Activity mActivity, String email,
                           String password, WebAPIResponseListener webAPIResponseListener) {
        this.mActivity = mActivity;
        this.emailId = email;
        this.password = password;
        this.mResponseListener = webAPIResponseListener;
        postAPICall();

    }

    /**
     * Making json object request
     */
    public void postAPICall() {
        /**
         * String Request
         */

        JSONObject mJsonObjectRequest = new JSONObject();
        try {
            mJsonObjectRequest.put(GlobalKeys.EMAIL, emailId);
            mJsonObjectRequest.put(GlobalKeys.PASSWORD, password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest mJsonRequest = new JsonObjectRequest(
                Method.POST,
                (AppConstants.APP_WEBSERVICE_API_URL + GlobalKeys.LOGIN_API)
                        .trim(), mJsonObjectRequest,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        AppUtils.showInfoLog(TAG, "Response :"
                                + response);

                        parseLoginAPIResponse(response.toString());
                        mResponseListener.onSuccessOfResponse(response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                JSONObject errorJsonObj=null;
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
                return params;
            }

        };
        // Adding request to request queue
        if (ParkingAppController.getInstance() != null) {
            ParkingAppController.getInstance().addToRequestQueue(
                    mJsonRequest, GlobalKeys.PILLION_LOGIN_REQUEST_KEY);
        }
        // set request time-out
        mJsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                AppConstants.ONE_SECOND * 30, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    /**
     * Parse LoginAPI Response
     *
     * @param response
     */
    protected void parseLoginAPIResponse(String response) {

    }
}
