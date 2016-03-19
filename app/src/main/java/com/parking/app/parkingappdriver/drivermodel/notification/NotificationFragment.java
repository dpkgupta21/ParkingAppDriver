package com.parking.app.parkingappdriver.drivermodel.notification;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.parking.app.parkingappdriver.R;

import java.util.ArrayList;


public class NotificationFragment extends Fragment implements View.OnClickListener {

    Toolbar mToolbar;
    View view;

    ListView listView;
    NotificationAdapter mNotificationAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_notification_screen, container, false);
        return view;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initViews();
        assignClicks();
        mNotificationAdapter = new NotificationAdapter(getActivity());
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("aas");
        arrayList.add("aas");
        arrayList.add("aas");
        arrayList.add("aas");
        arrayList.add("aas");
        arrayList.add("aas");
        arrayList.add("aas");

        mNotificationAdapter.addDataOnList(arrayList);

        listView.setAdapter(mNotificationAdapter);

    }


    private void assignClicks() {

    }

    private void initViews() {
        listView = (ListView) view.findViewById(R.id.notification_listview);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }


}
