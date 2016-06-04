package com.parking.app.parkingappdriver.drivermodel;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parking.app.parkingappdriver.R;
import com.parking.app.parkingappdriver.fragments.BaseFragment;


public class StartJobFragment extends BaseFragment {

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.driver_start_job_screen, container, false);
        return view;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        assignClicks();
    }


    private void assignClicks() {
        setClick(R.id.start_job_button, view);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.start_job_button:
                startActivity(new Intent(getActivity(), VehicleInspectionScreen.class));
               // startActivity(new Intent(getActivity(), CustomerComments.class));
                break;
        }
    }
}
