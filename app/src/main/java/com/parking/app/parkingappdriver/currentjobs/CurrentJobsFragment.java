package com.parking.app.parkingappdriver.currentjobs;

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
import com.parking.app.parkingappdriver.myjobs.MyJobsAdapter;
import com.parking.app.parkingappdriver.webservices.handler.CurrentJobsAPIHandler;
import com.parking.app.parkingappdriver.webservices.ihelper.WebAPIResponseListener;
import java.lang.reflect.Type;
import java.util.ArrayList;


public class CurrentJobsFragment extends BaseFragment {

    private View view;
    private Activity mActivity;
    private ListView listView;
    private static ArrayList<LoadJobsDTO> jobsList;
    private static MyJobsAdapter myJobsAdapter;

    public static CurrentJobsFragment newInstance(String param1, String param2) {
        CurrentJobsFragment fragment = new CurrentJobsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_current_jobs, container, false);
        mActivity = getActivity();
        initViews();

        jobsList = new ArrayList<>();

        //calling current job web service.
        new CurrentJobsAPIHandler(mActivity, manageResponseListner());
        myJobsAdapter = new MyJobsAdapter(mActivity, this);

        listView.setAdapter(myJobsAdapter);
        return view;
    }

    private void initViews() {
        listView = (ListView) view.findViewById(R.id.jobs_listview);
    }

    private static WebAPIResponseListener manageResponseListner() {
        WebAPIResponseListener webAPIResponseListener = new WebAPIResponseListener() {
            @Override
            public void onSuccessOfResponse(Object... arguments) {
                String response = (String) arguments[0];
                Gson gson = new Gson();
                Type listType = new TypeToken<ArrayList<LoadJobsDTO>>() {
                }.getType();
                if (jobsList != null) {
                    jobsList.clear();
                }
                jobsList = gson.fromJson(response, listType);
                myJobsAdapter.addDataOnList(jobsList);
                myJobsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailOfResponse(Object... arguments) {

            }
        };

        return webAPIResponseListener;
    }
}
