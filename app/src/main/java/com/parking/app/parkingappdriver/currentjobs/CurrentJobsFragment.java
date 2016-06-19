package com.parking.app.parkingappdriver.currentjobs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.parking.app.parkingappdriver.R;
import com.parking.app.parkingappdriver.customViews.CustomAlert;
import com.parking.app.parkingappdriver.customViews.CustomProgressDialog;
import com.parking.app.parkingappdriver.fragments.BaseFragment;
import com.parking.app.parkingappdriver.model.CurrentJobsDTO;
import com.parking.app.parkingappdriver.preferences.SessionManager;
import com.parking.app.parkingappdriver.utils.AppUtils;
import com.parking.app.parkingappdriver.utils.WebserviceResponseConstants;
import com.parking.app.parkingappdriver.view.LoginScreen;
import com.parking.app.parkingappdriver.webservices.handler.CurrentJobsAPIHandler;
import com.parking.app.parkingappdriver.webservices.ihelper.WebAPIResponseListener;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class CurrentJobsFragment extends BaseFragment {

    private View view;
    private Activity mActivity;
    private ListView listView;
    private static ArrayList<CurrentJobsDTO> jobsList;
    private static CurrentJobsAdapter currentJobsAdapter;

    private static final int CREATE_ORDER_TOKEN_EXPIRED_FAILURE = 2000;

    public static CurrentJobsFragment newInstance(String param1, String param2) {
        CurrentJobsFragment fragment = new CurrentJobsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_current_jobs, container, false);
        mActivity = getActivity();
        initViews();

        jobsList = new ArrayList<>();

        //calling current job web service.
        CustomProgressDialog.showProgDialog(mActivity, "Current Jobs...");
        new CurrentJobsAPIHandler(mActivity, currentJobResponseListner());
        currentJobsAdapter = new CurrentJobsAdapter(mActivity, this);

        listView.setAdapter(currentJobsAdapter);
        return view;
    }

    private void initViews() {
        listView = (ListView) view.findViewById(R.id.jobs_listview);
    }

    public WebAPIResponseListener currentJobResponseListner() {
        WebAPIResponseListener webAPIResponseListener = new WebAPIResponseListener() {
            @Override
            public void onSuccessOfResponse(Object... arguments) {
                try {
                    CustomProgressDialog.hideProgressDialog();

                    String response = (String) arguments[0];
                    Gson gson = new Gson();
                    Type listType = new TypeToken<ArrayList<CurrentJobsDTO>>() {
                    }.getType();
                    if (jobsList != null) {
                        jobsList.clear();
                    }
                    jobsList = gson.fromJson(response, listType);
                    currentJobsAdapter.addDataOnList(jobsList);
                    currentJobsAdapter.notifyDataSetChanged();
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
                                equalsIgnoreCase(WebserviceResponseConstants.NO_ORDERS_FOUND)) {

                            setViewVisibility(R.id.txt_no_data, view, View.VISIBLE);
                            setViewText(R.id.txt_no_data,
                                    AppUtils.getWebServiceErrorMsg(errorJsonObj), view);

                        } else if (AppUtils.getWebServiceErrorCode(errorJsonObj).
                                equalsIgnoreCase(WebserviceResponseConstants.ERROR_TOKEN_EXPIRED) ||
                                AppUtils.getWebServiceErrorCode(errorJsonObj).
                                        equalsIgnoreCase(WebserviceResponseConstants.ERROR_INVALID_TOKEN)
                                ||
                                AppUtils.getWebServiceErrorCode(errorJsonObj).
                                        equalsIgnoreCase(WebserviceResponseConstants.ERROR_TOKEN_MISMATCH)) {

                            new CustomAlert(mActivity, CurrentJobsFragment.this)
                                    .singleButtonAlertDialog(
                                            AppUtils.getWebServiceErrorMsg(errorJsonObj),
                                            getString(R.string.ok),
                                            "singleBtnCallbackResponse",
                                            CREATE_ORDER_TOKEN_EXPIRED_FAILURE);
                        }
                    } else {
                        AppUtils.showDialog(mActivity,
                                getString(R.string.dialog_title_alert),
                                getString(R.string.network_error_please_try_again));
                    }
                } catch (Exception e) {
                    CustomProgressDialog.hideProgressDialog();
                    AppUtils.showDialog(mActivity,
                            getString(R.string.dialog_title_alert),
                            getString(R.string.network_error_please_try_again));
                    e.printStackTrace();
                }
            }
        };

        return webAPIResponseListener;
    }


    public void singleBtnCallbackResponse(Boolean flag, int code) {
        if (flag) {
            if (code == CREATE_ORDER_TOKEN_EXPIRED_FAILURE) {
                SessionManager.getInstance(mActivity).clearSession();
                Intent intent = new Intent(mActivity, LoginScreen.class);
                startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                mActivity.finish();
            }
        }
    }
}
