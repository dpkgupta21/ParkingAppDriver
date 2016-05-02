package com.parking.app.parkingappdriver.myjobs;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parking.app.parkingappdriver.R;
import com.parking.app.parkingappdriver.currentjobs.CurrentJobsFragment;
import com.parking.app.parkingappdriver.model.LoadJobsDTO;

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

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        LoadJobsDTO jobsDTO = mMyJobsModelArrayList.get(position);
        holder.mobile.setText(jobsDTO.getMobile_no());
        holder.txt_color.setText(jobsDTO.getVehicle_Color());
        holder.txt_make.setText(jobsDTO.getVehicle_Make());
        holder.venue.setText(jobsDTO.getVenueName());
        holder.model.setText(jobsDTO.getVehicle_Model());
        holder.customer.setText(jobsDTO.getCustomerName());
        holder.plateNumber.setText(jobsDTO.getPlate_No());
        holder.startTime.setText(jobsDTO.getJobStartTime());

        if (myJobsFragment instanceof CurrentJobsFragment) {
            holder.confirm_job_btn.setVisibility(View.GONE);
            if (jobsDTO.getJob_Status().equalsIgnoreCase("lock")) {
                holder.currentjobbuttonLayout.setVisibility(View.VISIBLE);
                holder.startBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                holder.releaseBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            } else {
                holder.currentjobbuttonLayout.setVisibility(View.GONE);
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


    public class ViewHolder {
        TextView confirm_job_btn, mobile, plateNumber;
        RelativeLayout currentjobbuttonLayout;
        TextView txt_color;
        TextView txt_make;
        TextView startTime;
        TextView venue;
        TextView model;
        TextView customer;
        Button startBtn, releaseBtn;
    }
}
