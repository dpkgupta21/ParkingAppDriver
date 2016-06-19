package com.parking.app.parkingappdriver.currentjobs;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parking.app.parkingappdriver.R;
import com.parking.app.parkingappdriver.customViews.CustomAlert;
import com.parking.app.parkingappdriver.customViews.CustomProgressDialog;
import com.parking.app.parkingappdriver.drivermodel.VehicleInspectionScreen;
import com.parking.app.parkingappdriver.model.CurrentJobsDTO;
import com.parking.app.parkingappdriver.myjobs.MyJobsFragment;
import com.parking.app.parkingappdriver.navigationDrawer.DriverNavigationDrawerActivity;
import com.parking.app.parkingappdriver.preferences.SessionManager;
import com.parking.app.parkingappdriver.utils.AppConstants;
import com.parking.app.parkingappdriver.utils.AppUtils;
import com.parking.app.parkingappdriver.utils.WebserviceResponseConstants;
import com.parking.app.parkingappdriver.view.LoginScreen;
import com.parking.app.parkingappdriver.webservices.handler.CurrentJobsAPIHandler;
import com.parking.app.parkingappdriver.webservices.handler.EndJobAPIHandler;
import com.parking.app.parkingappdriver.webservices.handler.ReleaseAPIHandler;
import com.parking.app.parkingappdriver.webservices.handler.StartJobAPIHandler;
import com.parking.app.parkingappdriver.webservices.ihelper.WebAPIResponseListener;

import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by Harish on 12/16/2015.
 */
public class CurrentJobsAdapter extends BaseAdapter implements View.OnClickListener {

    private static final int CREATE_ORDER_TOKEN_EXPIRED_FAILURE = 2000;
    private static final int START_JOB_HANDLE = 2001;
    private static final int RELEASE_JOB_HANDLE = 2002;
    private static final int END_JOB_HANDLE = 2003;
    private Activity mActivity;
    private LayoutInflater mLayoutInflater;

    private ArrayList<CurrentJobsDTO> currentJobsDTOsList;
    private Fragment currentFragment;


