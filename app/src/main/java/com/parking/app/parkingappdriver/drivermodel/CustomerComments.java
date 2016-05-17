package com.parking.app.parkingappdriver.drivermodel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.parking.app.parkingappdriver.R;
import com.parking.app.parkingappdriver.activity.BaseActivity;


public class CustomerComments extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_comments);
        initViews();
        assignClicks();
    }

    private void assignClicks() {
        setClick(R.id.take_vehicle_button);
    }


    private void initViews() {

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.cust_comnts));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationIcon(R.drawable.back_button);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        int id = item.getItemId();
        switch (id) {

            case android.R.id.home:
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.take_vehicle_button:
                startActivity(new Intent(this, VehicleInspectionScreen.class));
                break;
        }
    }
}
