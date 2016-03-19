package com.parking.app.parkingappdriver.drivermodel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parking.app.parkingappdriver.R;


public class CustomerComments extends AppCompatActivity implements View.OnClickListener {

    private Toolbar mToolbar;
    private TextView toolbar_title;
    private RelativeLayout take_vehicle_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_comments);
        initViews();
        assignClicks();
    }

    private void assignClicks() {
        take_vehicle_button.setOnClickListener(this);
    }


    private void initViews() {

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.cust_comnts));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationIcon(R.drawable.back_button);
        take_vehicle_button = (RelativeLayout) findViewById(R.id.take_vehicle_button);
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
