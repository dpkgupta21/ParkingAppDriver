package com.parking.app.parkingappdriver.drivermodel.myjobs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.parking.app.parkingappdriver.R;

import java.util.ArrayList;


public class MyJobsFragment extends Fragment implements View.OnClickListener {

    private Toolbar mToolbar;
    private View view;

    private ListView listView;

    private MyJobsAdapter myJobsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_myjobs_screen, container, false);
        return view;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initViews();
        assignClicks();
        myJobsAdapter = new MyJobsAdapter(getActivity());
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("aas");
        arrayList.add("aas");
        arrayList.add("aas");
        arrayList.add("aas");
        arrayList.add("aas");
        arrayList.add("aas");
        arrayList.add("aas");

        myJobsAdapter.addDataOnList(arrayList);

        listView.setAdapter(myJobsAdapter);

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


}
