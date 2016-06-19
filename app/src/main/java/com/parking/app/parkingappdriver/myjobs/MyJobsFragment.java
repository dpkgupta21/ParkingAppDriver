package com.parking.app.parkingappdriver.myjobs;

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
import com.parking.app.parkingappdriver.model.LoadJobsDTO;
import com.parking.app.parkingappdriver.preferences.SessionManager;
import com.parking.app.parkingappdriver.utils.AppUtils;
import com.parking.app.parkingappdriver.utils.WebserviceResponseConstants;
import com.parking.app.parkingappdriver.view.LoginScreen;
import com.parking.app.parkingappdriver.webservices.handler.ConfirmJobAPIHandler;
import com.parking.app.parkingappdriver.webservices.handler.MyJobsAPIHandler;
import com.parking.app.parkingappdriver.webservices.ihelper.WebAPIResponseListener;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class MyJobsFragment extends BaseFragment {

    private View view;
    private Activity mActivity;
    private ListView listView;

    private static ArrayList<LoadJobsDTO> myJobsDTOArrayList;
    private static MyJobsAdapter myJobsAdapter;

    private static final int CREATE_ORDER_TOKEN_EXPIRED_FAILURE = 2000;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_myjobs_screen, container, false);

        return view;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = getActivity();
        initViews();

        myJobsDTOArrayList = new ArrayList<LoadJobsDTO>();

        CustomProgressDialog.showProgDialog(mActivity, "Load Jobs...");
        new MyJobsAPIHandler(mActivity, myJobsResponseListner());

        myJobsAdapter = new MyJobsAdapter(getActivity(), this);

        listView.setAdapter(myJobsAdapter);

    }

    public WebAPIResponseListener myJobsResponseListner() {

        WebAPIResponseListener webAPIResponseListener = new WebAPIResponseListener() {
            @Override
            public void onSuccessOfResponse(Object... arguments) {
                try {
                    CustomProgressDialog.hideProgressDialog();
                    String response = (String) arguments[0];
                    Gson gson = new Gson();
                    Type listType = new TypeToken<ArrayList<LoadJobsDTO>>() {
                    }.getType();
                    if (myJobsDTOArrayList != null) {
                        myJobsDTOArrayList.clear();
                    }
                    myJobsDTOArrayList = gson.fromJson(response, listType);
                    myJobsAdapter.addDataOnList(myJobsDTOArrayList);
                    myJobsAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    CustomProgressDialog.hideProgressDialog();
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

                            new CustomAlert(mActivity, MyJobsFragment.this)
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

    private void initViews() {
        listView = (ListView) view.findViewById(R.id.jobs_listview);

    }

//    public void confirmJob(LoadJobsDTO jobsDTO) {
//        String valletId = SessionManager.getInstance(mActivity)
//                .getVallet_Id();
//
//        new ConfirmJobAPIHandler(mActivity, jobsDTO.getJobId(),
//                valletId, new WebAPIResponseListener() {
//            @Override
//            public void onSuccessOfResponse(Object... arguments) {
//
//                String response = (String) arguments[0];
//                try {
//                    JSONObject jobj = new JSONObject(response);
//                    new CustomAlert(mActivity, mActivity)
//                            .singleButtonAlertDialog("This job " + jobj.getString("jobId") +
//                                            " has been confirmed." +
//                                            " Please see it in your current job.",
//                                    mActivity.getString(R.string.ok),
//                                    "singleBtnCallbackResponse", 1000);
//
//
//                } catch (Exception e) {
//
//                    CustomProgressDialog.hideProgressDialog();
//                    AppUtils.showDialog(mActivity,
//                            getString(R.string.dialog_title_alert),
//                            getString(R.string.network_error_please_try_again));
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailOfResponse(Object... arguments) {
//                try {
//                    CustomProgressDialog.hideProgressDialog();
//                    if (arguments != null) {
//                        JSONObject errorJsonObj = (JSONObject) arguments[0];
//                        if (AppUtils.getWebServiceErrorCode(errorJsonObj).
//                                equalsIgnoreCase(WebserviceResponseConstants.ERROR_TOKEN_EXPIRED)) {
//
//                            new CustomAlert(mActivity, mActivity)
//                                    .singleButtonAlertDialog(
//                                            AppUtils.getWebServiceErrorMsg(errorJsonObj),
//                                            getString(R.string.ok),
//                                            "singleBtnCallbackResponse",
//                                            CREATE_ORDER_TOKEN_EXPIRED_FAILURE);
//                        }
//                    } else {
//                        AppUtils.showDialog(mActivity,
//                                getString(R.string.dialog_title_alert),
//                                getString(R.string.network_error_please_try_again));
//                    }
//                } catch (Exception e) {
//                    CustomProgressDialog.hideProgressDialog();
//                    AppUtils.showDialog(mActivity,
//                            getString(R.string.dialog_title_alert),
//                            getString(R.string.network_error_please_try_again));
//                    e.printStackTrace();
//                }
//
//            }
//        });
//
//    }


    public void singleBtnCallbackResponse(Boolean flag, int code) {
        if (flag) {
            if (code == CREATE_ORDER_TOKEN_EXPIRED_FAILURE
                    || code == CREATE_ORDER_TOKEN_EXPIRED_FAILURE) {
                SessionManager.getInstance(mActivity).clearSession();
                Intent intent = new Intent(mActivity, LoginScreen.class);
                startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                mActivity.finish();
            }
        }
    }


}
