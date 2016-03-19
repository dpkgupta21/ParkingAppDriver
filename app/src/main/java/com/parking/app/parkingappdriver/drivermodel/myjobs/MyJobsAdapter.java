package com.parking.app.parkingappdriver.drivermodel.myjobs;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parking.app.parkingappdriver.R;

import java.util.ArrayList;


/**
 * Created by Harish on 12/16/2015.
 */
public class MyJobsAdapter extends BaseAdapter {


    Activity mActivity;
    LayoutInflater mLayoutInflater;

    ArrayList<MyJobsModel> mMyJobsModelArrayList;
    ArrayList<String> dummyArrayList;


    public MyJobsAdapter(Activity mActivity) {
        this.mActivity = mActivity;

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
            ArrayList<String> bookingHistoryList) {
        this.dummyArrayList = bookingHistoryList;
    }

    @Override
    public int getCount() {
        if (dummyArrayList != null) {
            return dummyArrayList.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {

        if (dummyArrayList != null) {
            return dummyArrayList.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.my_jobs_screen_row, parent, false);
            holder = new ViewHolder();

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        return convertView;
    }


    public class ViewHolder {
        TextView ticket_value, airport_tv, pickup_value, drop_value;
        RelativeLayout new_rl;
    }
}
