package com.parking.app.parkingappdriver.myjobs;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.parking.app.parkingappdriver.R;
import com.parking.app.parkingappdriver.fragments.BaseFragment;
import com.parking.app.parkingappdriver.model.LoadJobsDTO;
import com.parking.app.parkingappdriver.webservices.handler.ConfirmJobAPIHandler;
import com.parking.app.parkingappdriver.webservices.handler.MyJobsAPIHandler;
import com.parking.app.parkingappdriver.webservices.ihelper.WebAPIResponseListener;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class MyJobsFragment extends BaseFragment {

    private View view;
    private static Activity mActivity;
    private ListView listView;

    private static ArrayList<LoadJobsDTO> myJobsDTOArrayList;
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

        myJobsDTOArrayList = new ArrayList<LoadJobsDTO>();

        new MyJobsAPIHandler(mActivity, manageResponseListner());

        myJobsAdapter = new MyJobsAdapter(getActivity(), this);

        listView.setAdapter(myJobsAdapter);

    }

    private static WebAPIResponseListener manageResponseListner() {

        WebAPIResponseListener webAPIResponseListener = new WebAPIResponseListener() {
            @Override
            public void onSuccessOfResponse(Object... arguments) {

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

            }

            @Override
            public void onFailOfResponse(Object... arguments) {

            }
        };
        return webAPIResponseListener;
    }

    private void initViews() {
        listView = (ListView) view.findViewById(R.id.jobs_listview);

    }

    public static void confirmJob(LoadJobsDTO jobsDTO) {
        new ConfirmJobAPIHandler(mActivity, jobsDTO.getJobId(),
                "5721bd345de9114b647d6d5c", new WebAPIResponseListener() {
            @Override
            public void onSuccessOfResponse(Object... arguments) {
                new MyJobsAPIHandler(mActivity, manageResponseListner());
            }

            @Override
            public void onFailOfResponse(Object... arguments) {

            }
        });

    }


}