package com.parking.app.parkingappdriver.myjobs;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.parking.app.parkingappdriver.R;
import com.parking.app.parkingappdriver.currentjobs.CurrentJobsFragment;
import com.parking.app.parkingappdriver.drivermodel.VehicleInspectionScreen;
import com.parking.app.parkingappdriver.model.LoadJobsDTO;
import com.parking.app.parkingappdriver.navigationDrawer.DriverNavigationDrawerActivity;
import com.parking.app.parkingappdriver.webservices.handler.EndJobAPIHandler;
import com.parking.app.parkingappdriver.webservices.handler.ReleaseAPIHandler;
import com.parking.app.parkingappdriver.webservices.handler.StartJobAPIHandler;
import com.parking.app.parkingappdriver.webservices.ihelper.WebAPIResponseListener;

import java.util.ArrayList;


/**
 * Created by Harish on 12/16/2015.
 */
public class MyJobsAdapter extends BaseAdapter {


    Activity mActivity;
    LayoutInflater mLayoutInflater;

    ArrayList<LoadJobsDTO> mMyJobsModelArrayList;
    private Fragment myJobsFragment;


    public MyJobsAdapter(Activity mActivity, Fragment fragment) {
        this.mActivity = mActivity;
        this.myJobsFragment = fragment;
        mMyJobsModelArrayList = new ArrayList<>();
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

        ViewHolder holder = null;
        if (convertView == null) {
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
            holder.startBtn = (Button) convertView.findViewById(R.id.btn_start_job);
            holder.releaseBtn = (Button) convertView.findViewById(R.id.btn_release_job);
            holder.currentjobbuttonLayout = (RelativeLayout) convertView
                    .findViewById(R.id.current_job_buttons_rl);
            holder.endButtonLayout = (RelativeLayout) convertView
                    .findViewById(R.id.current_job_end_button_rl);

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
        holder.startTime.setText(jobsDTO.getJobStartTime());

        Button endBtn = (Button) holder.endButtonLayout.findViewById(R.id.btn_end_job);
        endBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new EndJobAPIHandler(mActivity, jobsDTO, manageAPIHandler());
            }
        });

        if (myJobsFragment instanceof CurrentJobsFragment) {
            holder.confirm_job_btn.setVisibility(View.GONE);
            if (jobsDTO.getJob_Status().equalsIgnoreCase("lock")) {
                holder.endButtonLayout.setVisibility(View.GONE);
                holder.currentjobbuttonLayout.setVisibility(View.VISIBLE);
                final RelativeLayout relativeLayout = holder.currentjobbuttonLayout;
                final RelativeLayout endButtonLayout = holder.endButtonLayout;
                holder.startBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        new StartJobAPIHandler(mActivity,
                                manageStartJobAPIHandler(relativeLayout, endButtonLayout), jobsDTO);
                    }
                });

                holder.releaseBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new ReleaseAPIHandler(mActivity, jobsDTO, manageAPIHandler());
                    }
                });
            } else if (jobsDTO.getJob_Status().equalsIgnoreCase("INPROGRESS")) {
                holder.currentjobbuttonLayout.setVisibility(View.GONE);
                holder.endButtonLayout.setVisibility(View.VISIBLE);
            } else {
                holder.currentjobbuttonLayout.setVisibility(View.GONE);
                holder.endButtonLayout.setVisibility(View.GONE);
            }
        } else {
            holder.confirm_job_btn.setVisibility(View.VISIBLE);
            holder.currentjobbuttonLayout.setVisibility(View.GONE);
            holder.confirm_job_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (myJobsFragment instanceof MyJobsFragment) {
                        ((MyJobsFragment) myJobsFragment)
                                .confirmJob(mMyJobsModelArrayList.get(position));
                    }
                }
            });
        }

        return convertView;
    }


    private WebAPIResponseListener manageAPIHandler() {
        WebAPIResponseListener responseListener = new WebAPIResponseListener() {
            @Override
            public void onSuccessOfResponse(Object... arguments) {
                Intent intent = new Intent(mActivity, DriverNavigationDrawerActivity.class);
                intent.putExtra("fragmentNumber", 1);
                mActivity.startActivity(intent);

            }

            @Override
            public void onFailOfResponse(Object... arguments) {
                VolleyError error = (VolleyError) arguments[0];
                Toast.makeText(mActivity, "Operation failed.", Toast.LENGTH_SHORT).show();
                Log.i(MyJobsAdapter.class.getSimpleName(), "Job operation error " + error.toString());
            }
        };

        return responseListener;
    }

    private WebAPIResponseListener manageStartJobAPIHandler(final RelativeLayout currentLayout,
                                                            final RelativeLayout endBtnlayout) {
        WebAPIResponseListener responseListener = new WebAPIResponseListener() {
            @Override
            public void onSuccessOfResponse(Object... arguments) {
                currentLayout.setVisibility(View.GONE);
                endBtnlayout.setVisibility(View.VISIBLE);
                Intent intent = new Intent(mActivity, VehicleInspectionScreen.class);
                mActivity.startActivity(intent);

            }

            @Override
            public void onFailOfResponse(Object... arguments) {
                VolleyError error = (VolleyError) arguments[0];
                Toast.makeText(mActivity, "Operation failed.", Toast.LENGTH_SHORT).show();
                Log.i(MyJobsAdapter.class.getSimpleName(), "Job operation error " + error.toString());
            }
        };

        return responseListener;
    }

    public class ViewHolder {
        TextView confirm_job_btn, mobile, plateNumber;
        RelativeLayout currentjobbuttonLayout;
        RelativeLayout endButtonLayout;
        TextView txt_color;
        TextView txt_make;
        TextView startTime;
        TextView venue;
        TextView model;
        TextView customer;
        Button startBtn, releaseBtn;
    }
}
