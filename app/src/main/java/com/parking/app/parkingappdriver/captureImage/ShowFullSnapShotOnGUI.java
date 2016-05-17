package com.parking.app.parkingappdriver.captureImage;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.parking.app.parkingappdriver.R;
import com.parking.app.parkingappdriver.activity.BaseActivity;
import com.parking.app.parkingappdriver.utils.AppConstants;


/**
 * This class is used to Show full screen image on GUI
 */
public class ShowFullSnapShotOnGUI extends BaseActivity {

    private ImageView fullScreenSnap;
    private Toolbar mToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_full_snapshot_);
        initViews();
        setImageOnGUI();
    }


    /**
     * Init Views is used to initialize the views in the UI.
     */
    private void initViews() {
        /* Intent get data handler */
        fullScreenSnap = (ImageView) findViewById(R.id.fullScreenSnap);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(AppConstants.imageName);
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

    /**
     * Set Image ON GUI
     */
    private void setImageOnGUI() {

        // Capture position and set to the ImageView
        if (AppConstants.fullScreenBitmap != null) {
            fullScreenSnap.setImageBitmap(AppConstants.fullScreenBitmap);
        }

    }

}
