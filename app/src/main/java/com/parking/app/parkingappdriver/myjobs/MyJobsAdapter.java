package com.parking.app.parkingappdriver.myjobs;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parking.app.parkingappdriver.R;
import com.parking.app.parkingappdriver.model.LoadJobsDTO;
import com.parking.app.parkingappdriver.model.MyJobsDTO;

import java.util.ArrayList;


/**
 * Created by Harish on 12/16/2015.
 */
public class MyJobsAdapter extends BaseAdapter {


    Activity mActivity;
    LayoutInflater mLayoutInflater;

    ArrayList<LoadJobsDTO> mMyJobsModelArrayList;
    private MyJobsFragment myJobsFragment;


    public MyJobsAdapter(Activity mActivity, MyJobsFragment myJobsFragment) {
        this.mActivity = mActivity;
        this.myJobsFragment = myJobsFragment;

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
            holder.id = (TextView) convertView.findViewById(R.id.id);
            holder.confirm_job_btn = (TextView) convertView.findViewById(R.id.confirm_job_btn);


            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //holder.id.setText(mMyJobsModelArrayList.get(position).get_id());

        holder.confirm_job_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //myJobsFragment.confirmJob(mMyJobsModelArrayList.get(position).get_id());
            }
        });

        return convertView;
    }


    public class ViewHolder {
        TextView confirm_job_btn, id;
        RelativeLayout new_rl;
    }
}
