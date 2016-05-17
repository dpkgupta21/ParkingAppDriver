package com.parking.app.parkingappdriver.appIntroduction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parking.app.parkingappdriver.fragments.BaseFragment;

public class WelcomeScreenFragment extends BaseFragment {
 
    final static String LAYOUT_ID = "layoutId";
 
    public static WelcomeScreenFragment newInstance(int layoutId) {
        WelcomeScreenFragment pane = new WelcomeScreenFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(LAYOUT_ID, layoutId);
        pane.setArguments(bundle);
        return pane;
    }
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(getArguments().getInt(LAYOUT_ID, -1), container, false);
        return root;
    }
}