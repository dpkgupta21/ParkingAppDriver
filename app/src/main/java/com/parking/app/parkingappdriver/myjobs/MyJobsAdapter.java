package com.parking.app.parkingappdriver.myjobs;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.parking.app.parkingappdriver.R;
import com.parking.app.parkingappdriver.customViews.CustomAlert;
import com.parking.app.parkingappdriver.customViews.CustomProgressDialog;
import com.parking.app.parkingappdriver.model.LoadJobsDTO;
import com.parking.app.parkingappdriver.preferences.SessionManager;
import com.parking.app.parkingappdriver.utils.AppUtils;
import com.parking.app.parkingappdriver.utils.WebserviceResponseConstants;
import com.parking.app.parkingappdriver.view.LoginScreen;
import com.parking.app.parkingappdriver.webservices.handler.ConfirmJobAPIHandler;
import com.parking.app.parkingappdriver.webservices.handler.MyJobsAPIHandler;
import com.parking.app.parkingappdriver.webservices.ihelper.WebAPIResponseListener;

import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by Deepak on 12/16/2015.
 */
public class MyJobsAdapter extends BaseAdapter {

    private static final int CREATE_ORDER_TOKEN_EXPIRED_FAILURE = 2000;
    private static final int CONFIRM_JOB_HANDLE = 2001;
    private Activity mActivity;
    //private LayoutInflater mLayoutInflater;

    private ArrayList<LoadJobsDTO> mMyJobsModelArrayList;
    private Fragment currentFragment;


    public MyJobsAdapter(Activity mActivity, Fragment fragment) {
        this.mActivity = mActivity;
        this.currentFragment = fragment;
        mMyJobsModelArrayList = new ArrayList<>();
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Add all user data on GUI
     */
    public void addDataOnList(
            ArrayList<LoadJobsDTO> myJobsModelArrayList) {

        this.mMyJobsModelArrayList.clear();
        this.mMyJobsModelArrayList = myJobsModelArrayList;
    }

    @Override
    public int getCount() {
        if (mMyJobsModelArrayList != null) {
            return mMyJobsModelArrayList.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {

        if (mMyJobsModelArrayList != null) {
            return mMyJobsModelArrayList.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        try {
            ViewHolder holder = null;
            if (convertView == null) {
                LayoutInflater mLayoutInflater = (LayoutInflater) mActivity
                        .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                convertView = mLayoutInflater.inflate(R.layout.my_jobs_screen_row, parent, false);
                holder = new ViewHolder();
                holder.mobile = (TextView) convertView.findViewById(R.id.txt_mobile);
                holder.confirm_job_btn = (TextView) convertView.findViewById(R.id.confirm_job_btn);
                holder.startTime = (TextView) convertView.findViewById(R.id.txt_job_start_time);
                holder.txt_color = (TextView) convertView.findViewById(R.id.txt_color);
                holder.txt_make = (TextView) convertView.findViewById(R.id.txt_make);
                holder.venue = (TextView) convertView.findViewById(R.id.txt_venue);
                holder.model = (TextView) convertView.findViewById(R.id.txt_model);
                holder.customer = (TextView) convertView.findViewById(R.id.txt_customer_name);
                holder.plateNumber = (TextView) convertView.findViewById(R.id.txt_plate_number);

                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final LoadJobsDTO jobsDTO = mMyJobsModelArrayList.get(position);
            holder.mobile.setText(jobsDTO.getMobile_no());
            holder.txt_color.setText(jobsDTO.getVehicle_Color());
            holder.txt_make.setText(jobsDTO.getVehicle_Make());
            holder.venue.setText(jobsDTO.getVenueName());
            holder.model.setText(jobsDTO.getVehicle_Model());
            holder.customer.setText(jobsDTO.getCustomerName());
            holder.plateNumber.setText(jobsDTO.getPlate_No());
            holder.startTime.setText(AppUtils.getJobsDisplayDateTime(jobsDTO.getJobStartTime()));


            holder.confirm_job_btn.setVisibility(View.VISIBLE);
            holder.confirm_job_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    confirmJob(mMyJobsModelArrayList.get(position));
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    public void confirmJob(LoadJobsDTO jobsDTO) {
        CustomProgressDialog.showProgDialog(mActivity, "Confirming Job...");
        String valletId = SessionManager.getInstance(mActivity)
                .getVallet_Id();

        new ConfirmJobAPIHandler(mActivity, jobsDTO.getJobId(),
                valletId, new WebAPIResponseListener() {
            @Override
            public void onSuccessOfResponse(Object... arguments) {
                CustomProgressDialog.hideProgressDialog();
                try {
                    String response = (String) arguments[0];
                    JSONObject jobj = new JSONObject(response);
                    new CustomAlert(mActivity, MyJobsAdapter.this)
                            .singleButtonAlertDialog("This job " + jobj.getString("jobId") +
                                            " has been confirmed." +
                                            " Please see it in your current job.",
                                    mActivity.getString(R.string.ok),
                                    "singleBtnCallbackResponse", CONFIRM_JOB_HANDLE);


                } catch (Exception e) {

                    CustomProgressDialog.hideProgressDialog();
                    AppUtils.showDialog(mActivity,
                            mActivity.getString(R.string.dialog_title_alert),
                            mActivity.getString(R.string.network_error_please_try_again));
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
                                equalsIgnoreCase(WebserviceResponseConstants.ERROR_TOKEN_EXPIRED)) {

                            new CustomAlert(mActivity, MyJobsAdapter.this)
                                    .singleButtonAlertDialog(
                                            AppUtils.getWebServiceErrorMsg(errorJsonObj),
                                            mActivity.getString(R.string.ok),
                                            "singleBtnCallbackResponse",
                                            CREATE_ORDER_TOKEN_EXPIRED_FAILURE);
                        }
                    } else {
                        AppUtils.showDialog(mActivity,
                                mActivity.getString(R.string.dialog_title_alert),
                                mActivity.getString(R.string.network_error_please_try_again));
                    }
                } catch (Exception e) {
                    CustomProgressDialog.hideProgressDialog();
                    AppUtils.showDialog(mActivity,
                            mActivity.getString(R.string.dialog_title_alert),
                            mActivity.getString(R.string.network_error_please_try_again));
                    e.printStackTrace();
                }

            }
        });

    }


    public void singleBtnCallbackResponse(Boolean flag, int code) {
        if (flag) {
            if (code == CREATE_ORDER_TOKEN_EXPIRED_FAILURE
                    || code == CREATE_ORDER_TOKEN_EXPIRED_FAILURE) {
                SessionManager.getInstance(mActivity).clearSession();
                Intent intent = new Intent(mActivity, LoginScreen.class);
                mActivity.startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                mActivity.finish();
            } else if (code == CONFIRM_JOB_HANDLE) {
                if (currentFragment instanceof MyJobsFragment) {
                    CustomProgressDialog.showProgDialog(mActivity, null);
                    new MyJobsAPIHandler(mActivity, ((MyJobsFragment) currentFragment).
                            myJobsResponseListner());
                }

            }
        }
    }

    public class ViewHolder {
        TextView confirm_job_btn;
        TextView mobile;
        TextView plateNumber;
        TextView txt_color;
        TextView txt_make;
        TextView startTime;
        TextView venue;
        TextView model;
        TextView customer;
    }
}