    public CurrentJobsAdapter(Activity mActivity, Fragment fragment) {
        this.mActivity = mActivity;
        this.currentFragment = fragment;
        currentJobsDTOsList = new ArrayList<>();
        try {
            mLayoutInflater = (LayoutInflater) mActivity
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Add all user data on GUI
     */
    public void addDataOnList(
            ArrayList<CurrentJobsDTO> currentJobsDTOsList) {

        this.currentJobsDTOsList.clear();
        this.currentJobsDTOsList = currentJobsDTOsList;
    }

    @Override
    public int getCount() {
        if (currentJobsDTOsList != null) {
            return currentJobsDTOsList.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {

        if (currentJobsDTOsList != null) {
            return currentJobsDTOsList.get(position);
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

        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.current_jobs_screen_row, parent, false);
            holder = new ViewHolder();
            holder.mobile = (TextView) convertView.findViewById(R.id.txt_mobile);
            holder.startTime = (TextView) convertView.findViewById(R.id.txt_job_start_time);
            holder.txt_color = (TextView) convertView.findViewById(R.id.txt_color);
            holder.txt_make = (TextView) convertView.findViewById(R.id.txt_make);
            holder.venue = (TextView) convertView.findViewById(R.id.txt_venue);
            holder.model = (TextView) convertView.findViewById(R.id.txt_model);
            holder.customer = (TextView) convertView.findViewById(R.id.txt_customer_name);
            holder.plateNumber = (TextView) convertView.findViewById(R.id.txt_plate_number);
            holder.startBtn = (Button) convertView.findViewById(R.id.btn_start_job);
            holder.releaseBtn = (Button) convertView.findViewById(R.id.btn_release_job);
            holder.endBtn = (Button) convertView.findViewById(R.id.btn_end_job);
            holder.currentjobbuttonLayout = (RelativeLayout) convertView
                    .findViewById(R.id.current_job_buttons_rl);
            holder.endButtonLayout = (RelativeLayout) convertView
                    .findViewById(R.id.current_job_end_button_rl);

            // TODO Vehicle inspection button remove
            holder.vehicleInspectionBtn = (Button) convertView.findViewById(R.id.btn_vehicle_inspection_job);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final CurrentJobsDTO jobsDTO = currentJobsDTOsList.get(position);
        holder.mobile.setText(jobsDTO.getMobile_no());
        holder.txt_color.setText(jobsDTO.getVehicle_Color());
        holder.txt_make.setText(jobsDTO.getVehicle_Make());
        holder.venue.setText(jobsDTO.getVenueName());
        holder.model.setText(jobsDTO.getVehicle_Model());
        holder.customer.setText(jobsDTO.getCustomerName());
        holder.plateNumber.setText(jobsDTO.getPlate_No());
        holder.startTime.setText(AppUtils.getJobsDisplayDateTime(jobsDTO.getJobStartTime()));


        holder.endBtn.setTag(position);
        holder.startBtn.setTag(position);
        holder.releaseBtn.setTag(position);
        holder.vehicleInspectionBtn.setTag(position);

        holder.endBtn.setOnClickListener(this);
        holder.startBtn.setOnClickListener(this);
        holder.releaseBtn.setOnClickListener(this);

        // TODO Vehicle inspection button remove
        holder.vehicleInspectionBtn.setVisibility(View.VISIBLE);
        holder.vehicleInspectionBtn.setOnClickListener(this);

        if (jobsDTO.getJob_Status().equalsIgnoreCase(AppConstants.LOCK)) {
            holder.endButtonLayout.setVisibility(View.GONE);
            holder.currentjobbuttonLayout.setVisibility(View.VISIBLE);

        } else if (jobsDTO.getJob_Status().equalsIgnoreCase(AppConstants.IN_PROGRESS)) {
            holder.currentjobbuttonLayout.setVisibility(View.GONE);
            holder.endButtonLayout.setVisibility(View.VISIBLE);
        } else {
            holder.currentjobbuttonLayout.setVisibility(View.GONE);
            holder.endButtonLayout.setVisibility(View.GONE);
        }


        return convertView;
    }


    @Override
    public void onClick(View v) {
        int pos = Integer.parseInt(v.getTag().toString());

        switch (v.getId()) {
            case R.id.btn_start_job:
                CustomProgressDialog.showProgDialog(mActivity, "Starting Job...");
                new StartJobAPIHandler(mActivity,
                        startJobAPIHandler(),
                        currentJobsDTOsList.get(pos).getJobId());
                break;
            case R.id.btn_end_job:
                CustomProgressDialog.showProgDialog(mActivity, "Ending Job...");
                new EndJobAPIHandler(mActivity,
                        currentJobsDTOsList.get(pos).getJobId(),
                        endJobAPIHandler());
                break;
            case R.id.btn_release_job:
                CustomProgressDialog.showProgDialog(mActivity, "Releasing Job...");
                new ReleaseAPIHandler(mActivity,
                        currentJobsDTOsList.get(pos).getJobId(),
                        releaseJobAPIHandler());
                break;
            case R.id.btn_vehicle_inspection_job:
                Intent intent = new Intent(mActivity, VehicleInspectionScreen.class);
                mActivity.startActivity(intent);
                break;

        }
    }


    private WebAPIResponseListener releaseJobAPIHandler() {
        WebAPIResponseListener responseListener = new WebAPIResponseListener() {
            @Override
            public void onSuccessOfResponse(Object... arguments) {
                String response = (String) arguments[0];
                try {
                    JSONObject jobj = new JSONObject(response);

                    Intent intent = new Intent(mActivity, DriverNavigationDrawerActivity.class);
                    intent.putExtra("fragmentNumber", 1);
                    mActivity.startActivity(intent);


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

                            new CustomAlert(mActivity, CurrentJobsAdapter.this)
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
        };

        return responseListener;
    }


    private WebAPIResponseListener endJobAPIHandler() {
        WebAPIResponseListener responseListener = new WebAPIResponseListener() {
            @Override
            public void onSuccessOfResponse(Object... arguments) {
                String response = (String) arguments[0];
                try {
                    JSONObject jobj = new JSONObject(response);
                    Intent intent = new Intent(mActivity, DriverNavigationDrawerActivity.class);
                    intent.putExtra("fragmentNumber", 1);
                    mActivity.startActivity(intent);


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

                            new CustomAlert(mActivity, CurrentJobsAdapter.this)
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
        };

        return responseListener;
    }


    private WebAPIResponseListener startJobAPIHandler() {
        WebAPIResponseListener responseListener = new WebAPIResponseListener() {
            @Override
            public void onSuccessOfResponse(Object... arguments) {

                String response = (String) arguments[0];
                try {

                    new CurrentJobsAPIHandler(mActivity,
                            ((CurrentJobsFragment) currentFragment).currentJobResponseListner());
                    JSONObject jobj = new JSONObject(response);

                    new CustomAlert(mActivity, CurrentJobsAdapter.this)
                            .singleButtonAlertDialog("This job " + jobj.getString("jobId") +
                                            " has been started." +
                                            " Please inspect vehicle for that job.",
                                    mActivity.getString(R.string.ok),
                                    "singleBtnCallbackResponse", START_JOB_HANDLE);


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

                            new CustomAlert(mActivity, CurrentJobsAdapter.this)
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
        };

        return responseListener;
    }

    public void singleBtnCallbackResponse(Boolean flag, int code) {
        if (flag) {
            if (code == CREATE_ORDER_TOKEN_EXPIRED_FAILURE
                    || code == CREATE_ORDER_TOKEN_EXPIRED_FAILURE) {
                SessionManager.getInstance(mActivity).clearSession();
                Intent intent = new Intent(mActivity, LoginScreen.class);
                mActivity.startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                mActivity.finish();
            } else if (code == START_JOB_HANDLE) {
                Intent intent = new Intent(mActivity, VehicleInspectionScreen.class);
                mActivity.startActivity(intent);
            }
        }
    }


    public class ViewHolder {
        TextView mobile, plateNumber;
        RelativeLayout currentjobbuttonLayout;
        RelativeLayout endButtonLayout;
        TextView txt_color;
        TextView txt_make;
        TextView startTime;
        TextView venue;
        TextView model;
        TextView customer;
        Button startBtn;
        Button releaseBtn;
        Button endBtn;
        // TODO Vehicle inspection button remove
        Button vehicleInspectionBtn;
    }
}
