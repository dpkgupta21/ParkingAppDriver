package com.parking.app.parkingappdriver.webservices.handler;

import android.app.Activity;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.parking.app.parkingappdriver.application.ParkingAppController;
import com.parking.app.parkingappdriver.iClasses.GlobalKeys;
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
 * Created by Deepak Singh on 09-May-16.
 */
public class DriverConfigAPIHandler {
    private Activity mActivity;
    private String TAG = StartJobAPIHandler.class.getSimpleName();
    private WebAPIResponseListener responseListener;
    private String authToken = "";

    public DriverConfigAPIHandler(Activity activity, WebAPIResponseListener responseListener) {
        AppUtils.showProgressDialog(mActivity, "Configuring driver...", false);
        this.mActivity = activity;
        this.responseListener = responseListener;
        this.authToken = SessionManager.getInstance(mActivity).getAuthToken();

        Log.d("AuthToken",this.authToken);

        postAPICall();
    }

    private void postAPICall() {
        try {
            String url = (AppConstants.APP_WEBSERVICE_API_URL + GlobalKeys.DRIVER_CONFIG).trim()
                    + "?email=" + SessionManager.getInstance(mActivity).getEmail();

//            Request<JSONObject> jsonRequest = new Request<JSONObject>(
//                    Request.Method.GET, url,
//                    new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError volleyError) {
//                            WebserviceAPIErrorHandler.getInstance()
//                                    .VolleyErrorHandler(volleyError, mActivity);
//                            AppUtils.hideProgressDialog();
//                            responseListener.onFailOfResponse(volleyError);
//                        }
//                    }
//            ) {
//
//                @Override
//                public Map<String, String> getHeaders() throws AuthFailureError {
//                    Map<String, String> params = new HashMap<>();
//                    params.put(GlobalKeys.HEADER_KEY_CONTENT_TYPE,
//                            GlobalKeys.HEADER_VALUE_CONTENT_TYPE);
//                    params.put(GlobalKeys.AUTHTOKEN, authToken);
//                    return params;
//                }
//
//                @Override
//                protected Response<JSONObject> parseNetworkResponse(NetworkResponse networkResponse) {
//                    try {
//                        String je = new String(networkResponse.data, HttpHeaderParser
//                                .parseCharset(networkResponse.headers));
//                        return Response.success(new JSONObject(je), HttpHeaderParser
//                                .parseCacheHeaders(networkResponse));
//                    } catch (UnsupportedEncodingException var3) {
//                        return Response.error(new ParseError(var3));
//                    } catch (JSONException var4) {
//                        return Response.error(new ParseError(var4));
//                    }
//                }
//
//                @Override
//                protected void deliverResponse(JSONObject jsonObject) {
//                    AppUtils.showInfoLog(TAG, "Response :"
//                            + jsonObject);
//                    responseListener.onSuccessOfResponse(jsonObject.toString());
//                    AppUtils.hideProgressDialog();
//                }
//            };

            JsonObjectRequest mJsonRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            AppUtils.showInfoLog(TAG, "Response :"
                                    + response);

                            //parseLoginAPIResponse(response.toString());
                            responseListener.onSuccessOfResponse(response);
                            AppUtils.hideProgressDialog();

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    WebserviceAPIErrorHandler.getInstance()
                            .VolleyErrorHandler(error, mActivity);
                    AppUtils.hideProgressDialog();
                    responseListener.onFailOfResponse(error);
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
                    params.put(GlobalKeys.AUTHTOKEN, authToken);
                    return params;
                }

            };
//            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET,
//                    url,
//                    null,
//                    new Response.Listener<JSONObject>() {
//                        @Override
//                        public void onResponse(JSONObject jsonObject) {
//                            AppUtils.showInfoLog(TAG, "Response :"
//                                    + jsonObject);
//                            responseListener.onSuccessOfResponse(jsonObject.toString());
//                            AppUtils.hideProgressDialog();
//                        }
//                    },
//                    new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError volleyError) {
//                            WebserviceAPIErrorHandler.getInstance()
//                                    .VolleyErrorHandler(volleyError, mActivity);
//                            AppUtils.hideProgressDialog();
//                            responseListener.onFailOfResponse(volleyError);
//                        }
//                    }
//            ) {
//                @Override
//                public Map<String, String> getHeaders() throws AuthFailureError {
//                    Map<String, String> params = new HashMap<>();
//                    params.put(GlobalKeys.HEADER_KEY_CONTENT_TYPE,
//                            GlobalKeys.HEADER_VALUE_CONTENT_TYPE);
//                    params.put(GlobalKeys.AUTHTOKEN, authToken);
//                    return params;
//                }
//
//            };

            // Adding request to request queue
            if (ParkingAppController.getInstance() != null) {
                ParkingAppController.getInstance().addToRequestQueue(
                        mJsonRequest, GlobalKeys.DRIVER_CONFIG_REQUEST_KEY);
            }
            // set request time-out
            mJsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                    AppConstants.ONE_SECOND * 30, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
