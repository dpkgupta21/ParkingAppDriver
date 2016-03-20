package com.parking.app.parkingappdriver.myjobs;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.parking.app.parkingappdriver.R;
import com.parking.app.parkingappdriver.model.MyJobsDTO;
import com.parking.app.parkingappdriver.webservices.handler.ConfirmJobAPIHandler;
import com.parking.app.parkingappdriver.webservices.handler.MyJobsAPIHandler;
import com.parking.app.parkingappdriver.webservices.ihelper.WebAPIResponseListener;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class MyJobsFragment extends Fragment implements View.OnClickListener {

    private Toolbar mToolbar;
    private View view;
    private static Activity mActivity;
    private ListView listView;
    private static MyJobsAPIHandler myJobsAPIHandler;
    private static ConfirmJobAPIHandler confirmJobAPIHandler;

    private static ArrayList<MyJobsDTO> myJobsDTOArrayList;
    private static MyJobsAdapter myJobsAdapter;

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
        assignClicks();
        myJobsDTOArrayList = new ArrayList<MyJobsDTO>();

        myJobsAPIHandler = new MyJobsAPIHandler(mActivity, manageResponseListner());

        myJobsAdapter = new MyJobsAdapter(getActivity(), this);

        listView.setAdapter(myJobsAdapter);

    }

    private static WebAPIResponseListener manageResponseListner() {

        WebAPIResponseListener webAPIResponseListener = new WebAPIResponseListener() {
            @Override
            public void onSuccessOfResponse(Object... arguments) {

                String response = (String) arguments[0];
                Gson gson = new Gson();
                Type listType = new TypeToken<ArrayList<MyJobsDTO>>() {
                }.getType();
                if (myJobsDTOArrayList != null) {
                    myJobsDTOArrayList.clear();
                }
                myJobsDTOArrayList = gson.fromJson(response, listType);
                myJobsAdapter.addDataOnList(myJobsDTOArrayList);
                myJobsAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailOfResponse(Object... arguments) {

            }
        };
        return webAPIResponseListener;
    }


    private void assignClicks() {

    }

    private void initViews() {
        listView = (ListView) view.findViewById(R.id.jobs_listview);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }


    public static void confirmJob(String jobId) {
        confirmJobAPIHandler = new ConfirmJobAPIHandler(mActivity, jobId, "10002", new WebAPIResponseListener() {
            @Override
            public void onSuccessOfResponse(Object... arguments) {

                myJobsAPIHandler = new MyJobsAPIHandler(mActivity, manageResponseListner());
            }

            @Override
            public void onFailOfResponse(Object... arguments) {

            }
        });


    }


}
